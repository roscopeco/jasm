package com.roscopeco.jasm

internal object LiteralNames {
    private val LITERAL_NAME_REGEX = Regex("`(?:([^`])|$)")

    internal fun unescape(name: String): String =
        name.replace(LITERAL_NAME_REGEX, "$1")
}