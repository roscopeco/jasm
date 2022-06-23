package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JvmTypesLexer;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertToken;

public class JvmTypesLexerTests {
    @Test
    void shouldLexVoidVoidDescriptor() {
        final var lexer = lexerForTest("()V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexIntVoidDescriptor() {
        final var lexer = lexerForTest("(I)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexIntIntLongDescriptor() {
        final var lexer = lexerForTest("(II)J");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LONG);
    }

    @Test
    void shouldLexIntIntObjectDescriptor() {
        final var lexer = lexerForTest("(II)Ljava/lang/Object;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/lang/Object;");
    }

    @Test
    void shouldLexObjectObjectDescriptor() {
        final var lexer = lexerForTest("(Ljava/lang/String;)Ljava/lang/Object;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/lang/String;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/lang/Object;");
    }

    @Test
    void shouldLexAllTypesInDescriptor() {
        final var lexer = lexerForTest("(BCDFIJLjava/lang/String;Z)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);
        
        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.BYTE);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.CHAR);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.DOUBLE);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.FLOAT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LONG);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/lang/String;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.BOOL);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexIntArrayVoidDescriptor() {
        final var lexer = lexerForTest("([I)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexIntMultiArrayVoidDescriptor() {
        final var lexer = lexerForTest("([[I)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.INT);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexRefArrayVoidDescriptor() {
        final var lexer = lexerForTest("([Ljava/util/List;)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/util/List;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    @Test
    void shouldLexVoidRefArrayDescriptor() {
        final var lexer = lexerForTest("()[Ljava/util/List;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/util/List;");
    }

    @Test
    void shouldLexRefMultiArrayVoidDescriptor() {
        final var lexer = lexerForTest("([[Ljava/util/List;)V");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.LPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.ARRAY);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.REF)
            .hasText("Ljava/util/List;");

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.RPAREN);

        assertToken(lexer.nextToken())
            .hasType(JvmTypesLexer.VOID);
    }

    private JvmTypesLexer lexerForTest(String input) {
        return new JvmTypesLexer(CharStreams.fromString(input));
    }
}
