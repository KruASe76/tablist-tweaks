package me.kruase.tablisttweaks

import org.bukkit.plugin.java.JavaPlugin


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
        lateinit var userConfig: TTConfig
    }

    override fun onEnable() {
        instance = this
        userConfig = getUserConfig()

        getCommand("tablisttweaks")!!.setExecutor(TTCommands())

        server.pluginManager.registerEvents(TTEvents(), instance)
    }
}
