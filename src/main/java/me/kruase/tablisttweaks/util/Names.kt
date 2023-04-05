package me.kruase.tablisttweaks.util

import org.bukkit.ChatColor


fun getColoredPlayerName(name: String, color: ChatColor): String {
    return "$color$name${ChatColor.RESET}"
}
