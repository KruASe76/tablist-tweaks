package me.kruase.tablisttweaks.util

import org.bukkit.command.CommandSender
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance


fun CommandSender.hasPluginPermission(name: String): Boolean {
    return hasPermission("${instance.name.lowercase()}.$name")
}
