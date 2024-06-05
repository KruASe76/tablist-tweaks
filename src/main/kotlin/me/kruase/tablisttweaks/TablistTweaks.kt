package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.commands.reload
import me.kruase.tablisttweaks.util.infoNotNull
import me.neznamy.tab.api.TabAPI
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


class TablistTweaks : JavaPlugin() {
    companion object {
        lateinit var instance: TablistTweaks
        lateinit var userConfig: TTConfig

        var tabApiInstance: TabAPI? = null

        val idlePlayerTaskIds = mutableMapOf<UUID, Int>()
    }

    override fun onEnable() {
        instance = this

        reload()

        if (server.pluginManager.isPluginEnabled("TAB")) {
            tabApiInstance = TabAPI.getInstance()
            logger.infoNotNull(userConfig.messages.info["tab-found"])
        }

        getCommand("tablisttweaks")!!.setExecutor(TTCommands())

        server.pluginManager.registerEvents(TTEvents(), instance)
    }
}
