/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.MethodContext
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

class MethodAssert internal constructor(actual: MethodContext) :
    AbstractAssert<MethodAssert, MethodContext>(actual, MethodAssert::class.java) {

    fun hasName(expected: String): MethodAssert {
        isNotNull

        if (expected != actual.membername().text) {
            failWithMessage(
                "Expected method to have name '"
                        + expected
                        + "', but was '"
                        + actual.membername().text
                        + "'"
            )
        }

        return this
    }

    fun isVoid(): MethodAssert {
        isNotNull

        if (actual.type().TYPE_VOID() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type V, but is "
                        + actual.type()
            )
        }

        return this
    }

    fun isInteger(): MethodAssert {
        isNotNull

        if (actual.type().TYPE_INT() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type I, but is "
                        + actual.type()
            )
        }

        return this
    }

    fun isReference(expected: String): MethodAssert {
        isNotNull

        val failureMessage = {
            ("Expected method "
                    + actual.membername().text
                    + " to have return type "
                    + expected +
                    ", but is "
                    + (actual.type().text ?: "Unknown")
            )
        }

        val type = actual.type()
        if (type?.ref_type()?.text != expected && type?.ref_array_type()?.text != expected) {
            failWithMessage(failureMessage.invoke())
        }

        return this
    }

    fun hasArgumentCount(expected: Int): MethodAssert {
        isNotNull

        if (actual.argument_type().size != expected) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have "
                        + expected +
                        " arguments, but it has "
                        + (actual.argument_type().size)
            )
        }

        return this
    }

    fun hasNoArguments() = hasArgumentCount(0)

    fun hasArgumentTypes(vararg types: String): MethodAssert {
        isNotNull

        assertThat(actual.argument_type())
            .isNotNull
            .isNotEmpty

        assertThat(actual.argument_type().map {
            if (it.prim_array_type() != null) it.prim_array_type().text else it.text
        })
            .`as`("Method parameter types for ${actual.membername().text}")
            .containsExactly(*types)

        return this
    }


    fun hasCodeSequence() = CodeSequenceAssert(actual.stat_block(), this)
}