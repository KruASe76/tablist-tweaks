package me.kruase.tablisttweaks.util

import me.kruase.tablisttweaks.TablistTweaks.Companion.idlePlayerTaskIds
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.tabApiInstance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player


val idleBadge = " ${ChatColor.GOLD}⌚${ChatColor.RESET}"


var Player.idleTaskId: Int?
    get() = idlePlayerTaskIds[uniqueId]
    set(value) {
        idlePlayerTaskIds[uniqueId] = value!!
    }

fun Player.deleteIdleTaskId() {
    idlePlayerTaskIds.remove(uniqueId)
}


fun Player.updateDimension(destinationLocation: Location = location, isInitial: Boolean = false) {
    val color = when (destinationLocation.world!!.environment) {
        World.Environment.NORMAL -> ChatColor.GREEN
        World.Environment.NETHER -> ChatColor.RED
        World.Environment.THE_END -> ChatColor.LIGHT_PURPLE
    }

    setDimensionColor(color, isInitial)
}


fun Player.startIdleTracking() {
    idleTaskId = instance.server.scheduler.scheduleSyncDelayedTask(
        instance,
        { addIdleBadge() },
        userConfig.idleTimeout
    )
}

fun Player.stopIdleTracking() {
    tryCancelIdleTask()
    deleteIdleTaskId()
}

fun Player.refreshIdleTracking() {
    tryCancelIdleTask()

    removeIdleBadge()

    startIdleTracking()
}

fun Player.tryCancelIdleTask() {
    idleTaskId
        ?.let { instance.server.scheduler.cancelTask(it) }
        ?: instance.logger.warnNotNull(
            userConfig.messages.warning["improper-tracking"]
                ?.replace("{player}", name)
        )
}


fun Player.setDimensionColor(color: ChatColor, isInitial: Boolean) {
    if (tabApiInstance == null) {
        val currentName =
            playerListName
                .removeSuffix(ChatColor.RESET.toString())
                .let {
                    if (isInitial) it
                    else it.drop(2)
                }
        // removing suffix ChatColor.RESET only if present
        // (it seems like Paper automatically removes ChatColor.RESET at the end of the playerListName)
        // and dropping only first color code

        setPlayerListName("$color$currentName${ChatColor.RESET}")
    } else {
        tabApiInstance
            ?.run {
                val tabPlayer = getPlayer(uniqueId) ?: return@run

                tabListFormatManager
                    ?.run {
                        setName(
                            tabPlayer,
                            "$color${getOriginalName(tabPlayer)}${ChatColor.RESET}"
                        )
                        // always using getOriginalName to override dimension color on every call
                        // also the color won't be overridden if getOriginalName is already colored
                    }
            }
    }
}

fun Player.addIdleBadge() {
    if (tabApiInstance == null) {
        setPlayerListName(playerListName + idleBadge)
    } else {
        tabApiInstance
            ?.run {
                val tabPlayer = getPlayer(uniqueId) ?: return@run

                tabListFormatManager
                    ?.run {
                        setSuffix(
                            tabPlayer,
                            (getCustomSuffix(tabPlayer) ?: getOriginalSuffix(tabPlayer)) + idleBadge
                        )
                    }
            }
    }
}

fun Player.removeIdleBadge() {
    if (tabApiInstance == null) {
        setPlayerListName(
            playerListName
                .removeSuffix(idleBadge)
                .removeSuffix(idleBadge.removeSuffix(ChatColor.RESET.toString()))  // for Paper again
        )
    } else {
        tabApiInstance
            ?.run {
                val tabPlayer = getPlayer(uniqueId) ?: return@run

                tabListFormatManager
                    ?.run {
                        setSuffix(
                            tabPlayer,
                            (getCustomSuffix(tabPlayer) ?: getOriginalSuffix(tabPlayer))
                                .removeSuffix(idleBadge)
                                .removeSuffix(idleBadge.removeSuffix(ChatColor.RESET.toString()))  // for Paper again
                        )
                    }
            }
    }
}
