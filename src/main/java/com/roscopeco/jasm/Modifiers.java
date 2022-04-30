/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.util.List;
import java.util.Optional;

import com.roscopeco.jasm.antlr.JasmParser;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNCHRONIZED;

class Modifiers {
    int mapModifiers(final List<JasmParser.ModifierContext> modifiers) {
        return Optional.ofNullable(modifiers).stream()
                .flatMap(List::stream)
                .map(this::mapSingleModifierToAsm)
                .reduce(0, (sum, i) -> sum | i);
    }

    private int mapSingleModifierToAsm(final JasmParser.ModifierContext modifier) {
        if (modifier.PUBLIC() != null) {
            return ACC_PUBLIC;
        } else if (modifier.PRIVATE() != null) {
            return ACC_PRIVATE;
        } else if (modifier.PROTECTED() != null) {
            return ACC_PROTECTED;
        } else if (modifier.FINAL() != null) {
            return ACC_FINAL;
        } else if (modifier.SYNCHRONIZED() != null) {
            return ACC_SYNCHRONIZED;
        } else if (modifier.STATIC() != null) {
            return ACC_STATIC;
        } else {
            return 0;
        }
    }
}
