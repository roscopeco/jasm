/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.asserts.CodeSequenceAssert;
import lombok.NonNull;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;

class ParserInstructionTests {
    @Test
    void shouldParseAaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aaload.jasm", code -> code
                .aaload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aastore.jasm", code -> code
                .aastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAconstNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/AconstNull.jasm", code -> code
                .aconstNull()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aload.jasm", code -> code
                .aload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Areturn.jasm", code -> code
                .areturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseArraylength() {
        runInstructionTest("com/roscopeco/jasm/insntest/Arraylength.jasm", code -> code
                .arraylength()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Astore.jasm", code -> code
                .astore(1)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseAthrow() {
        runInstructionTest("com/roscopeco/jasm/insntest/Athrow.jasm", code -> code
                .athrow()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseBaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Baload.jasm", code -> code
                .baload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseBastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Bastore.jasm", code -> code
                .bastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseBipush() {
        runInstructionTest("com/roscopeco/jasm/insntest/Bipush.jasm", code -> code
                .bipush(100)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseCaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Caload.jasm", code -> code
                .caload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseCastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Castore.jasm", code -> code
                .castore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseCheckcast() {
        runInstructionTest("com/roscopeco/jasm/insntest/Checkcast.jasm", code -> code
                .checkcast("java/util/ArrayList")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Daload.jasm", code -> code
                .daload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dastore.jasm", code -> code
                .dastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dload.jasm", code -> code
                .dload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dreturn.jasm", code -> code
                .dreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dstore.jasm", code -> code
                .dstore(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDup() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dup.jasm", code -> code
                .dup()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Faload.jasm", code -> code
                .faload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fastore.jasm", code -> code
                .fastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fload.jasm", code -> code
                .fload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Freturn.jasm", code -> code
                .freturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fstore.jasm", code -> code
                .fstore(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseGoto() {
        runInstructionTest("com/roscopeco/jasm/insntest/Goto.jasm", code -> code
                .label("infinity:")
                ._goto("infinity")
                ._goto("unreachable")
                .label("unreachable:")
                .vreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iaload.jasm", code -> code
                .iaload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iastore.jasm", code -> code
                .iastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iconst.jasm", code -> code
                .iconst(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIfs() {
        runInstructionTest("com/roscopeco/jasm/insntest/If.jasm", code -> code
                .ifeq("label")
                .ifge("label")
                .ifgt("label")
                .ifle("label")
                .iflt("label")
                .ifne("label")
                .label("label:")
                .vreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIfAcmps() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfAcmp.jasm", code -> code
                .if_acmpeq("label")
                .if_acmpne("label")
                .label("label:")
                .vreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIfIcmps() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfIcmp.jasm", code -> code
                .if_icmpeq("label")
                .if_icmpge("label")
                .if_icmpgt("label")
                .if_icmple("label")
                .if_icmplt("label")
                .if_icmpne("label")
                .label("label:")
                .vreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIfNullNonNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/IfNullNonNull.jasm", code -> code
                .ifNull("label")
                .ifNonNull("label")
                .label("label:")
                .vreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iload.jasm", code -> code
                .iload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseInvokeDynamic() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeDynamic.jasm", code -> code
                .invokeDynamic("dynamicMethod", "(I)Ljava/lang/Object;")
                // TODO more in-depth test here!
                .noMoreCode()
        );
    }

    @Test
    void shouldParseInvokeInterface() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeInterface.jasm", code -> code
                .invokeInterface("java/util/List", "get", "(I)Ljava/lang/Object;")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseInvokeSpecial() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeSpecial.jasm", code -> code
                .invokeSpecial("java/lang/Object", "<init>", "(Ljava/lang/String;I;Z)V")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseInvokeStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeStatic.jasm", code -> code
                .invokeStatic("java/lang/Thread", "currentThread", "()Ljava/lang/Thread;")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseInvokeVirtual() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeVirtual.jasm", code -> code
                .invokeVirtual("java/lang/String", "trim", "()Ljava/lang/String;")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ireturn.jasm", code -> code
                .ireturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Istore.jasm", code -> code
                .istore(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Laload.jasm", code -> code
                .laload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLastore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lastore.jasm", code -> code
                .lastore()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLdc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ldc.jasm", code -> code
                .ldc(true)
                .ldc(false)
                .ldc(10)
                .ldc(5.0f)
                .ldcStr("Test string")
                .ldcClass("java/util/List")
                .ldcMethodType("(java/util/String;)I")
                .ldcMethodHandle("invokevirtualcom/roscopeco/jasm/Tests.example()V")
                .ldcConstDynamic("constdynamicDYNAMIC_CONSTLjava/lang/Object;{invokeinterfacecom/example/Bootstrap.constsite(Ljava/lang/String;)Ljava/lang/invoke/CallSite;[\"Something else\"]}")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lload.jasm", code -> code
                .lload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lreturn.jasm", code -> code
                .lreturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseLstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lstore.jasm", code -> code
                .lstore(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseNew() {
        runInstructionTest("com/roscopeco/jasm/insntest/New.jasm", code -> code
                .anew("java/util/ArrayList")
                .noMoreCode()
        );
    }

    @Test
    void shouldParseReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Return.jasm", code -> code
                .vreturn()
                .noMoreCode()
        );
    }

    private void runInstructionTest(@NonNull final String testCase, ThrowingConsumer<CodeSequenceAssert> codeAssertions) {
        final var test  = doParse(testCase);

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        final var codeAsserter = assertMember(member)
                .isNotNull()
                .isMethod()
                    .hasName("insnTest")
                    .hasCodeSequence();

        codeAssertions.accept(codeAsserter);
    }
}
