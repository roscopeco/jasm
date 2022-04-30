/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.util.List;
import java.util.function.Function;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNCHRONIZED;

class ModifiersTest {
    private Modifiers modifiers;
    private TerminalNode mockTerminal;

    @BeforeEach
    void setup() {
        this.modifiers = new Modifiers();
        this.mockTerminal = mock(TerminalNode.class);
    }

    @Test
    void testPrivateModifier() {
        runTest(ACC_PRIVATE, List.of(JasmParser.ModifierContext::PRIVATE));
    }

    @Test
    void testProtectedModifier() {
        runTest(ACC_PROTECTED, List.of(JasmParser.ModifierContext::PROTECTED));
    }

    @Test
    void testPublicModifier() {
        runTest(ACC_PUBLIC, List.of(JasmParser.ModifierContext::PUBLIC));
    }

    @Test
    void testFinalModifier() {
        runTest(ACC_FINAL, List.of(JasmParser.ModifierContext::FINAL));
    }

    @Test
    void testSynchronizedModifier() {
        runTest(ACC_SYNCHRONIZED, List.of(JasmParser.ModifierContext::SYNCHRONIZED));
    }

    @Test
    void testStaticModifier() {
        runTest(ACC_STATIC, List.of(JasmParser.ModifierContext::STATIC));
    }

    @Test
    void testModifiersAreCombinedCorrectly() {
        runTest(ACC_PUBLIC | ACC_FINAL,
                List.of(JasmParser.ModifierContext::PUBLIC, JasmParser.ModifierContext::FINAL)
        );

        runTest(ACC_PUBLIC | ACC_SYNCHRONIZED | ACC_FINAL,
                List.of(JasmParser.ModifierContext::PUBLIC,
                        JasmParser.ModifierContext::SYNCHRONIZED,
                        JasmParser.ModifierContext::FINAL
                )
        );

        runTest(ACC_PUBLIC | ACC_STATIC,
                List.of(JasmParser.ModifierContext::PUBLIC, JasmParser.ModifierContext::STATIC)
        );
    }

    private void runTest(
            final int expectedBitmap,
            @NonNull final List<Function<JasmParser.ModifierContext, TerminalNode>> mocks
    ) {
        final var mockContexts = mocks.stream().map(mockFunc -> {
            final var mock = mock(JasmParser.ModifierContext.class);
            when(mockFunc.apply(mock)).thenReturn(mockTerminal);
            return mock;
        }).toList();

        assertThat(modifiers.mapModifiers(mockContexts)).isEqualTo(expectedBitmap);
    }
}
