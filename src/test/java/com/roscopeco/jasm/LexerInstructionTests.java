/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmLexer;
import lombok.NonNull;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;

class LexerInstructionTests {
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
    void shouldLexReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Return.jasm", lexer ->
                assertNextToken(lexer)
                        .hasType(JasmLexer.RETURN)
        );
    }

    private void runInstructionTest(
            @NonNull final String testCase,
            @NonNull final ThrowingConsumer<JasmLexer> assertions
    ) {
        final var lexer = testCaseLexer(testCase);

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.TYPENAME);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.TYPE_VOID);

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
