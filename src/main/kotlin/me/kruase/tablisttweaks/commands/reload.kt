package me.kruase.tablisttweaks.commands

import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.sendPlayerMessage
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.getUserConfig
import me.kruase.tablisttweaks.util.disableFeatures
import me.kruase.tablisttweaks.util.hasPluginPermission
import me.kruase.tablisttweaks.util.initFeatures
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


fun reload(sender: CommandSender) {
    if (!sender.hasPluginPermission("reload")) throw UnsupportedOperationException()

    instance.server.onlinePlayers.forEach { it.disableFeatures() }

    userConfig = instance.getUserConfig()

    instance.server.onlinePlayers.forEach { it.initFeatures() }

    if (sender is Player)
        sendPlayerMessage(sender, userConfig.messages.info["config-loaded"])
}


fun reload() {
    reload(instance.server.consoleSender)
}
