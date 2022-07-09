/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JasmLexer;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.TestUtil.testCaseLexer;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertClass;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertNextToken;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertTokens;
import static org.assertj.core.api.Assertions.assertThat;

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
            .hasType(JasmLexer.NAME)
            .hasText("someField");

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_INT);

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
            .hasType(JasmLexer.NAME)
            .hasText("someField");

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Object");

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
            .hasType(JasmLexer.NAME)
            .hasText("testMethod");

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_VOID);

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

    @Test
    void shouldLexMethodArgumentsCorrectly() {
        final var lexer = testCaseLexer("MethodArgParsingTests.jasm");

        assertTokens(lexer, tokens -> {
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.CLASS);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("MethodArgParsingTests");

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            /* All primitives method */
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("allPrims");

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_BYTE);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_CHAR);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_DOUBLE);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_FLOAT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_INT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_LONG);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_SHORT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_BOOL);

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_VOID);

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            /* All primitives method (longhand) */
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("allPrimsLong");

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_BYTE);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_CHAR);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_DOUBLE);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_FLOAT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_INT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_LONG);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_SHORT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_BOOL);

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_VOID);

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            /* All references method */
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("allRefs");

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/Object");

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/util/List");

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            /* Mixed prims and references method */
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("mixPrimsAndRefsLongAndShort");

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_INT);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_LONG);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_BOOL);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/util/List");
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_BOOL);
            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.TYPE_BOOL);

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/util/List");

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            tokens.next()
                .hasType(JasmLexer.EOF);
        });
    }

    @Test
    @SuppressWarnings("java:S5961" /* We need to consume (and assert!) all the tokens */)
    void shouldLexClassWithSuperclassAndInterfaces() {
        final var lexer = testCaseLexer("com/roscopeco/jasm/InheritAndInterfaceTest.jasm");

        assertTokens(lexer, tokens -> {
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.CLASS);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/InheritAndInterfaceTest");

            tokens.next()
                .hasType(JasmLexer.EXTENDS);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/model/Superclass");

            tokens.next()
                .hasType(JasmLexer.IMPLEMENTS);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/model/Interface1");

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/model/Interface2");

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.INIT);

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_VOID);

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            tokens.next()
                .hasType(JasmLexer.ALOAD);

            tokens.next()
                .hasType(JasmLexer.INT)
                .hasText("0");

            tokens.next()
                .hasType(JasmLexer.INVOKESPECIAL);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/model/Superclass");

            tokens.next()
                .hasType(JasmLexer.DOT);

            tokens.next()
                .hasType(JasmLexer.INIT);

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.TYPE_VOID);

            tokens.next()
                .hasType(JasmLexer.RETURN);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            tokens.next()
                .hasType(JasmLexer.RBRACE);

            tokens.next()
                .hasType(JasmLexer.EOF);
        });
    }

    @Test
    void shouldLexClassWithArrayTypes() {
        final var lexer = testCaseLexer("com/roscopeco/jasm/ArrayTypesTest.jasm");

        assertTokens(lexer, tokens -> {
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.CLASS);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("com/roscopeco/jasm/ArrayTypesTest");

            tokens.next()
                .hasType(JasmLexer.LBRACE);

            // Field 1
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("arrayField");

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/Object");

            // Field 2
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("primArrayField");

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.TYPE_INT);

            // Method
            tokens.next()
                .hasType(JasmLexer.PUBLIC);

            tokens.next()
                .hasType(JasmLexer.NAME)
                .hasText("arrayTypesTest");

            tokens.next()
                .hasType(JasmLexer.LPAREN);

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.TYPE_INT);

            tokens.next()
                .hasType(JasmLexer.COMMA);

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.LSQUARE);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/String");

            tokens.next()
                .hasType(JasmLexer.RPAREN);

            tokens.next()
                .hasType(JasmLexer.QNAME)
                .hasText("java/lang/Object");

            /* no need to go further */
        });
    }

    @Test
    void shouldLexClassWithBlockComments() {
        final var lexer = testCaseLexer("ClassWithBlockComments.jasm");

        assertNextToken(lexer)
            .hasType(JasmLexer.BLOCK_COMMENT)
            .hasText("/*\n" +
                " * This is a block comment\n" +
                " */");

        assertNextToken(lexer)
            .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
            .hasType(JasmLexer.BLOCK_COMMENT)
            .hasText("/* This is also a block comment */");

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("ClassWithBlockComments");

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.EOF);
    }

    @Test
    void shouldLexClassWithTryCatch() {
        final var lexer = testCaseLexer("com/roscopeco/jasm/TryCatchTest.jasm");

        assertNextToken(lexer)
            .hasType(JasmLexer.PUBLIC);

        assertNextToken(lexer)
            .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("com/roscopeco/jasm/TryCatchTest");

        assertNextToken(lexer)
            .hasType(JasmLexer.IMPLEMENTS);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("com/roscopeco/jasm/model/TryCatchTest");

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        /* Manual exception handler */
        assertNextToken(lexer)
            .hasType(JasmLexer.BLOCK_COMMENT)
            .hasText("/* Manual exception handler */");

        assertNextToken(lexer)
            .hasType(JasmLexer.PUBLIC);

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("manualExceptionHandlerTest");

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.EXCEPTION);

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("tryBegin");

        assertNextToken(lexer)
            .hasType(JasmLexer.COMMA);

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("tryEnd");

        assertNextToken(lexer)
            .hasType(JasmLexer.COMMA);

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("catchBegin");

        assertNextToken(lexer)
            .hasType(JasmLexer.COMMA);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.LABEL)
            .hasText("tryBegin:");

        assertNextToken(lexer)
            .hasType(JasmLexer.NEW);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DUP);

        assertNextToken(lexer)
            .hasType(JasmLexer.LDC);

        assertNextToken(lexer)
            .hasType(JasmLexer.STRING)
            .hasText("\"Pass\"");

        assertNextToken(lexer)
            .hasType(JasmLexer.INVOKESPECIAL);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DOT);

        assertNextToken(lexer)
            .hasType(JasmLexer.INIT);

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/String");

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_VOID);

        assertNextToken(lexer)
            .hasType(JasmLexer.ATHROW);

        assertNextToken(lexer)
            .hasType(JasmLexer.LABEL)
            .hasText("tryEnd:");

        assertNextToken(lexer)
            .hasType(JasmLexer.NEW);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DUP);

        assertNextToken(lexer)
            .hasType(JasmLexer.LDC);

        assertNextToken(lexer)
            .hasType(JasmLexer.STRING)
            .hasText("\"Fail\"");

        assertNextToken(lexer)
            .hasType(JasmLexer.INVOKESPECIAL);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DOT);

        assertNextToken(lexer)
            .hasType(JasmLexer.INIT);

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/String");

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_VOID);

        assertNextToken(lexer)
            .hasType(JasmLexer.ARETURN);

        assertNextToken(lexer)
            .hasType(JasmLexer.LABEL)
            .hasText("catchBegin:");

        assertNextToken(lexer)
            .hasType(JasmLexer.CHECKCAST);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.ARETURN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RBRACE);

        /* With try/catch syntactic sugar */
        assertNextToken(lexer)
            .hasType(JasmLexer.BLOCK_COMMENT)
            .hasText("/* With try/catch syntactic sugar */");

        assertNextToken(lexer)
            .hasType(JasmLexer.PUBLIC);

        assertNextToken(lexer)
            .hasType(JasmLexer.NAME)
            .hasText("basicTryCatchTest");

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/String");

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.TRY);

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.NEW);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DUP);

        assertNextToken(lexer)
            .hasType(JasmLexer.INVOKESPECIAL);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.DOT);

        assertNextToken(lexer)
            .hasType(JasmLexer.INIT);

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.TYPE_VOID);

        assertNextToken(lexer)
            .hasType(JasmLexer.ATHROW);

        assertNextToken(lexer)
            .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.CATCH);

        assertNextToken(lexer)
            .hasType(JasmLexer.LPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/Exception");

        assertNextToken(lexer)
            .hasType(JasmLexer.RPAREN);

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.LDC);

        assertNextToken(lexer)
            .hasType(JasmLexer.STRING)
            .hasText("\"Pass\"");

        assertNextToken(lexer)
            .hasType(JasmLexer.ARETURN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.LDC);

        assertNextToken(lexer)
            .hasType(JasmLexer.STRING)
            .hasText("\"Fail\"");

        assertNextToken(lexer)
            .hasType(JasmLexer.ARETURN);

        assertNextToken(lexer)
            .hasType(JasmLexer.RBRACE);

        /* no need to go further */
    }

    @Test
    void shouldLexClassWithGetForNumericField() {
        // https://github.com/roscopeco/jasm/issues/35
        final var lexer = testCaseLexer("com/roscopeco/jasm/LiteralNames.jasm");
        assertNextToken(lexer)
            .hasType(JasmLexer.PUBLIC);

        assertNextToken(lexer)
            .hasType(JasmLexer.CLASS);

        assertNextToken(lexer)
            .hasType(JasmLexer.LITERAL_NAME)
            .hasText("`com/roscopeco/jasm/Literal Names`");

        assertNextToken(lexer)
            .hasType(JasmLexer.IMPLEMENTS);

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("com/roscopeco/jasm/model/LiteralNames");

        assertNextToken(lexer)
            .hasType(JasmLexer.LBRACE);

        assertNextToken(lexer)
            .hasType(JasmLexer.PRIVATE);

        assertNextToken(lexer)
            .hasType(JasmLexer.STATIC);

        assertNextToken(lexer)
            .hasType(JasmLexer.LITERAL_NAME)
            .hasText("`0`");

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/String");

        assertNextToken(lexer)
            .hasType(JasmLexer.EQUALS);

        assertNextToken(lexer)
            .hasType(JasmLexer.STRING)
            .hasText("\"test\"");

        assertNextToken(lexer)
            .hasType(JasmLexer.PRIVATE);

        assertNextToken(lexer)
            .hasType(JasmLexer.LITERAL_NAME)
            .hasText("`1`");

        assertNextToken(lexer)
            .hasType(JasmLexer.QNAME)
            .hasText("java/lang/String");

        // No need to go further - we're parsing literal names fine
    }
}
