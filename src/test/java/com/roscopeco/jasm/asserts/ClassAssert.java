/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.assertj.core.api.AbstractAssert;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    public ClassAssert hasSuperclass(@NonNull final String qname) {
        isNotNull();

        if (!qname.equals(actual.extends_().QNAME().getText())) {
            failWithMessage("Expected class to extend '"
                    + qname
                    + "' but was '"
                    + Optional.ofNullable(actual.extends_()).map(RuleContext::getText).orElse("java/lang/Object")
                    + "'");

        }

        return this;
    }
    public ClassAssert hasInterfaces(@NonNull final String... qnames) {
        isNotNull();

        assertThat(actual.implements_().QNAME().stream().map(ParseTree::getText)).containsExactly(qnames);

        return this;
    }
}
