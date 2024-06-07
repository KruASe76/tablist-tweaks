package me.kruase.tablisttweaks.util

import me.kruase.tablisttweaks.TablistTweaks.Companion.idlePlayerTaskIds
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.tabApiInstance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player


const val dimensionDot = " ⏺"
val idleBadge = "${ChatColor.GOLD} ⌚${ChatColor.RESET}"


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


// when getting originalName, removing suffix ChatColor.RESET only if present
// (it seems like Paper automatically removes ChatColor.RESET at the end of the playerListName)
fun Player.setDimensionColor(color: ChatColor, isInitial: Boolean) {
    if (tabApiInstance == null) {
        if (userConfig.enabledFeatures.dimensionColorDots) {
            val originalName =
                playerListName
                    .removeSuffix(ChatColor.RESET.toString())
                    .let {
                        if (isInitial) it
                        else it
                            .removeSuffix(dimensionDot)
                            .dropLast(2)
                        // removing the dot and preceding color code
                    }

            setPlayerListName("$originalName$color$dimensionDot${ChatColor.RESET}")
        } else {
            val originalName =
                playerListName
                    .removeSuffix(ChatColor.RESET.toString())
                    .let {
                        if (isInitial) it
                        else it.drop(2)
                        // dropping only first color code
                    }

            setPlayerListName("$color$originalName${ChatColor.RESET}")
        }
    } else {
        tabApiInstance
            ?.run {
                val tabPlayer = getPlayer(uniqueId) ?: return@run
                tabListFormatManager
                    ?.run {
                        if (userConfig.enabledFeatures.dimensionColorDots) {
                            setSuffix(
                                tabPlayer,
                                "${getOriginalSuffix(tabPlayer)}$color$dimensionDot${ChatColor.RESET}"
                            )
                            // always using getOriginalSuffix to override dimension color on every call
                            // this would reset idleBadge, but... if player changes dimension, they're not AFK, right?
                        } else {
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
}

fun Player.unsetDimensionColor() {
    if (tabApiInstance == null) {
        setPlayerListName(
            playerListName
                .removeSuffix(ChatColor.RESET.toString())
                .let { name ->
                    if (dimensionDot in name) {
                        name.indexOf(dimensionDot)
                            .let { index ->
                                name.take(index - 2) + name.drop(index + dimensionDot.length)
                                // removing dimensionDot and preceding color code
                                // (possibly leaving a ChatColor.RESET... but it makes no difference)
                            }
                    }
                    else if (name.startsWith(ChatColor.COLOR_CHAR))
                        name.drop(2)
                    else
                        name
                }
        )
    } else {
        tabApiInstance
            ?.run {
                val tabPlayer = getPlayer(uniqueId) ?: return@run
                tabListFormatManager
                    ?.run {
                        setName(tabPlayer, getOriginalName(tabPlayer))
                        setSuffix(
                            tabPlayer,
                            getCustomSuffix(tabPlayer)
                                ?.removeSuffix(ChatColor.RESET.toString())
                                ?.let { suffix ->
                                    if (dimensionDot in suffix)
                                        suffix.indexOf(dimensionDot)
                                            .let { index ->
                                                suffix.take(index - 2) + suffix.drop(index + dimensionDot.length)
                                                // removing dimensionDot and preceding color code
                                                // (possibly leaving a ChatColor.RESET... but it makes no difference)
                                            }
                                    else
                                        suffix
                                }
                        )
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
                            getCustomSuffix(tabPlayer)
                                ?.removeSuffix(idleBadge)
                                ?.removeSuffix(idleBadge.removeSuffix(ChatColor.RESET.toString()))  // for Paper again
                        )
                    }
            }
    }
}
