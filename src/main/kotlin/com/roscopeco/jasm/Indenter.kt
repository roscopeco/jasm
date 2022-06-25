package com.roscopeco.jasm

interface Indenter {
    fun indented(s: String): String
    fun indent(): Indenter
    fun outdent(): Indenter
}

abstract class NStringIndenter(private val indentWith: String, private val currentIndent: Int) : Indenter {
    override fun indented(s: String): String = "${(0..currentIndent).map { indentWith }.joinToString()}$s"
}

class SpaceIndenter(private val baseIndent: Int, private val currentIndent: Int) : NStringIndenter(" ", currentIndent) {
    constructor(baseIndent: Int) : this(baseIndent, 0)

    override fun indented(s: String): String = "${(0 until currentIndent).map { " " }.joinToString(separator = "")}$s"
    override fun indent(): Indenter = SpaceIndenter(baseIndent, currentIndent + baseIndent)
    override fun outdent(): Indenter = if (currentIndent > 0) SpaceIndenter(baseIndent, currentIndent - baseIndent) else this
}

