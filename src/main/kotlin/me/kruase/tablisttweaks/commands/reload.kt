package me.kruase.tablisttweaks.commands

import org.bukkit.command.CommandSender
import org.bukkit.ChatColor
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.getUserConfig
import me.kruase.tablisttweaks.util.*


fun reload(sender: CommandSender, args: Array<out String>) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    if (args.isNotEmpty()) throw IllegalArgumentException()

    userConfig = instance.getUserConfig()

    if (userConfig.enabledFeatures.dimensionColors) {
        instance.server.onlinePlayers.forEach { it.updateDimension(isInitial = true) }
    } else {
        instance.server.onlinePlayers.forEach {
            it.playerListName.run {
                val dropStart = if (startsWith(ChatColor.COLOR_CHAR)) 2 else 0
                val dropEnd = if (endsWith(ChatColor.RESET.toString())) 2 else 0

                it.setPlayerListName(drop(dropStart).dropLast(dropEnd))
            }
        }
    }

    if (userConfig.enabledFeatures.idleTracking) {
        instance.server.onlinePlayers.forEach {
            it.stopIdleTracking()
            it.startIdleTracking()
        }
    } else  {
        instance.server.onlinePlayers.forEach {
            it.stopIdleTracking()
            it.setPlayerListName(it.playerListName.replace(idleBadge, ""))
        }
    }
}


fun reload() {
    reload(instance.server.consoleSender, emptyArray())
}
