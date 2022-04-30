/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.RuleContext;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeSequenceAssert extends AbstractAssert<CodeSequenceAssert, JasmParser.Stat_blockContext> {
    final MethodAssert caller;
    int pc;

    CodeSequenceAssert(final JasmParser.Stat_blockContext actual, @NonNull final MethodAssert caller) {
        super(actual, CodeSequenceAssert.class);
        this.caller = caller;
        this.pc = 0;
    }

    public MethodAssert noMoreCode() {
        if (this.pc != actual.stat().size()) {
            failWithMessage("Expected end of code reached at "
                    + this.pc
                    + ", but found "
                    + actual.stat().get(pc).getText()
                    + " instead"
            );
        }

        return this.caller;
    }

    public CodeSequenceAssert aconstNull() {
        return genericNoOperandCheck("aconstnull", JasmParser.InstructionContext::insn_aconstnull);
    }

    public CodeSequenceAssert aload(final int expected) {
        return genericIntOperandCheck("aload", expected,
                JasmParser.InstructionContext::insn_aload,
                (aload) -> aload.atom().getText()
        );
    }

    public CodeSequenceAssert areturn() {
        return genericNoOperandCheck("areturn", JasmParser.InstructionContext::insn_areturn);
    }

    public CodeSequenceAssert freturn() {
        return genericNoOperandCheck("freturn", JasmParser.InstructionContext::insn_freturn);
    }

    public CodeSequenceAssert iconst(final int expected) {
        return genericIntOperandCheck("iconst", expected,
                JasmParser.InstructionContext::insn_iconst,
                (iconst) -> iconst.atom().getText()
        );
    }

    public CodeSequenceAssert invokeInterface(
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor
    ) {
        return assertInvoke(
                "invokeinterface",
                owner,
                name,
                descriptor,
                JasmParser.InstructionContext::insn_invokeinterface,
                JasmParser.Insn_invokeinterfaceContext::owner,
                JasmParser.Insn_invokeinterfaceContext::membername,
                JasmParser.Insn_invokeinterfaceContext::method_descriptor
        );
    }

    public CodeSequenceAssert invokeSpecial(
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor
    ) {
        return assertInvoke(
                "invokespecial",
                owner,
                name,
                descriptor,
                JasmParser.InstructionContext::insn_invokespecial,
                JasmParser.Insn_invokespecialContext::owner,
                JasmParser.Insn_invokespecialContext::membername,
                JasmParser.Insn_invokespecialContext::method_descriptor
        );
    }

    public CodeSequenceAssert invokeStatic(
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor
    ) {
        return assertInvoke(
                "invokestatic",
                owner,
                name,
                descriptor,
                JasmParser.InstructionContext::insn_invokestatic,
                JasmParser.Insn_invokestaticContext::owner,
                JasmParser.Insn_invokestaticContext::membername,
                JasmParser.Insn_invokestaticContext::method_descriptor
        );
    }

    public CodeSequenceAssert invokeVirtual(
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor
    ) {
        return assertInvoke(
                "invokevirtual",
                owner,
                name,
                descriptor,
                JasmParser.InstructionContext::insn_invokevirtual,
                JasmParser.Insn_invokevirtualContext::owner,
                JasmParser.Insn_invokevirtualContext::membername,
                JasmParser.Insn_invokevirtualContext::method_descriptor
        );
    }

    private <T> CodeSequenceAssert assertInvoke(
            @NonNull final String invokeType,
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor,
            @NonNull final Function<JasmParser.InstructionContext, T> invokeExtractor,
            @NonNull final Function<T, JasmParser.OwnerContext> ownerExtractor,
            @NonNull final Function<T, JasmParser.MembernameContext> membernameExtractor,
            @NonNull final Function<T, JasmParser.Method_descriptorContext> descriptorExtractor
    ) {
        final Function<JasmParser.InstructionContext, String> failMessageSupplier = (insn) ->
                "Expected "
                    + invokeType
                    + owner + "." + name + descriptor
                    + " instruction at pc("
                    + this.pc
                    + "), but was "
                    + insn.getText();

        isNotNull();
        assertThat(actual.stat()).isNotNull();
        hasNotUnderflowed(invokeType);

        final var stat = actual.stat().get(pc);
        if (stat.instruction() == null || invokeExtractor.apply(stat.instruction()) == null) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()));
        }

        final var insn = invokeExtractor.apply(stat.instruction());

        if (!owner.equals(ownerExtractor.apply(insn).getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + "\n"
                    + "    <owner mismatch: '"
                    + owner + "' vs '" + ownerExtractor.apply(insn).getText() + "'>");
        }
        if (!name.equals(membernameExtractor.apply(insn).getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + "\n"
                    + "    <name mismatch: '"
                    + name + "' vs '" + membernameExtractor.apply(insn).getText() + "'>");
        }
        if (!descriptor.equals(descriptorExtractor.apply(insn).getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + "\n"
                    + "    <descriptor mismatch'"
                    + descriptor + "' vs '" + descriptorExtractor.apply(insn).getText() + "'>");
        }

        this.pc++;
        return this;
    }

    public CodeSequenceAssert ireturn() {
        return genericNoOperandCheck("ireturn", JasmParser.InstructionContext::insn_ireturn);
    }

    public CodeSequenceAssert ldc(final int expected) {
        return genericLdcCheck("" + expected, atom -> expected == Integer.parseInt(atom.getText()));
    }

    public CodeSequenceAssert ldc(final float expected) {
        return genericLdcCheck("" + expected, atom -> expected == Float.parseFloat(atom.getText()));
    }

    public CodeSequenceAssert ldc(@NonNull final String expected) {
        return genericLdcCheck('"' + expected + '"', atom -> expected.equals(cleanConstantString(atom.getText())));
    }

    public CodeSequenceAssert vreturn() {
        return genericNoOperandCheck("vreturn", JasmParser.InstructionContext::insn_return);
    }

    private String cleanConstantString(@NonNull final String constant) {
        return constant.substring(1, constant.length() - 1);
    }

    private void hasNotUnderflowed(@NonNull final String expected) {
        if (this.pc >= actual.stat().size()) {
            failWithMessage("Code underflowed at pc ("
                + this.pc
                + "): expected " + expected + " but end of code found instead");
        }
    }

    private CodeSequenceAssert genericNoOperandCheck(
            @NonNull final String name,
            @NonNull final Function<JasmParser.InstructionContext, Object> getInsnFunc
    ) {
        isNotNull();
        assertThat(actual.stat()).isNotNull();
        hasNotUnderflowed(name);

        final var stat = actual.stat().get(pc);
        if (stat.instruction() == null || getInsnFunc.apply(stat.instruction()) == null) {
            failWithMessage("Expected "
                    + name
                    + " instruction at pc("
                    + this.pc
                    + "), but was "
                    + stat.instruction().getText()
            );
        }

        this.pc++;
        return this;
    }

    private <T> CodeSequenceAssert genericIntOperandCheck(
            @NonNull final String name,
            final int expectedOperand,
            @NonNull final Function<JasmParser.InstructionContext, T> getInsnFunc,
            @NonNull final Function<T, String> getAtomTextFunc
    ) {
        isNotNull();
        assertThat(actual.stat()).isNotNull();
        hasNotUnderflowed(name);

        final var stat = actual.stat().get(pc);
        if (stat.instruction() == null
                || getInsnFunc.apply(stat.instruction()) == null
                || !Integer.toString(expectedOperand).equals(getAtomTextFunc.apply(getInsnFunc.apply(stat.instruction())))
        ) {
            failWithMessage("Expected "
                    + name
                    + " "
                    + expectedOperand
                    + " instruction at pc("
                    + this.pc
                    + "), but was "
                    + stat.instruction().getText()
            );
        }

        this.pc++;
        return this;
    }

    private CodeSequenceAssert genericLdcCheck(
            @NonNull final String expectedStr,
            @NonNull final Predicate<JasmParser.AtomContext> atomPredicate
    ) {
        isNotNull();
        assertThat(actual.stat()).isNotNull();
        hasNotUnderflowed("ldc");

        final var insn = actual.stat().get(this.pc).instruction();
        if (
                insn == null
                        || insn.insn_ldc() == null
                        || insn.insn_ldc().atom() == null
                        || !atomPredicate.test(insn.insn_ldc().atom())
        ) {
            failWithMessage("Expected ldc("
                    + expectedStr
                    + ") instruction at pc("
                    + this.pc
                    + "), but was "
                    + Optional.ofNullable(insn)
                    .flatMap(i -> Optional.ofNullable(i.insn_ldc()))
                    .map(RuleContext::getText)
                    .orElse("<Unknown>")
            );
        }

        this.pc++;
        return this;
    }
}
