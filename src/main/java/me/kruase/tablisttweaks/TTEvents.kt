package me.kruase.tablisttweaks

import java.util.*
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.entity.Player
import org.bukkit.Location
import org.bukkit.World.Environment
import org.bukkit.ChatColor
import me.kruase.tablisttweaks.util.getColoredPlayerName


val idleSuffix: String = " ${ChatColor.GOLD}⌚"
val idleTimeout: Long = TablistTweaks.instance.config.getLong("idle-indicator-timeout-seconds") * 20
val idlePlayerThreadIds: MutableMap<UUID, Int> = mutableMapOf()

class TTEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        updatePlayerDimension(event.player)
        startPlayerIdleTracking(event.player)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        stopPlayerIdleTracking(event.player)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        refreshPlayerIdleTracking(event.player)
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        updatePlayerDimension(event.player, event.to!!)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        updatePlayerDimension(event.player, event.respawnLocation)
    }
}


fun updatePlayerDimension(player: Player, location: Location = player.location) {
    player.setPlayerListName(
        getColoredPlayerName(
            player.playerListName,
            when (location.world!!.environment) {
                Environment.NORMAL -> ChatColor.GREEN
                Environment.NETHER -> ChatColor.RED
                Environment.THE_END -> ChatColor.LIGHT_PURPLE
            }
        )
    )
}


fun startPlayerIdleTracking(player: Player) {
    idlePlayerThreadIds[player.uniqueId] = trackPlayerIdle(player)
}

fun stopPlayerIdleTracking(player: Player) {
    TablistTweaks.instance.server.scheduler.cancelTask(idlePlayerThreadIds[player.uniqueId]!!)
    idlePlayerThreadIds.remove(player.uniqueId)
}

fun refreshPlayerIdleTracking(player: Player) {
    TablistTweaks.instance.server.scheduler.cancelTask(idlePlayerThreadIds[player.uniqueId]!!)
    player.setPlayerListName(player.playerListName.replace(idleSuffix, ""))
    startPlayerIdleTracking(player)
}

fun trackPlayerIdle(player: Player): Int {
    return TablistTweaks.instance.server.scheduler.scheduleSyncDelayedTask(
        TablistTweaks.instance, {
            player.setPlayerListName(player.playerListName + idleSuffix)
        }, idleTimeout
    )
}
