package me.kruase.tablisttweaks.util

import org.bukkit.ChatColor


fun getColoredPlayerName(name: String, color: ChatColor, initial: Boolean): String {
    return "$color${if (initial) name else name.drop(2)}${ChatColor.RESET}"
}
