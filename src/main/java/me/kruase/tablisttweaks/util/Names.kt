package me.kruase.tablisttweaks.util

import org.bukkit.ChatColor


fun getColoredPlayerName(name: String, color: ChatColor): String {
    return name.run {
        if (startsWith(ChatColor.COLOR_CHAR)) "$color${this.drop(2)}${ChatColor.RESET}"
        else "$color$this${ChatColor.RESET}"
    }
}
