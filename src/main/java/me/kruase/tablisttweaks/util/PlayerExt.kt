package me.kruase.tablisttweaks.util

import java.util.UUID
import org.bukkit.entity.Player
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig


val idleBadge = " ${ChatColor.GOLD}⌚${ChatColor.RESET}"

val idlePlayerThreadIds = mutableMapOf<UUID, Int>()


var Player.idleTreadId: Int
    get() = idlePlayerThreadIds[uniqueId]!!
    set(value) {
        idlePlayerThreadIds[uniqueId] = value
    }

fun Player.deleteIdleTreadId() {
    idlePlayerThreadIds.remove(uniqueId)
}


fun Player.coloredName(color: ChatColor, initial: Boolean): String {
    return "$color${if (initial) playerListName else playerListName.drop(2).dropLast(2)}${ChatColor.RESET}"
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
    idleTreadId = trackIdle()
}

fun Player.stopIdleTracking() {
    instance.server.scheduler.cancelTask(idleTreadId)
    deleteIdleTreadId()
}

fun Player.refreshIdleTracking() {
    instance.server.scheduler.cancelTask(idleTreadId)
    setPlayerListName(playerListName.replace(idleBadge, ""))
    startIdleTracking()
}

fun Player.trackIdle(): Int {
    return instance.server.scheduler.scheduleSyncDelayedTask(
        instance,
        { setPlayerListName(playerListName + idleBadge) },
        userConfig.idleTimeout
    )
}
