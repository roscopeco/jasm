/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import java.util.function.Supplier;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.assertj.core.api.AbstractAssert;

public class MethodAssert extends AbstractAssert<MethodAssert, JasmParser.MethodContext> {
    MethodAssert(final JasmParser.MethodContext actual) {
        super(actual, MethodAssert.class);
    }

    public MethodAssert hasName(@NonNull String expected) {
        isNotNull();

        if (!expected.equals(actual.membername().getText())) {
            failWithMessage("Expected field to have name '"
                    + expected
                    + "', but was '"
                    + actual.membername().getText()
                    + "'"
            );
        }

        return this;
    }

    public MethodAssert isVoid() {
        isNotNull();

        if (actual.type().size() == 0 || actual.type().get(0).TYPE_VOID() == null) {
            failWithMessage("Expected method "
                    + actual.membername().getText()
                    + " to have return type V, but is "
                    + actual.type().get(0)
            );
        }

        return this;
    }

    public MethodAssert isInteger() {
        isNotNull();

        if (actual.type().size() == 0 || actual.type().get(0).TYPE_INT() == null) {
            failWithMessage("Expected method "
                    + actual.membername().getText()
                    + " to have return type I, but is "
                    + actual.type().get(0)
            );
        }

        return this;
    }

    public MethodAssert isReference(@NonNull final String expected) {
        isNotNull();

        Supplier<String> failureMessage = () -> "Expected method "
                + actual.membername().getText()
                + " to have return type "
                + expected +
                ", but is "
                + actual.type().get(0).getText();

        if (actual.type().size() == 0 || actual.type().get(0).QNAME() == null) {
            failWithMessage(failureMessage.get());
        }

        if (!expected.equals(actual.type().get(0).getText())) {
            failWithMessage(failureMessage.get());
        }

        return this;
    }

    public MethodAssert hasArgumentCount(final int expected) {
        isNotNull();

        if (actual.type().size() != expected + 1) {
            failWithMessage("Expected method "
                    + actual.membername().getText()
                    + " to have "
                    + expected +
                    " arguments, but it has "
                    + (actual.type().size() - 1)
            );
        }

        return this;
    }

    public MethodAssert hasNoArguments() {
        return hasArgumentCount(0);
    }

    public CodeSequenceAssert hasCodeSequence() {
        return new CodeSequenceAssert(actual.stat_block(), this);
    }
}
