package me.kruase.tablisttweaks

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.entity.EntityPortalEnterEvent
import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.World.Environment
import org.bukkit.ChatColor


val idleSuffix: String = " ${ChatColor.GOLD}⌚"
val idleTimeout: Long = TabListTweaks.instance.config.getLong("idle-indicator-timeout-seconds") * 20

class TLTEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        updatePlayerDimension(event.player, event.player.location.world!!.environment)
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
        updatePlayerDimension(event.player, event.to!!.world!!.environment)
    }

    @EventHandler
    fun onPlayerExitEndPortalEnter(event: EntityPortalEnterEvent) {  // End exit portal fix
        if (event.entity !is Player ||
            event.location.block.type != Material.END_PORTAL ||
            event.location.world!!.environment != Environment.THE_END) return
        updatePlayerDimension(event.entity as Player, Environment.NORMAL)
    }
}


fun updatePlayerDimension(player: Player, environment: Environment) {
    player.setPlayerListName(
        when (environment) {
            Environment.NORMAL -> "${ChatColor.GREEN}${player.name}"
            Environment.NETHER -> "${ChatColor.RED}${player.name}"
            Environment.THE_END -> "${ChatColor.LIGHT_PURPLE}${player.name}"
            Environment.CUSTOM -> player.name
        }
    )
}


fun startPlayerIdleTracking(player: Player) {
    TabListTweaks.idlePlayerThreadIds[player.uniqueId] = trackPlayerIdle(player)
}

fun stopPlayerIdleTracking(player: Player) {
    TabListTweaks.instance.server.scheduler.cancelTask(TabListTweaks.idlePlayerThreadIds[player.uniqueId]!!)
    TabListTweaks.idlePlayerThreadIds.remove(player.uniqueId)
}

fun refreshPlayerIdleTracking(player: Player) {
    TabListTweaks.instance.server.scheduler.cancelTask(TabListTweaks.idlePlayerThreadIds[player.uniqueId]!!)
    player.setPlayerListName(player.playerListName.replace(idleSuffix, ""))
    startPlayerIdleTracking(player)
}

fun trackPlayerIdle(player: Player): Int {
    return TabListTweaks.instance.server.scheduler.scheduleSyncDelayedTask(
        TabListTweaks.instance, {
            player.setPlayerListName(player.playerListName + idleSuffix)
        }, idleTimeout
    )
}
