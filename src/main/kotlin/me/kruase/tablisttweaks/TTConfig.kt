package me.kruase.tablisttweaks

import org.bukkit.configuration.file.FileConfiguration
import java.io.File


val allPaths = listOf(
    "idle-badge-timeout-seconds",
    "features.dimension-colors",
    "features.idle-badge",
    "messages.error.no-permission",
    "messages.error.invalid-command",
    "messages.help.header",
    "messages.help.help",
    "messages.help.reload",
)


data class TTConfig(private val config: FileConfiguration) {
    val idleTimeout = config.getLong("idle-badge-timeout-seconds") * 20
    val enabledFeatures = EnabledFeaturesConfig(config)
    val messages = MessagesConfig(config)
}


fun TablistTweaks.getUserConfig(): TTConfig {
    return try {
        saveDefaultConfig()
        reloadConfig()

        if ((allPaths - config.getKeys(true)).isNotEmpty()) throw NullPointerException()

        TTConfig(config)
    } catch (e: Exception) {
        when (e) {
            is NullPointerException -> {
                newDefaultConfig()
                TTConfig(config)
            }
            else -> throw e
        }
    }.also { logger.info("Config loaded!") }
}

fun TablistTweaks.newDefaultConfig() {
    logger.severe("Invalid $name config detected! Creating a new one (default)...")
    File(dataFolder, "config.yml").renameTo(
        File(dataFolder, "config.yml.old-${System.currentTimeMillis()}")
    )
    saveDefaultConfig()
    reloadConfig()
    logger.info("New (default) config created!")
}


data class EnabledFeaturesConfig(private val config: FileConfiguration) {
    val dimensionColors = config.getBoolean("features.dimension-colors")
    val idleTracking = config.getBoolean("features.idle-badge")
}

data class MessagesConfig(private val config: FileConfiguration) {
    val help: Map<String, String> = config.getConfigurationSection("messages.help")!!
        .getKeys(false).associateWith { config.getString("messages.help.$it")!! }
    val error: Map<String, String> = config.getConfigurationSection("messages.error")!!
        .getKeys(false).associateWith { config.getString("messages.error.$it")!! }
    val warning: Map<String, String> = config.getConfigurationSection("messages.warning")!!
        .getKeys(false).associateWith { config.getString("messages.warning.$it")!! }
}
