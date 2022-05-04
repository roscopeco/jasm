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

import java.util.function.BiFunction;
import java.util.function.Function;

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
    void shouldParseD2f() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2f.jasm", code -> code
            .d2f()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseD2i() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2i.jasm", code -> code
            .d2i()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseD2l() {
        runInstructionTest("com/roscopeco/jasm/insntest/D2l.jasm", code -> code
            .d2l()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseDadd() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dadd.jasm", code -> code
            .dadd()
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
    void shouldParseDcmpg() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dcmpg.jasm", code -> code
            .dcmpg()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseDcmpl() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dcmpl.jasm", code -> code
            .dcmpl()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseDconst() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dconst.jasm", code -> code
            .dconst(0)
            .noMoreCode()
        );
    }

    @Test
    void shouldParseDdiv() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ddiv.jasm", CodeSequenceAssert::ddiv);
    }

    @Test
    void shouldParseDload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Dload.jasm", code -> code
                .dload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseDmul() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dmul.jasm", CodeSequenceAssert::dmul);
    }

    @Test
    void shouldParseDneg() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dneg.jasm", CodeSequenceAssert::dneg);
    }

    @Test
    void shouldParseDrem() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Drem.jasm", CodeSequenceAssert::drem);
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
    void shouldParseDsub() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dsub.jasm", CodeSequenceAssert::dsub);
    }

    @Test
    void shouldParseDup() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup.jasm", CodeSequenceAssert::dup);
    }

    @Test
    void shouldParseDup_x1() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup_x1.jasm", CodeSequenceAssert::dupX1);
    }

    @Test
    void shouldParseDup_x2() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup_x2.jasm", CodeSequenceAssert::dupX2);
    }

    @Test
    void shouldParseDup2() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup2.jasm", CodeSequenceAssert::dup2);
    }

    @Test
    void shouldParseDup2_x1() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup2_x1.jasm", CodeSequenceAssert::dup2X1);
    }

    @Test
    void shouldParseDup2_x2() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Dup2_x2.jasm", CodeSequenceAssert::dup2X2);
    }

    @Test
    void shouldParseF2d() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/F2d.jasm", CodeSequenceAssert::f2d);
    }

    @Test
    void shouldParseF2i() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/F2i.jasm", CodeSequenceAssert::f2i);
    }

    @Test
    void shouldParseF2l() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/F2l.jasm", CodeSequenceAssert::f2l);
    }

    @Test
    void shouldParseFadd() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fadd.jasm", CodeSequenceAssert::fadd);
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
    void shouldParseFcmpg() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fcmpg.jasm", CodeSequenceAssert::fcmpg);
    }

    @Test
    void shouldParseFcmpl() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fcmpl.jasm", CodeSequenceAssert::fcmpl);
    }

    @Test
    void shouldParseFconst() {
        singleInsnOneOperand("com/roscopeco/jasm/insntest/Fconst.jasm", 0, CodeSequenceAssert::fconst);
    }

    @Test
    void shouldParseFdiv() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fdiv.jasm", CodeSequenceAssert::fdiv);
    }

    @Test
    void shouldParseFload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Fload.jasm", code -> code
                .fload(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseFmul() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fmul.jasm", CodeSequenceAssert::fmul);
    }

    @Test
    void shouldParseFneg() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fneg.jasm", CodeSequenceAssert::fneg);
    }

    @Test
    void shouldParseFrem() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Frem.jasm", CodeSequenceAssert::frem);
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
    void shouldParseFsub() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Fsub.jasm", CodeSequenceAssert::fsub);
    }

    @Test
    void shouldParseGetField() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetField.jasm", code -> code
            .getField("com/example/SomeClass", "someField", "Ljava/lang/String;")
        );
    }

    @Test
    void shouldParseGetStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetStatic.jasm", code -> code
            .getStatic("com/example/SomeClass", "someField", "Ljava/lang/String;")
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
    void shouldParseI2b() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2b.jasm", CodeSequenceAssert::i2b);
    }

    @Test
    void shouldParseI2c() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2c.jasm", CodeSequenceAssert::i2c);
    }

    @Test
    void shouldParseI2d() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2d.jasm", CodeSequenceAssert::i2d);
    }

    @Test
    void shouldParseI2f() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2f.jasm", CodeSequenceAssert::i2f);
    }

    @Test
    void shouldParseI2l() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2l.jasm", CodeSequenceAssert::i2l);
    }

    @Test
    void shouldParseI2s() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/I2s.jasm", CodeSequenceAssert::i2s);
    }

    @Test
    void shouldParseIadd() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Iadd.jasm", CodeSequenceAssert::iadd);
    }

    @Test
    void shouldParseIaload() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iaload.jasm", code -> code
                .iaload()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIand() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Iand.jasm", CodeSequenceAssert::iand);
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
    void shouldParseIdiv() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Idiv.jasm", CodeSequenceAssert::idiv);
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
    void shouldParseIinc() {
        runInstructionTest("com/roscopeco/jasm/insntest/Iinc.jasm", code -> code
            .iinc(3, 42)    // This appears twice to test both forms parse properly
            .iinc(3, 42)
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
    void shouldParseImul() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Imul.jasm", CodeSequenceAssert::imul);
    }

    @Test
    void shouldParseIneg() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ineg.jasm", CodeSequenceAssert::ineg);
    }

    @Test
    void shouldParseInstanceof() {
        singleInsnOneOperand(
            "com/roscopeco/jasm/insntest/Instanceof.jasm",
            "java/lang/Object",
            CodeSequenceAssert::instance_of
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
                .invokeSpecial("java/lang/Object", "<init>", "(Ljava/lang/String;IZ)V")
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
    void shouldParseIor() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ior.jasm", CodeSequenceAssert::ior);
    }

    @Test
    void shouldParseIrem() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Irem.jasm", CodeSequenceAssert::irem);
    }

    @Test
    void shouldParseIreturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Ireturn.jasm", code -> code
                .ireturn()
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIshl() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ishl.jasm", CodeSequenceAssert::ishl);
    }

    @Test
    void shouldParseIshr() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ishr.jasm", CodeSequenceAssert::ishr);
    }

    @Test
    void shouldParseIstore() {
        runInstructionTest("com/roscopeco/jasm/insntest/Istore.jasm", code -> code
                .istore(0)
                .noMoreCode()
        );
    }

    @Test
    void shouldParseIsub() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Isub.jasm", CodeSequenceAssert::isub);
    }

    @Test
    void shouldParseIushr() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Iushr.jasm", CodeSequenceAssert::iushr);
    }

    @Test
    void shouldParseIxor() {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/Ixor.jasm", CodeSequenceAssert::ixor);
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
                .ldcMethodType("(java/util/String)I")
                .ldcMethodHandle("invokevirtualcom/roscopeco/jasm/Tests.example()V")
                .ldcConstDynamic("constdynamicDYNAMIC_CONSTjava/lang/Object{invokeinterfacecom/example/Bootstrap.constsite(java/lang/String)java/lang/invoke/CallSite[\"Something else\"]}")
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
    void shouldParsePutField() {
        runInstructionTest("com/roscopeco/jasm/insntest/PutField.jasm", code -> code
            .putField("com/example/SomeClass", "someField", "Ljava/lang/String;")
        );
    }

    @Test
    void shouldParsePutStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/PutStatic.jasm", code -> code
            .putStatic("com/example/SomeClass", "someField", "Ljava/lang/String;")
        );
    }

    @Test
    void shouldParseReturn() {
        runInstructionTest("com/roscopeco/jasm/insntest/Return.jasm", code -> code
                .vreturn()
                .noMoreCode()
        );
    }

    void singleInsnNoOperands(final String testCase, final Function<CodeSequenceAssert, CodeSequenceAssert> assertFunc) {
        runInstructionTest(testCase, code -> assertFunc.apply(code).noMoreCode());
    }

    <T> void singleInsnOneOperand(
        final String testCase,
        T operand,
        final BiFunction<CodeSequenceAssert, T, CodeSequenceAssert> assertFunc
    ) {
        runInstructionTest(testCase, code -> assertFunc.apply(code, operand).noMoreCode());
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
