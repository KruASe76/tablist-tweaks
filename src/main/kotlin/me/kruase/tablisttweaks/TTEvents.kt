package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.util.initFeatures
import me.kruase.tablisttweaks.util.refreshIdleTracking
import me.kruase.tablisttweaks.util.stopIdleTracking
import me.kruase.tablisttweaks.util.updateDimension
import me.kruase.tablisttweaks.util.updateTablistNameWithStats
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.entity.Player

class TTEvents : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.initFeatures()
        event.player.updateTablistNameWithStats()
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
        event.player.updateTablistNameWithStats()
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

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player && userConfig.enabledFeatures.showNumericHealth) {
            // Delay update to after damage is applied
            player.server.scheduler.runTaskLater(
                me.kruase.tablisttweaks.TablistTweaks.instance,
                Runnable { player.updateTablistNameWithStats() },
                1L
            )
        }
    }

    @EventHandler
    fun onPlayerHeal(event: EntityRegainHealthEvent) {
        val player = event.entity
        if (player is Player && userConfig.enabledFeatures.showNumericHealth) {
            // Delay update to after healing is applied
            player.server.scheduler.runTaskLater(
                me.kruase.tablisttweaks.TablistTweaks.instance,
                Runnable { player.updateTablistNameWithStats() },
                1L
            )
        }
    }

    @EventHandler
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        if (userConfig.enabledFeatures.showNumericHealth) {
            // Delay update to after item effect is applied
            event.player.server.scheduler.runTaskLater(
                me.kruase.tablisttweaks.TablistTweaks.instance,
                Runnable { event.player.updateTablistNameWithStats() },
                2L
            )
        }
    }
}
