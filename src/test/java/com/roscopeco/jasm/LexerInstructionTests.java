/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmLexer;
import lombok.NonNull;
import org.assertj.core.api.LocalDateAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;

class LexerInstructionTests {
    @Test
    void shouldLexAconstNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/AconstNull.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ACONST_NULL));
    }

    @Test
    void shouldLexAload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aload.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ALOAD);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexAreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Areturn.jasm", REF_RETURN_TYPE_TOKEN, lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.ARETURN));
    }

    @Test
    void shouldLexFreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Freturn.jasm", lexer -> assertNextToken(lexer)
                .hasType(JasmLexer.FRETURN));
    }


    @Test
    void shouldLexIconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iconst.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.ICONST);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("0");
        });
    }

    @Test
    void shouldLexIreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ireturn.jasm", lexer -> assertNextToken(lexer)
                .hasType(JasmLexer.IRETURN));
    }

    @Test
    void shouldLexInvokeSpecial() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeSpecial.jasm", lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.INVOKESPECIAL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPENAME)
                    .hasText("java/lang/Object");

            assertNextToken(lexer)
                    .hasType(JasmLexer.DOT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INIT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.LPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPENAME)
                    .hasText("Ljava/lang/String");

            assertNextToken(lexer)
                    .hasType(JasmLexer.SEMI);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_INT);

            assertNextToken(lexer)
                    .hasType(JasmLexer.SEMI);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_BOOL);

            assertNextToken(lexer)
                    .hasType(JasmLexer.RPAREN);

            assertNextToken(lexer)
                    .hasType(JasmLexer.TYPE_VOID);
        });
    }

    @Test
    void shouldLexLdc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ldc.jasm", REF_RETURN_TYPE_TOKEN, lexer -> {
            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.INT)
                    .hasText("10");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.FLOAT)
                    .hasText("5.0");

            assertNextToken(lexer)
                    .hasType(JasmLexer.LDC);

            assertNextToken(lexer)
                    .hasType(JasmLexer.STRING)
                    .hasText("\"Test string\"");
        });
    }

    @Test
    void shouldLexReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Return.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.RETURN)
        );
    }

    private static final List<Integer> VOID_RETURN_TYPE_TOKEN = List.of(JasmLexer.TYPE_VOID);
    private static final List<Integer> REF_RETURN_TYPE_TOKEN = List.of(JasmLexer.TYPENAME, JasmLexer.SEMI);

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final ThrowingConsumer<JasmLexer> assertions
    ) {
        runInstructionTest(testCase, VOID_RETURN_TYPE_TOKEN, assertions);
    }

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final List<Integer> expectedReturnTokenTypes,
            @NonNull final ThrowingConsumer<JasmLexer> assertions
    ) {
        final var lexer = testCaseLexer(testCase);

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.TYPENAME);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        expectedReturnTokenTypes.forEach(token -> assertNextToken(lexer)
                .hasType(token)
        );

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("insnTest");

        assertNextToken(lexer)
                .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
                .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertions.accept(lexer);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }
}
