package me.kruase.tablisttweaks.commands

import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.getUserConfig
import me.kruase.tablisttweaks.util.*
import org.bukkit.command.CommandSender


fun reload(sender: CommandSender, args: Array<out String>) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    if (args.isNotEmpty()) throw IllegalArgumentException()

    userConfig = instance.getUserConfig()

    instance.server.onlinePlayers.forEach { it.unsetDimensionColor() }
    if (userConfig.enabledFeatures.dimensionColors)
        instance.server.onlinePlayers.forEach { it.updateDimension(isInitial = true) }

    if (userConfig.enabledFeatures.idleTracking) {
        instance.server.onlinePlayers.forEach {
            it.stopIdleTracking()
            it.startIdleTracking()
        }
    } else  {
        instance.server.onlinePlayers.forEach {
            it.stopIdleTracking()
            it.removeIdleBadge()
        }
    }

    instance.server.onlinePlayers.forEach { player ->
        player.unsetDimensionColor()

        if (userConfig.enabledFeatures.dimensionColors)
            player.updateDimension(isInitial = true)

        player.stopIdleTracking()

        if (userConfig.enabledFeatures.idleTracking)
            player.startIdleTracking()
        else
            player.removeIdleBadge()
    }
}


fun reload() {
    reload(instance.server.consoleSender, emptyArray())
}
