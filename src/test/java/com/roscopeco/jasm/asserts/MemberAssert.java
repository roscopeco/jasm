/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmParser;
import org.assertj.core.api.AbstractAssert;

public class MemberAssert extends AbstractAssert<MemberAssert, JasmParser.MemberContext> {
    MemberAssert(final JasmParser.MemberContext actual) {
        super(actual, MemberAssert.class);
    }

    public FieldAssert isField() {
        isNotNull();

        if (actual.field() == null) {
            failWithMessage("Expected member to be a field, but it is not - it is " + actual.getText());
        }

        return new FieldAssert(actual.field());
    }

    public MethodAssert isMethod() {
        isNotNull();

        if (actual.method() == null) {
            failWithMessage("Expected member to be a method, but it is not - it is " + actual.getText());
        }

        return new MethodAssert(actual.method());
    }
}
