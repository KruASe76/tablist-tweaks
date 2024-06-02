package me.kruase.tablisttweaks.util

import org.bukkit.entity.Player
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.TablistTweaks.Companion.idlePlayerTaskIds


val idleBadge = " ${ChatColor.GOLD}⌚${ChatColor.RESET}"


var Player.idleTaskId: Int?
    get() = idlePlayerTaskIds[uniqueId]
    set(value) {
        idlePlayerTaskIds[uniqueId] = value!!
    }

fun Player.deleteIdleTaskId() {
    idlePlayerTaskIds.remove(uniqueId)
}


fun Player.coloredName(color: ChatColor, initial: Boolean): String {
    return "$color${
        if (initial) playerListName else playerListName.drop(2).removeSuffix(ChatColor.RESET.toString())
    }${ChatColor.RESET}"
    // dropping only first color code and then adding ChatColor.RESET only if not already present
    // (it seems like Paper automatically removes ChatColor.RESET at the end of the playerListName)
}

fun Player.updateDimension(destinationLocation: Location = location, initial: Boolean = false) {
    instance.server.scheduler.scheduleSyncDelayedTask(
        instance,
        {
            setPlayerListName(
                coloredName(
                    when (destinationLocation.world!!.environment) {
                        World.Environment.NORMAL -> ChatColor.GREEN
                        World.Environment.NETHER -> ChatColor.RED
                        World.Environment.THE_END -> ChatColor.LIGHT_PURPLE
                    },
                    initial
                )
            )
        },
        1L
    )
}


fun Player.startIdleTracking() {
    idleTaskId = instance.server.scheduler.scheduleSyncDelayedTask(
        instance,
        { setPlayerListName(playerListName + idleBadge) },
        userConfig.idleTimeout
    )
}

fun Player.stopIdleTracking() {
    tryCancelIdleTask()
    deleteIdleTaskId()
}

fun Player.refreshIdleTracking() {
    tryCancelIdleTask()

    setPlayerListName(
        playerListName
            .removeSuffix(idleBadge)
            .removeSuffix(idleBadge.removeSuffix(ChatColor.RESET.toString()))  // for Paper again
    )

    startIdleTracking()
}

fun Player.tryCancelIdleTask() {
    idleTaskId
        ?.let { instance.server.scheduler.cancelTask(it) }
        ?: instance.warnNotNull(
            userConfig.messages.warning["improper-tracking"]
                ?.replace("{player}", name)
        )
}
