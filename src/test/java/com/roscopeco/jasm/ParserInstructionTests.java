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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;

class ParserInstructionTests {
    static Stream<Arguments> singleInstructionNoOperandsTests() {
        return Stream.of(
            Arguments.of("Aaload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::aaload),
            Arguments.of("Aastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::aastore),
            Arguments.of("AconstNull.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::aconstNull),
            Arguments.of("Areturn.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::areturn),
            Arguments.of("Arraylength.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::arraylength),
            Arguments.of("Athrow.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::athrow),
            Arguments.of("Baload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::baload),
            Arguments.of("Bastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::bastore),
            Arguments.of("Caload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::caload),
            Arguments.of("Castore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::castore),
            Arguments.of("D2f.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::d2f),
            Arguments.of("D2i.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::d2i),
            Arguments.of("D2l.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::d2l),
            Arguments.of("Dadd.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dadd),
            Arguments.of("Daload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::daload),
            Arguments.of("Dastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dastore),
            Arguments.of("Dcmpg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dcmpg),
            Arguments.of("Dcmpl.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dcmpl),
            Arguments.of("Ddiv.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ddiv),
            Arguments.of("Dmul.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dmul),
            Arguments.of("Dneg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dneg),
            Arguments.of("Drem.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::drem),
            Arguments.of("Dreturn.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dreturn),
            Arguments.of("Dsub.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dsub),
            Arguments.of("Dup.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dup),
            Arguments.of("Dup_x1.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dupX1),
            Arguments.of("Dup_x2.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dupX2),
            Arguments.of("Dup2.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dup2),
            Arguments.of("Dup2_x1.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dup2X1),
            Arguments.of("Dup2_x2.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::dup2X2),
            Arguments.of("F2d.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::f2d),
            Arguments.of("F2i.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::f2i),
            Arguments.of("F2l.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::f2l),
            Arguments.of("Fadd.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fadd),
            Arguments.of("Faload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::faload),
            Arguments.of("Fastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fastore),
            Arguments.of("Fcmpg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fcmpg),
            Arguments.of("Fcmpl.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fcmpl),
            Arguments.of("Fdiv.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fdiv),
            Arguments.of("Fmul.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fmul),
            Arguments.of("Fneg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fneg),
            Arguments.of("Frem.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::frem),
            Arguments.of("Freturn.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::freturn),
            Arguments.of("Fsub.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::fsub),
            Arguments.of("I2b.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2b),
            Arguments.of("I2c.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2c),
            Arguments.of("I2d.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2d),
            Arguments.of("I2f.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2f),
            Arguments.of("I2l.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2l),
            Arguments.of("I2s.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::i2s),
            Arguments.of("Iadd.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::iadd),
            Arguments.of("Iaload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::iaload),
            Arguments.of("Iand.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::iand),
            Arguments.of("Iastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::iastore),
            Arguments.of("Idiv.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::idiv),
            Arguments.of("Imul.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::imul),
            Arguments.of("Ineg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ineg),
            Arguments.of("Ior.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ior),
            Arguments.of("Irem.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::irem),
            Arguments.of("Ireturn.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ireturn),
            Arguments.of("Ishl.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ishl),
            Arguments.of("Ishr.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ishr),
            Arguments.of("Isub.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::isub),
            Arguments.of("Iushr.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::iushr),
            Arguments.of("Ixor.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ixor),
            Arguments.of("L2d.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::l2d),
            Arguments.of("L2f.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::l2f),
            Arguments.of("L2i.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::l2i),
            Arguments.of("Ladd.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ladd),
            Arguments.of("Laload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::laload),
            Arguments.of("Land.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::land),
            Arguments.of("Lastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lastore),
            Arguments.of("Lcmp.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lcmp),
            Arguments.of("Ldiv.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::ldiv),
            Arguments.of("Lmul.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lmul),
            Arguments.of("Lneg.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lneg),
            Arguments.of("Lor.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lor),
            Arguments.of("Lrem.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lrem),
            Arguments.of("Lreturn.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lreturn),
            Arguments.of("Lshl.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lshl),
            Arguments.of("Lshr.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lshr),
            Arguments.of("Lsub.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lsub),
            Arguments.of("Lushr.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lushr),
            Arguments.of("Lxor.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::lxor),
            Arguments.of("Monitorenter.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::monitorenter),
            Arguments.of("Monitorexit.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::monitorexit),
            Arguments.of("Nop.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::nop),
            Arguments.of("Pop.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::pop),
            Arguments.of("Pop2.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::pop2),
            Arguments.of("Return.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::vreturn),
            Arguments.of("Saload.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::saload),
            Arguments.of("Sastore.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::sastore),
            Arguments.of("Swap.jasm", (Function<CodeSequenceAssert, CodeSequenceAssert>) CodeSequenceAssert::swap)
        );
    }

    static Stream<Arguments> singleInstructionOneOperandTests() {
        return Stream.of(
            Arguments.of("Aload.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::aload),
            Arguments.of("Astore.jasm", 1, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::astore),
            Arguments.of("Anewarray.jasm", "java/lang/String", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::anewarray),
            Arguments.of("Bipush.jasm", 100, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::bipush),
            Arguments.of("Checkcast.jasm", "java/util/ArrayList", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::checkcast),
            Arguments.of("Dconst.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::dconst),
            Arguments.of("Dload.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::dload),
            Arguments.of("Dstore.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::dstore),
            Arguments.of("Fconst.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::fconst),
            Arguments.of("Fload.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::fload),
            Arguments.of("Fstore.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::fstore),
            Arguments.of("Iconst.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::iconst),
            Arguments.of("Iload.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::iload),
            Arguments.of("Instanceof.jasm", "java/lang/Object", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::instance_of),
            Arguments.of("Istore.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::istore),
            Arguments.of("Jsr.jasm", "label", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::jsr),
            Arguments.of("Lconst.jasm", 1, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::lconst),
            Arguments.of("Lload.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::lload),
            Arguments.of("Lstore.jasm", 0, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::lstore),
            Arguments.of("New.jasm", "java/util/ArrayList", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::anew),
            Arguments.of("Ret.jasm", 1, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::ret),
            Arguments.of("Newarray.jasm", "I", (BiFunction<CodeSequenceAssert, String, CodeSequenceAssert>) CodeSequenceAssert::newarray),
            Arguments.of("Sipush.jasm", 100, (BiFunction<CodeSequenceAssert, Integer, CodeSequenceAssert>) CodeSequenceAssert::sipush)
        );
    }

    @Test
    void shouldParseGetField() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetField.jasm", code -> code
            .getField("com/example/SomeClass", "someField", "Ljava/lang/String;")
            .noMoreCode()
        );
    }

    @Test
    void shouldParseGetStatic() {
        runInstructionTest("com/roscopeco/jasm/insntest/GetStatic.jasm", code -> code
            .getStatic("com/example/SomeClass", "someField", "Ljava/lang/String;")
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
    void shouldParseLookupswitch() {
        runInstructionTest("com/roscopeco/jasm/insntest/Lookupswitch.jasm", code -> code
            .lookupswitch()
                .withDefault("defaultLabel")
                .withCase(1, "oneLabel")
                .withCase(100, "hundredLabel")
                .withCase(1000, "thousandLabel")
                .end()
            .noMoreCode()
        );
    }

    @Test
    void shouldParseMultianewarray() {
        runInstructionTest("com/roscopeco/jasm/insntest/Multianewarray.jasm", code -> code
            .multianewarray("[[java/lang/String")
            .multianewarray("[[java/lang/String", 2)
            .multianewarray("[[java/lang/String", 2)
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
    void shouldParseTableswitch() {
        runInstructionTest("com/roscopeco/jasm/insntest/Tableswitch.jasm", code -> code
            .tableswitch()
                .withDefault("defaultLabel")
                .withCase(1, "oneLabel")
                .withCase(2, "twoLabel")
                .withCase(5, "fiveLabel")
                .end()
            .noMoreCode()
        );
    }

    @ParameterizedTest
    @MethodSource("singleInstructionNoOperandsTests")
    void singleInstructionNoOperandsTests(final String testCase, final Function<CodeSequenceAssert, CodeSequenceAssert> assertFunc) {
        singleInsnNoOperands("com/roscopeco/jasm/insntest/" + testCase, assertFunc);
    }

    @ParameterizedTest
    @MethodSource("singleInstructionOneOperandTests")
    <T> void singleInstructionOneOperandTests(
        final String testCase, T operand,
        final BiFunction<CodeSequenceAssert, T, CodeSequenceAssert> assertFunc
    ) {
        singleInsnOneOperand("com/roscopeco/jasm/insntest/" + testCase, operand, assertFunc);
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

        assertThat(test.classbody().member()).hasSize(1);
        final var member = test.classbody().member(0);

        final var codeAsserter = assertMember(member)
                .isNotNull()
                .isMethod()
                    .hasName("insnTest")
                    .hasCodeSequence();

        codeAssertions.accept(codeAsserter);
    }
}
