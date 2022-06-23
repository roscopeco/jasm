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
        // This is quite brittle, they could change the way enums work under the hood and this would start failing...
        System.out.println(disassemble("EmptyEnum"));
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

        assertMember(test.classbody().member(1))
            .isMethod()
            .isStatic()
            .hasName("valueOf")
            .hasDescriptor("(Ljava/lang/String;)Lcom/roscopeco/jasm/model/disasm/EmptyEnum;")
            .hasCodeSequence()
                .label("label0:")
                .ldcClass("com/roscopeco/jasm/model/disasm/EmptyEnum")
                .aload(0)
                .invokeStatic("java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;")
                .checkcast("com/roscopeco/jasm/model/disasm/EmptyEnum")
                .areturn()
                .label("label1:")
                .noMoreCode();

        assertMember(test.classbody().member(2))
            .isMethod()
            .isPrivate()
            .hasName("<init>")
            .hasDescriptor("(Ljava/lang/String;I)V")
            .hasCodeSequence()
                .label("label0:")
                .aload(0)
                .aload(1)
                .iload(2)
                .invokeSpecial("java/lang/Enum", "<init>", "(Ljava/lang/String;I)V")
                .vreturn()
                .label("label1:")
                .noMoreCode();

        assertMember(test.classbody().member(3))
            .isMethod()
            .isStatic()
            .hasName("<clinit>")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .label("label0:")
                .iconst(0)
                .anewarray("com/roscopeco/jasm/model/disasm/EmptyEnum")
                .putStatic("com/roscopeco/jasm/model/disasm/EmptyEnum", "$VALUES", "[Lcom/roscopeco/jasm/model/disasm/EmptyEnum;")
                .vreturn()
                .noMoreCode();
    }

    @Test
    void shouldDisassembleClassWithTryCatch() {
        final var test = doParseString(disassemble("ExceptionTest"));
        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .exception("label0", "label1", "label1", "java/lang/InterruptedException")
                .label("label0:")
                    .anew("java/lang/InterruptedException")
                    .dup()
                    .ldcStr("BANG")
                    .invokeSpecial("java/lang/InterruptedException", "<init>", "(Ljava/lang/String;)V")
                    .athrow()
                .label("label1:")
                    .astore(1)
                .label("label2:")
                    .invokeStatic("java/lang/Thread", "currentThread", "()Ljava/lang/Thread;")
                    .invokeVirtual("java/lang/Thread", "interrupt", "()V")
                .label("label3:")
                    .vreturn()
                .label("label4:")
                    .noMoreCode();
    }
}
