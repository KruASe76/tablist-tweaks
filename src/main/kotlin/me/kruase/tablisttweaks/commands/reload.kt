package me.kruase.tablisttweaks.commands

import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.getUserConfig
import me.kruase.tablisttweaks.util.*
import org.bukkit.command.CommandSender


fun reload(sender: CommandSender, args: Array<out String>) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    if (args.isNotEmpty()) throw IllegalArgumentException()

    instance.server.onlinePlayers.forEach { it.disableFeatures() }

    userConfig = instance.getUserConfig()

    instance.server.onlinePlayers.forEach { it.initFeatures() }
}


fun reload() {
    reload(instance.server.consoleSender, emptyArray())
}
