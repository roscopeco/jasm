/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmLexer;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;

class LexerTests {
    @Test
    void shouldLexEmptyFile() {
        final var lexer = testCaseLexer("emptyfile.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexEmptyClassDefinition() {
        final var lexer = testCaseLexer("EmptyClass.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("EmptyClass");

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexClassWithEmptyBody() {
        final var lexer = testCaseLexer("ClassWithEmptyBody.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("ClassWithEmptyBody");

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexPublicFinalEmptyClass() {
        final var lexer = testCaseLexer("PublicFinalEmptyClass.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.PUBLIC);

        assertNextToken(lexer)
                .hasType(JasmLexer.FINAL);

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("PublicFinalEmptyClass");

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexClassWithSingleField() {
        final var lexer = testCaseLexer("ClassWithSingleField.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS)
                .hasText("class");

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("ClassWithSingleField");

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.TYPE_INT);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("someField");

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexEmptyClassDefinitionInPackage() {
        final var lexer = testCaseLexer("com/roscopeco/jasm/EmptyClassInPackage.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/EmptyClassInPackage");

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexClassWithObjectField() {
        final var lexer = testCaseLexer("ClassWithObjectField.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS)
                .hasText("class");

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("ClassWithObjectField");

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.QNAME)
                .hasText("Ljava/lang/Object");

        assertNextToken(lexer)
                .hasType(JasmLexer.SEMI);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("someField");

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexClassWithMinimalMethod() {
        final var lexer = testCaseLexer("com/roscopeco/jasm/MinimalMethodTest.jasm");

        assertNextToken(lexer)
                .hasType(JasmLexer.CLASS)
                .hasText("class");

        assertNextToken(lexer)
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/MinimalMethodTest");

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.TYPE_VOID);

        assertNextToken(lexer)
                .hasType(JasmLexer.NAME)
                .hasText("testMethod");

        assertNextToken(lexer)
                .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
                .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
                .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.RETURN);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
                .hasType(JasmLexer.EOF);
    }
}
