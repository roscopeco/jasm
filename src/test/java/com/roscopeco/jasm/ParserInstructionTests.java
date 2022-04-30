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
    void handlesAconstNull() {
        runInstructionTest("com/roscopeco/jasm/insntest/AconstNull.jasm", code -> code
                .aconstNull()
                .noMoreCode()
        );
    }

    @Test
    void handlesAload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Aload.jasm", code -> code
                .aload(0)
                .noMoreCode()
        );
    }

    @Test
    void handlesAreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Areturn.jasm", code -> code
                .areturn()
                .noMoreCode()
        );
    }

    @Test
    void handlesDup() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dup.jasm", code -> code
                .dup()
                .noMoreCode()
        );
    }

    @Test
    void handlesFreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Freturn.jasm", code -> code
                .freturn()
                .noMoreCode()
        );
    }

    @Test
    void handlesGoto() {
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
    void handlesIconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iconst.jasm", code -> code
                .iconst(0)
                .noMoreCode()
        );
    }

    @Test
    void handlesInvokeInterface() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeInterface.jasm", code -> code
                .invokeInterface("java/util/List", "get", "(I)Ljava/lang/Object;")
                .noMoreCode()
        );
    }

    @Test
    void handlesInvokeSpecial() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeSpecial.jasm", code -> code
                .invokeSpecial("java/lang/Object", "<init>", "(Ljava/lang/String;I;Z)V")
                .noMoreCode()
        );
    }

    @Test
    void handlesInvokeStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeStatic.jasm", code -> code
                .invokeStatic("java/lang/Thread", "currentThread", "()Ljava/lang/Thread;")
                .noMoreCode()
        );
    }

    @Test
    void handlesInvokeVirtual() {
        runInstructionTest("com/roscopeco/jasm/insntest/InvokeVirtual.jasm", code -> code
                .invokeVirtual("java/lang/String", "trim", "()Ljava/lang/String;")
                .noMoreCode()
        );
    }

    @Test
    void handlesIreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ireturn.jasm", code -> code
                .ireturn()
                .noMoreCode()
        );
    }

    @Test
    void handlesLdc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ldc.jasm", code -> code
                .ldc(10)
                .ldc(5.0f)
                .ldc("Test string")
                .noMoreCode()
        );
    }

    @Test
    void handlesNew() {
        runInstructionTest("com/roscopeco/jasm/insntest/New.jasm", code -> code
                .anew("java/util/ArrayList")
                .noMoreCode()
        );
    }

    @Test
    void handlesReturn() {
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
