package me.kruase.tablisttweaks

import org.bukkit.configuration.file.FileConfiguration
import java.io.File


data class TTConfig(private val config: FileConfiguration) {
    val idleTimeout = config.getLong("idle-badge-timeout-seconds") * 20
    val enabledFeatures = EnabledFeaturesConfig(config)
    val colors = ColorsConfig(config)
    val messages = MessagesConfig(config)
}


fun TablistTweaks.getUserConfig(): TTConfig {
    val configFile = File(dataFolder, "config.yml")
    val tempConfigFile = File(dataFolder, "temp-config.yml")
    val oldConfigFile = File(dataFolder, "old-config-${System.currentTimeMillis()}.yml")

    return try {
        saveDefaultConfig()
        reloadConfig()

        // validating current config
        val currentConfigKeys = config.getKeys(true)

        configFile.renameTo(tempConfigFile)
        saveDefaultConfig()
        reloadConfig()

        val defaultConfigKeys = config.getKeys(true)

        if ((defaultConfigKeys - currentConfigKeys).isNotEmpty())
            throw NullPointerException()
        else {
            configFile.delete()
            tempConfigFile.renameTo(configFile)
            reloadConfig()
        }

        TTConfig(config)
    } catch (e: Exception) {
        when (e) {
            is NullPointerException -> {
                logger.severe("Invalid $name config detected! Creating a new one (default)...")

                tempConfigFile.renameTo(oldConfigFile)

                logger.info("New (default) config created!")

                TTConfig(config)
            }
            else -> throw e
        }
    }
        .also { logger.info("Config loaded!") }
}


data class EnabledFeaturesConfig(private val config: FileConfiguration) {
    val dimensionColors = config.getBoolean("features.dimension-colors")
    val dimensionDots = config.getBoolean("features.dimension-dots")
    val idleTracking = config.getBoolean("features.idle-badge")
}

data class ColorsConfig(private val config: FileConfiguration) {
    val overworld = config.getString("colors.overworld")!!
    val nether = config.getString("colors.nether")!!
    val end = config.getString("colors.end")!!
    val idleBadge = config.getString("colors.idle-badge")!!
}

data class MessagesConfig(private val config: FileConfiguration) {
    val info: Map<String, String?> =
        config
            .getConfigurationSection("messages.info")!!
            .getKeys(false)
            .associateWith { config.getString("messages.info.$it") }
    val warning: Map<String, String?> =
        config
            .getConfigurationSection("messages.warning")!!
            .getKeys(false)
            .associateWith { config.getString("messages.warning.$it") }
    val error: Map<String, String?> =
        config
            .getConfigurationSection("messages.error")!!
            .getKeys(false)
            .associateWith { config.getString("messages.error.$it") }
    val help: Map<String, String?> =
        config
            .getConfigurationSection("messages.help")!!
            .getKeys(false)
            .associateWith { config.getString("messages.help.$it") }
}
