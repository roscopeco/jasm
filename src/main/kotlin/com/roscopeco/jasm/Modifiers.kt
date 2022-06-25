/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import org.antlr.v4.runtime.ParserRuleContext
import org.objectweb.asm.Opcodes

class Modifiers {
    private val forwardMap = mapOf(
        "abstract"      to Opcodes.ACC_ABSTRACT,
        "annotation"    to Opcodes.ACC_ANNOTATION,
        "bridge"        to Opcodes.ACC_BRIDGE,
        "deprecated"    to Opcodes.ACC_DEPRECATED,
        "enum"          to Opcodes.ACC_ENUM,
        "final"         to Opcodes.ACC_FINAL,
        "interface"     to Opcodes.ACC_INTERFACE,
        "native"        to Opcodes.ACC_NATIVE,
        "private"       to Opcodes.ACC_PRIVATE,
        "protected"     to Opcodes.ACC_PROTECTED,
        "public"        to Opcodes.ACC_PUBLIC,
        "record"        to Opcodes.ACC_RECORD,
        "static"        to Opcodes.ACC_STATIC,
        "strict"        to Opcodes.ACC_STRICT,
        "super"         to Opcodes.ACC_SUPER,
        "synchronized"  to Opcodes.ACC_SYNCHRONIZED,
        "synthetic"     to Opcodes.ACC_SYNTHETIC,
        "transient"     to Opcodes.ACC_TRANSIENT,
        "varargs"       to Opcodes.ACC_VARARGS,
        "volatile"      to Opcodes.ACC_VOLATILE
    )

    private val reverseClassMap = listOf(  /* Keep in JLS order for nice output */
        Pair(Opcodes.ACC_PUBLIC, "public"),
        Pair(Opcodes.ACC_ABSTRACT, "abstract"),
        Pair(Opcodes.ACC_INTERFACE, "interface"),
        Pair(Opcodes.ACC_ENUM, "enum"),
        Pair(Opcodes.ACC_SYNTHETIC, "synthetic"),
    )

    private val reverseMethodMap = listOf(  /* Keep in JLS order for nice output */
        Pair(Opcodes.ACC_PRIVATE, "private"),
        Pair(Opcodes.ACC_PROTECTED, "protected"),
        Pair(Opcodes.ACC_PUBLIC, "public"),
        Pair(Opcodes.ACC_STATIC, "static"),
        Pair(Opcodes.ACC_ABSTRACT, "abstract"),
        Pair(Opcodes.ACC_SYNCHRONIZED, "synchronized"),
        Pair(Opcodes.ACC_FINAL, "final"),
        Pair(Opcodes.ACC_NATIVE, "native"),
        Pair(Opcodes.ACC_SYNTHETIC, "synthetic"),
        Pair(Opcodes.ACC_BRIDGE, "bridge"),
        Pair(Opcodes.ACC_VARARGS, "varargs"),
    )

    private val reverseFieldMap = listOf(  /* Keep in JLS order for nice output */
        Pair(Opcodes.ACC_PRIVATE, "private"),
        Pair(Opcodes.ACC_PROTECTED, "protected"),
        Pair(Opcodes.ACC_PUBLIC, "public"),
        Pair(Opcodes.ACC_STATIC, "static"),
        Pair(Opcodes.ACC_FINAL, "final"),
        Pair(Opcodes.ACC_TRANSIENT, "volatile"),
        Pair(Opcodes.ACC_TRANSIENT, "transient"),
        Pair(Opcodes.ACC_SYNTHETIC, "synthetic"),
    )

    fun mapModifiers(modifiers: List<ParserRuleContext>): Int = modifiers
            .map { mod -> forwardMap[mod.text]!! }
            .fold(0) { value, modifier -> value or modifier }

    fun disassembleClassModifiers(modifiers: Int): String = disassembleModifiers(modifiers, reverseClassMap)

    fun disassembleMethodModifiers(modifiers: Int): String = disassembleModifiers(modifiers, reverseMethodMap)

    fun disassembleFieldModifiers(modifiers: Int): String = disassembleModifiers(modifiers, reverseFieldMap)

    private fun disassembleModifiers(modifiers: Int, reverseMap: List<Pair<Int, String>>)= reverseMap.map {
            (bit, str) -> if (modifiers and bit == bit) str else ""
    }
        .filter { it.isNotEmpty() }
        .joinToString(separator = " ")
}