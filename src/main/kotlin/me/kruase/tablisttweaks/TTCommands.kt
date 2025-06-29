package me.kruase.tablisttweaks

import me.kruase.tablisttweaks.TablistTweaks.Companion.userConfig
import me.kruase.tablisttweaks.commands.help
import me.kruase.tablisttweaks.commands.reload
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor


class TTCommands : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        val fullArgs = args.dropLast(1)
        return when (fullArgs.getOrNull(0)) {
            null -> userConfig.messages.help.keys
                .filter { sender.hasPermission("tablisttweaks.${it.replace("-", ".")}") } - "header"
            "help" -> when {
                sender.hasPermission("tablisttweaks.help") -> when (fullArgs.getOrNull(1)) {
                    null -> userConfig.messages.help.keys
                        .filter { sender.hasPermission("tablisttweaks.${it.replace("-", ".")}") } - "header"
                    else -> listOf()
                }
                else -> listOf()
            }
            else -> listOf()
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            when (args.getOrNull(0)) {
                null -> help(sender, arrayOf())
                "help" -> help(sender, args.drop(1).toTypedArray())
                "reload" -> reload(sender)
            }
        } catch (e: UnsupportedOperationException) {
            sender.sendMessage(
                "${ChatColor.RED}${userConfig.messages.error["no-permission"] ?: "Error: no-permission"}"
            )
        } catch (e: IllegalArgumentException) {
            sender.sendMessage(
                "${ChatColor.RED}${
                    userConfig.messages.error["invalid-command"] ?: "Error: invalid-command"
                }"
            )
        } catch (e: IllegalStateException) {
            // "Unknown error" should never happen
            sender.sendMessage("${ChatColor.RED}${e.message ?: "Unknown error"}")
        }

        return true
    }
}
