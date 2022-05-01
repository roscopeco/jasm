/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JasmParser.ModifierContext
import org.objectweb.asm.Opcodes

class Modifiers {
    fun mapModifiers(modifiers: List<ModifierContext>): Int = modifiers
            .map { mapSingleModifierToAsm(it) }
            .fold(0) { value, modifier -> value or modifier }

    private fun mapSingleModifierToAsm(modifier: ModifierContext): Int =
        if (modifier.PUBLIC() != null) {
            Opcodes.ACC_PUBLIC
        } else if (modifier.PRIVATE() != null) {
            Opcodes.ACC_PRIVATE
        } else if (modifier.PROTECTED() != null) {
            Opcodes.ACC_PROTECTED
        } else if (modifier.FINAL() != null) {
            Opcodes.ACC_FINAL
        } else if (modifier.SYNCHRONIZED() != null) {
            Opcodes.ACC_SYNCHRONIZED
        } else if (modifier.STATIC() != null) {
            Opcodes.ACC_STATIC
        } else {
            0
        }
}