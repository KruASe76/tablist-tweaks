package me.kruase.tablisttweaks

import org.bukkit.plugin.java.JavaPlugin


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()

        server.pluginManager.registerEvents(TTEvents(), instance)
    }
}
