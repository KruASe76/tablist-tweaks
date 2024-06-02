package me.kruase.tablisttweaks

import java.util.UUID
import org.bukkit.plugin.java.JavaPlugin
import me.kruase.tablisttweaks.commands.reload


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
        lateinit var userConfig: TTConfig

        val idlePlayerTaskIds = mutableMapOf<UUID, Int>()
    }

    fun warnNotNull(message: String?) {
        message?.let { logger.warning(it) }
    }

    override fun onEnable() {
        instance = this

        reload(server.consoleSender, emptyArray())

        getCommand("tablisttweaks")!!.setExecutor(TTCommands())

        server.pluginManager.registerEvents(TTEvents(), instance)
    }
}
