package me.kruase.tablisttweaks.util


fun String.replaceLast(oldValue: String, newValue: String): String {
    if (!contains(oldValue)) return this

    return substringBeforeLast(oldValue) + newValue + substringAfterLast(oldValue)
}
