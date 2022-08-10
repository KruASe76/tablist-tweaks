package me.kruase.tablisttweaks

import org.bukkit.plugin.java.JavaPlugin


class TabListTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TabListTweaks
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()

        server.pluginManager.registerEvents(TLTEvents(), instance)
    }
}
