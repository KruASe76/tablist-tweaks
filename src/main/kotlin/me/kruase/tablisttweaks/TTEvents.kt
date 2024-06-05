package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.util.refreshIdleTracking
import me.kruase.tablisttweaks.util.startIdleTracking
import me.kruase.tablisttweaks.util.stopIdleTracking
import me.kruase.tablisttweaks.util.updateDimension
import me.neznamy.tab.api.event.player.PlayerLoadEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*


class TTEvents : Listener {
    init {
        registerTabApiEvents()
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (TablistTweaks.tabApiInstance == null)
            event.player.joinHandle()
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
        if (TablistTweaks.userConfig.enabledFeatures.idleTracking)
            event.player.stopIdleTracking()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (TablistTweaks.userConfig.enabledFeatures.idleTracking)
            event.player.refreshIdleTracking()
    }
}


fun Player.joinHandle() {
    if (TablistTweaks.userConfig.enabledFeatures.dimensionColors)
        updateDimension(isInitial = true)

    if (TablistTweaks.userConfig.enabledFeatures.idleTracking)
        startIdleTracking()
}


fun registerTabApiEvents() {
    TablistTweaks.tabApiInstance
        ?.run {
            eventBus?.register(PlayerLoadEvent::class.java) { event ->
                TablistTweaks.instance.server.getPlayer(event.player.uniqueId)
                    ?.joinHandle()
            }
        }
}
