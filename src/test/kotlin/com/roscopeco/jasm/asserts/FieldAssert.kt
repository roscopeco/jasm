/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.FieldContext
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.AssertionsForInterfaceTypes
import org.assertj.core.api.ListAssert

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

        if (actual.type().prim_type()?.TYPE_INT() == null) {
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

        if (actual.type().ref_type() == null && actual.type().array_type() == null) {
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

    fun isPrimitiveArray(typeName: String): FieldAssert {
        isNotNull

        if (actual.type()?.array_type()?.prim_type() == null) {
            failWithMessage(
                "Expected field "
                        + actual.membername().text
                        + " to be of type [<primitive>, but is "
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

    fun hasModifiers(vararg modifiers: String) = modifierAssert { it.containsAll(modifiers.toList()) }

    private fun modifierAssert(assert: (ListAssert<String>) -> Unit): FieldAssert {
        isNotNull

        assert.invoke(
            AssertionsForInterfaceTypes.assertThat(actual.field_modifier().map { it.text })
            .`as`("Modifier list for ${actual.membername().text}"))

        return this
    }
}