package me.kruase.tablisttweaks.util

import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import org.bukkit.command.CommandSender


fun CommandSender.hasPluginPermission(name: String): Boolean {
    return hasPermission("${instance.name.lowercase()}.$name")
}
