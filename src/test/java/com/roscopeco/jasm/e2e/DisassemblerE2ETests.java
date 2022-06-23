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
        System.out.println(disassemble("EmptyEnum"));
        final var test = doParseString(disassemble("EmptyEnum"));

        assertClass(test)
            .isPublic()
            .isEnum()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyEnum");

        // Not testing the body here, as it differs between Javac versions (11 and 17 do things differently for example)
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

    @Test
    void shouldDisassembleClassWithLookupSwitch() {
        final var test = doParseString(disassemble("LookupSwitchTest"));

        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("(I)V")
            .hasCodeSequence()
                .label("label0:")
                .iload(1)
                .lookupswitch()
                    .withDefault("label1")
                    .withCase(1, "label2")
                    .withCase(10000, "label3")
                    .withCase(2000000, "label4")
                // Not interested in the rest
            ;
    }

    @Test
    void shouldDisassembleClassWithTableSwitch() {
        final var test = doParseString(disassemble("TableSwitchTest"));

        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("(I)V")
            .hasCodeSequence()
                .label("label0:")
                .iload(1)
                .tableswitch()
                    .withDefault("label1")
                    .withCase(1, "label2")
                    .withCase(2, "label3")
                    .withCase(3, "label1")
                    .withCase(4, "label1")
                    .withCase(5, "label4")
                // Not interested in the rest
        ;
    }
}
