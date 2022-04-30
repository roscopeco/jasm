/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.assertj.core.api.AbstractAssert;

public final class ClassAssert extends AbstractAssert<ClassAssert, JasmParser.ClassContext> {
    ClassAssert(final JasmParser.ClassContext actual) {
        super(actual, ClassAssert.class);
    }

    public ClassAssert hasName(@NonNull final String name) {
        isNotNull();
        if (!name.equals(actual.classname().getText())) {
            failWithMessage("Expected class to have name '" + name + "' but was '" + actual.classname().getText() + "'");
        }
        return this;
    }
}
