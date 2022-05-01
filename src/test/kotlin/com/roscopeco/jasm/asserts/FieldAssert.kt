/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.FieldContext
import org.assertj.core.api.AbstractAssert

class FieldAssert internal constructor(actual: FieldContext) :
    AbstractAssert<FieldAssert, FieldContext>(actual, FieldAssert::class.java) {

    fun hasName(expected: String): FieldAssert {
        isNotNull

        if (expected != actual.membername().text) {
            failWithMessage(
                "Expected field to have name '"
                        + expected
                        + "', but was '"
                        + actual.membername().text
                        + "'"
            )
        }

        return this
    }

    fun isInteger(): FieldAssert {
        isNotNull

        if (actual.type().TYPE_INT() == null) {
            failWithMessage(
                "Expected field "
                        + actual.membername().text
                        + " to be of type I, but is "
                        + actual.type().text
            )
        }

        return this
    }

    fun isReference(typeName: String): FieldAssert {
        isNotNull

        if (actual.type().QNAME() == null) {
            failWithMessage(
                "Expected field "
                        + actual.membername().text
                        + " to be of type L, but is "
                        + actual.type().text
            )
        }
        if (typeName != actual.type().text) {
            failWithMessage(
                "Expected field "
                        + actual.membername().text
                        + " to be of type "
                        + typeName +
                        ", but is "
                        + actual.type().text
            )
        }

        return this
    }
}