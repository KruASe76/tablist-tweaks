package me.kruase.tablisttweaks

import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID


class TabListTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TabListTweaks

        val idlePlayerThreadIds: MutableMap<UUID, Int> = mutableMapOf()
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()

        server.pluginManager.registerEvents(TLTEvents(), instance)
    }
}
