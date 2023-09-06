package me.kruase.tablisttweaks

import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerTeleportEvent
import me.kruase.tablisttweaks.util.*


class TTEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.dimensionColors) event.player.updateDimension(initial = true)
        if (TablistTweaks.userConfig.enabledFeatures.idleTracking) event.player.startIdleTracking()
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.dimensionColors)
            event.player.updateDimension(destinationLocation = event.to!!)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.dimensionColors)
            event.player.updateDimension(destinationLocation = event.respawnLocation)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.idleTracking) event.player.stopIdleTracking()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.idleTracking) event.player.refreshIdleTracking()
    }
}
