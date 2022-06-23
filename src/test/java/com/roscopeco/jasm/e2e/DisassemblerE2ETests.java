package com.roscopeco.jasm.e2e;

import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.disassemble;
import static com.roscopeco.jasm.TestUtil.doParseString;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertClass;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;

public class DisassemblerE2ETests {
    @Test
    void shouldDisassembleEmptyClass() {
        final var test = doParseString(disassemble("EmptyClass"));

        assertClass(test)
            .isPublic()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyClass");

        assertThat(test.classbody().member()).hasSize(1);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("<init>")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .label("label0:")
                .aload(0)
                .invokeSpecial("java/lang/Object", "<init>", "()V")
                .vreturn()
                .label("label1:")
                .noMoreCode();
    }

    @Test
    void shouldDisassembleEmptyClassWithDefaultAccess() {
        final var test = doParseString(disassemble("EmptyDefaultClass"));

        assertClass(test)
            .isNotPublic()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyDefaultClass");

        assertThat(test.classbody().member()).hasSize(1);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("<init>")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .label("label0:")
                .aload(0)
                .invokeSpecial("java/lang/Object", "<init>", "()V")
                .vreturn()
                .label("label1:")
                .noMoreCode();
    }

    @Test
    void shouldDisassembleEmptyInterface() {
        final var test = doParseString(disassemble("EmptyInterface"));

        assertClass(test)
            .isPublic()
            .isAbstract()
            .isInterface()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyInterface");

        assertThat(test.classbody()).isNull();
    }

    @Test
    void shouldDisassembleEmptyEnum() {
        final var test = doParseString(disassemble("EmptyEnum"));

        assertClass(test)
            .isPublic()
            .isEnum()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyEnum");

        assertThat(test.classbody().member()).hasSize(4);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("values")
            .hasDescriptor("()[Lcom/roscopeco/jasm/model/disasm/EmptyEnum;")
            .hasCodeSequence()
            .label("label0:")
            .getStatic("com/roscopeco/jasm/model/disasm/EmptyEnum", "$VALUES", "[Lcom/roscopeco/jasm/model/disasm/EmptyEnum;")
            .invokeVirtual("[Lcom/roscopeco/jasm/model/disasm/EmptyEnum", "clone", "()Ljava/lang/Object;")
            .checkcast("[Lcom/roscopeco/jasm/model/disasm/EmptyEnum")
            .areturn()
            .noMoreCode();
    }
}
