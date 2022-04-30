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

    public CodeSequenceAssert invokeSpecial(
            @NonNull final String owner,
            @NonNull final String name,
            @NonNull final String descriptor
    ) {
        final Function<JasmParser.InstructionContext, String> failMessageSupplier = (insn) ->
                "Expected invokespecial("
                + owner + "." + name + descriptor
                + " instruction at pc("
                + this.pc
                + "), but was "
                + insn.getText();

        isNotNull();
        assertThat(actual.stat()).isNotNull();
        hasNotUnderflowed("invokespecial");

        final var stat = actual.stat().get(pc);
        if (stat.instruction() == null || stat.instruction().insn_invokespecial() == null) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()));
        }

        final var insn = stat.instruction().insn_invokespecial();

        if (!owner.equals(insn.owner().getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + " <owner mismatch>");
        }
        if (!name.equals(insn.membername().getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + " <name mismatch>");
        }
        if (!descriptor.equals(insn.method_descriptor().getText())) {
            failWithMessage(failMessageSupplier.apply(stat.instruction()) + " <descriptor mismatch>");
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
