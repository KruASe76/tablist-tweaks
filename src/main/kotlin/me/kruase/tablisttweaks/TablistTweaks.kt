package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.commands.reload
import me.kruase.tablisttweaks.util.PlayerPlaceholders
import me.kruase.tablisttweaks.util.updateTablistNameWithStats
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
        lateinit var userConfig: TTConfig

        val playerIdleTaskIds = mutableMapOf<UUID, Int>()
        val playerPlaceholders = mutableMapOf<UUID, PlayerPlaceholders>()

        fun sendPlayerMessage(player: Player, message: String?) {
            if (message == null)
                return

            player.sendMessage(
                "${ChatColor.GOLD}[${ChatColor.GREEN}${instance.name}${ChatColor.GOLD}]${ChatColor.RESET} $message"
            )
        }
    }

    override fun onEnable() {
        instance = this

        reload()

        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            TTPlaceholderExpansion().register()
        }

        getCommand("tablisttweaks")!!.setExecutor(TTCommands())

        server.pluginManager.registerEvents(TTEvents(), instance)

        // Schedule repeating task to update ping in tablist if enabled
        server.scheduler.runTaskTimer(this, Runnable {
            if (userConfig.enabledFeatures.showNumericPing) {
                server.onlinePlayers.forEach { it.updateTablistNameWithStats() }
            }
        }, 20L, 40L) // every 2 seconds
    }
}
