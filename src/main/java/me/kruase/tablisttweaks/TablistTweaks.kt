package me.kruase.tablisttweaks

import org.bukkit.plugin.java.JavaPlugin
import me.kruase.tablisttweaks.commands.reload


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
        lateinit var userConfig: TTConfig
    }

    override fun onEnable() {
        instance = this

        reload(server.consoleSender, emptyArray())

        getCommand("tablisttweaks")!!.setExecutor(TTCommands())

        server.pluginManager.registerEvents(TTEvents(), instance)
    }
}
