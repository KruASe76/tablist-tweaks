package me.kruase.tablisttweaks.util

import java.util.logging.Logger


fun Logger.warnNotNull(message: String?) {
    message?.let { warning(it) }
}
