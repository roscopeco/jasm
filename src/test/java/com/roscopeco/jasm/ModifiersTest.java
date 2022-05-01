/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @BeforeEach
    void setup() {
        this.modifiers = new Modifiers();
    }

    @Test
    void testPrivateModifier() {
        runTest(ACC_PRIVATE, List.of("private"));
    }

    @Test
    void testProtectedModifier() {
        runTest(ACC_PROTECTED, List.of("protected"));
    }

    @Test
    void testPublicModifier() {
        runTest(ACC_PUBLIC, List.of("public"));
    }

    @Test
    void testFinalModifier() {
        runTest(ACC_FINAL, List.of("final"));
    }

    @Test
    void testSynchronizedModifier() {
        runTest(ACC_SYNCHRONIZED, List.of("synchronized"));
    }

    @Test
    void testStaticModifier() {
        runTest(ACC_STATIC, List.of("static"));
    }

    @Test
    void testModifiersAreCombinedCorrectly() {
        runTest(ACC_PUBLIC | ACC_FINAL,
                List.of("public", "final")
        );

        runTest(ACC_PUBLIC | ACC_SYNCHRONIZED | ACC_FINAL,
                List.of("public",
                        "synchronized",
                        "final"
                )
        );

        runTest(ACC_PUBLIC | ACC_STATIC,
                List.of("public", "static")
        );
    }

    private void runTest(final int expectedBitmap, @NonNull final List<String> mocks) {
        final var mockContexts = mocks.stream().map(mockFunc -> {
            final var mock = mock(JasmParser.Method_modifierContext.class);
            when(mock.getText()).thenReturn(mockFunc);
            return mock;
        }).toList();

        assertThat(modifiers.mapModifiers(mockContexts)).isEqualTo(expectedBitmap);
    }
}
