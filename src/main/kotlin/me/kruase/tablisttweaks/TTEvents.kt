package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.util.initFeatures
import me.kruase.tablisttweaks.util.refreshIdleTracking
import me.kruase.tablisttweaks.util.stopIdleTracking
import me.kruase.tablisttweaks.util.updateDimension
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*


class TTEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.initFeatures()
    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if (userConfig.enabledFeatures.dimensionColors)
            event.player.updateDimension(destinationLocation = event.to!!)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        if (userConfig.enabledFeatures.dimensionColors)
            event.player.updateDimension(destinationLocation = event.respawnLocation)
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        if (userConfig.enabledFeatures.idleTracking)
            event.player.stopIdleTracking()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (userConfig.enabledFeatures.idleTracking)
            event.player.refreshIdleTracking()
    }
}
