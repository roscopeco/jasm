package com.roscopeco.jasm;

import com.roscopeco.jasm.antlr.JvmTypesLexer;
import com.roscopeco.jasm.antlr.JvmTypesParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JvmTypesParserTests {
    @Test
    void shouldParseVoidVoidMethodDescriptor() {
        final var ctx = parserForTest("()V").method_descriptor();

        assertThat(ctx.param()).isEmpty();
        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().VOID()).isNotNull();
    }

    @Test
    void shouldParseIntVoidMethodDescriptor() {
        final var ctx = parserForTest("(I)V").method_descriptor();

        assertThat(ctx.param())
            .hasSize(1)
            .extracting(JvmTypesParser.ParamContext::type)
            .extracting(RuleContext::getText)
            .containsExactly("I");

        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().VOID()).isNotNull();
    }

    @Test
    void shouldParseVoidIntMethodDescriptor() {
        final var ctx = parserForTest("()I").method_descriptor();

        assertThat(ctx.param()).isEmpty();

        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().prim_type()).isNotNull();
        assertThat(ctx.return_().type().prim_type().ARRAY()).isEmpty();
        assertThat(ctx.return_().type().prim_type().INT()).isNotNull();
    }

    @Test
    void shouldParseRefLongDescriptor() {
        final var ctx = parserForTest("(Ljava/lang/Object;)J").method_descriptor();

        assertThat(ctx.param())
            .hasSize(1)
            .extracting(JvmTypesParser.ParamContext::type)
            .extracting(JvmTypesParser.TypeContext::ref_type)
            .extracting(JvmTypesParser.Ref_typeContext::REF)
            .extracting(ParseTree::getText)
            .containsExactly("Ljava/lang/Object;");

        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().prim_type()).isNotNull();
        assertThat(ctx.return_().type().prim_type().ARRAY()).isEmpty();
        assertThat(ctx.return_().type().prim_type().LONG()).isNotNull();
    }

    @Test
    void shouldParseVoidRefDescriptor() {
        final var ctx = parserForTest("()Ljava/lang/Object;").method_descriptor();

        assertThat(ctx.param()).isEmpty();

        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().ref_type()).isNotNull();
        assertThat(ctx.return_().type().ref_type().ARRAY()).isEmpty();
        assertThat(ctx.return_().type().ref_type().REF().getText()).isEqualTo("Ljava/lang/Object;");
    }

    @Test
    void shouldParseIntArrayVoidDescriptor() {
        final var ctx = parserForTest("([I)V").method_descriptor();

        assertThat(ctx.param())
            .hasSize(1);

        assertThat(ctx.param())
            .extracting(JvmTypesParser.ParamContext::type)
            .extracting(JvmTypesParser.TypeContext::prim_type)
            .extracting(JvmTypesParser.Prim_typeContext::ARRAY)
            .hasSize(1);

        assertThat(ctx.param())
            .extracting(JvmTypesParser.ParamContext::type)
            .extracting(JvmTypesParser.TypeContext::prim_type)
            .extracting(JvmTypesParser.Prim_typeContext::INT)
            .extracting(ParseTree::getText)
            .containsExactly("I");

        assertThat(ctx.return_()).isNotNull();
        assertThat(ctx.return_().type()).isNotNull();
        assertThat(ctx.return_().type().VOID()).isNotNull();
    }

    private JvmTypesParser parserForTest(String input) {
        return new JvmTypesParser(new CommonTokenStream(new JvmTypesLexer(CharStreams.fromString(input))));
    }
}
