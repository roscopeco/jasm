/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.assertj.core.api.AbstractAssert;

public class FieldAssert extends AbstractAssert<FieldAssert, JasmParser.FieldContext> {
    FieldAssert(final JasmParser.FieldContext actual) {
        super(actual, FieldAssert.class);
    }

    public FieldAssert hasName(@NonNull String expected) {
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

    public FieldAssert isInteger() {
        isNotNull();

        if (actual.type().TYPE_INT() == null) {
            failWithMessage("Expected field "
                    + actual.membername().getText()
                    + " to be of type I, but is "
                    + actual.type().getText()
            );
        }

        return this;
    }

    public FieldAssert isReference(@NonNull final String typeName) {
        isNotNull();

        if (actual.type().QNAME() == null) {
            failWithMessage("Expected field "
                    + actual.membername().getText()
                    + " to be of type L, but is "
                    + actual.type().getText()
            );
        }

        if (!typeName.equals(actual.type().getText())) {
            failWithMessage("Expected field "
                    + actual.membername().getText()
                    + " to be of type "
                    + typeName +
                    ", but is "
                    + actual.type().getText()
            );
        }

        return this;
    }
}
