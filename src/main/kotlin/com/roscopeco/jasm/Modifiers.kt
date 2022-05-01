/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import org.antlr.v4.runtime.ParserRuleContext
import org.objectweb.asm.Opcodes

class Modifiers {
    val modifierMap = mapOf(
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

    fun mapModifiers(modifiers: List<ParserRuleContext>): Int = modifiers
            .map { mod -> println("!!!!!!!!!!!!! ${mod.text}") ;  modifierMap[mod.text]!! }
            .fold(0) { value, modifier -> value or modifier }
}