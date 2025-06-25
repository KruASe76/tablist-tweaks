package me.kruase.tablisttweaks.util

import me.kruase.tablisttweaks.TablistTweaks.Companion.instance
import me.kruase.tablisttweaks.TablistTweaks.Companion.playerIdleTaskIds
import me.kruase.tablisttweaks.TablistTweaks.Companion.playerPlaceholders
import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World.Environment
import org.bukkit.entity.Player


const val dimensionDot = "⏺"
const val idleBadge = "⌚"


fun Player.initFeatures() {
    placeholders = PlayerPlaceholders()

    if (userConfig.enabledFeatures.dimensionColors)
        updateDimension(isInitial = true)

    if (userConfig.enabledFeatures.idleTracking)
        startIdleTracking()
}

fun Player.disableFeatures() {
    stopIdleTracking()
    removeIdleBadge()
    unsetDimensionColor()
}


var Player.idleTaskId: Int?
    get() = playerIdleTaskIds[uniqueId]
    set(value) {
        if (value == null)
            playerIdleTaskIds.remove(uniqueId)
        else
            playerIdleTaskIds[uniqueId] = value
    }


fun Player.updateDimension(destinationLocation: Location = location, isInitial: Boolean = false) {
    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    val color = when (destinationLocation.world!!.environment) {
        Environment.NORMAL -> userConfig.colors.overworld
        Environment.NETHER -> userConfig.colors.nether
        Environment.THE_END -> userConfig.colors.end
        else -> userConfig.colors.customNameMap[destinationLocation.world!!.name] ?: userConfig.colors.customDefault
    }

    if (!isInitial)
        removeIdleBadge()  // ...this is just logical

    setDimensionColor(color, isInitial)
}


fun Player.startIdleTracking() {
    idleTaskId = instance.server.scheduler.scheduleSyncDelayedTask(
        instance,
        {
            addIdleBadge()

            placeholders?.idleFlag = "true"
        },
        userConfig.idleTimeout
    )
}

fun Player.stopIdleTracking() {
    tryCancelIdleTask()
    idleTaskId = null

    placeholders?.idleFlag = "false"
}

fun Player.refreshIdleTracking() {
    tryCancelIdleTask()

    removeIdleBadge()

    startIdleTracking()

    placeholders?.idleFlag = "false"
}

fun Player.tryCancelIdleTask() {
    idleTaskId
        ?.let { instance.server.scheduler.cancelTask(it) }
        ?: instance.logger.warnNotNull(
            userConfig.messages.warning["improper-tracking"]
                ?.replace("{player}", name)
        )
}


// when getting originalName, removing suffix ChatColor.RESET only if present
// (it seems like Paper automatically removes ChatColor.RESET at the end of the playerListName)
fun Player.setDimensionColor(color: String, isInitial: Boolean) {
    if (!isInitial)
        unsetDimensionColor()

    val originalName = playerListName.removeSuffix(ChatColor.RESET.toString())

    if (userConfig.enabledFeatures.dimensionDots)
        setPlayerListName("$originalName $color$dimensionDot${ChatColor.RESET}")
    else
        setPlayerListName("$color$originalName${ChatColor.RESET}")

    placeholders?.dimensionColor = color
    placeholders?.dimensionDot = "$color$dimensionDot${ChatColor.RESET}"
}

fun Player.unsetDimensionColor() {
    setPlayerListName(
        playerListName
            .removeSuffix(ChatColor.RESET.toString())
            .let { name ->
                if (dimensionDot in name) {
                    name
                        .removeSuffix(dimensionDot)
                        .removeSuffix(userConfig.colors.overworld)
                        .removeSuffix(userConfig.colors.nether)
                        .removeSuffix(userConfig.colors.end)
                        .removeSuffix(" ")
                    // removing dimensionDot and preceding color code
                }
                else if (name.startsWith(ChatColor.COLOR_CHAR))
                    name
                        .removePrefix(userConfig.colors.overworld)
                        .removePrefix(userConfig.colors.nether)
                        .removePrefix(userConfig.colors.end)
                    // dropping first color code
                else
                    name
            }
            .ensureFinalColorReset()
    )
}

fun Player.addIdleBadge() {
    setPlayerListName("$playerListName ${userConfig.colors.idleBadge}$idleBadge${ChatColor.RESET}")

    placeholders?.idleBadge = "${userConfig.colors.idleBadge}$idleBadge${ChatColor.RESET}"
}

fun Player.removeIdleBadge() {
    setPlayerListName(
        playerListName
            .removeSuffix(ChatColor.RESET.toString())
            .removeSuffix(idleBadge)
            .removeSuffix(userConfig.colors.idleBadge)
            .removeSuffix(" ")
            .ensureFinalColorReset()
    )

    placeholders?.idleBadge = ""
}


fun String.ensureFinalColorReset(): String {
    return (
        if (!endsWith(ChatColor.RESET.toString()))
            this + ChatColor.RESET
        else
            this
    )
}


data class PlayerPlaceholders(
    var dimensionColor: String = "",
    var dimensionDot: String = "",
    var idleBadge: String = "",
    var idleFlag: String = "false",
)

var Player.placeholders: PlayerPlaceholders?
    get() = playerPlaceholders[uniqueId]
    set(value) {
        if (value == null)
            playerPlaceholders.remove(uniqueId)
        else
            playerPlaceholders[uniqueId] = value
    }
