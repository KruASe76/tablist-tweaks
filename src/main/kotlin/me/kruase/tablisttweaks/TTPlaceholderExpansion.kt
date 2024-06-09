package me.kruase.tablisttweaks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.util.placeholders
import org.bukkit.entity.Player


class TTPlaceholderExpansion : PlaceholderExpansion() {
    override fun getIdentifier() = "tablisttweaks"
    override fun getAuthor() = instance.description.authors.joinToString()
    override fun getVersion() = instance.description.version
    override fun persist(): Boolean = true

    override fun onPlaceholderRequest(player: Player?, name: String): String? {
        if (player == null)
            return null

        return when (name) {
            "dimension_color" -> player.placeholders?.dimensionColor ?: ""
            "dimension_dot" -> player.placeholders?.dimensionDot ?: ""
            "idle_badge" -> player.placeholders?.idleBadge ?: ""
            "idle_flag" -> player.placeholders?.idleFlag ?: ""
            else -> null
        }
    }
}
