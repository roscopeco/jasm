/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.MethodContext
import org.assertj.core.api.AbstractAssert

class MethodAssert internal constructor(actual: MethodContext) :
    AbstractAssert<MethodAssert, MethodContext>(actual, MethodAssert::class.java) {

    fun hasName(expected: String): MethodAssert {
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

    fun isVoid(): MethodAssert {
        isNotNull

        if (actual.type().size == 0 || actual.type()[0].TYPE_VOID() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type V, but is "
                        + actual.type()[0]
            )
        }

        return this
    }

    fun isInteger(): MethodAssert {
        isNotNull

        if (actual.type().size == 0 || actual.type()[0].TYPE_INT() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type I, but is "
                        + actual.type()[0]
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
                    + (actual.type().last()?.text ?: "Unknown")
            )
        }

        if (actual.type().last()?.QNAME()?.text != expected) {
            failWithMessage(failureMessage.invoke())
        }

        return this
    }

    fun hasArgumentCount(expected: Int): MethodAssert {
        isNotNull

        if (actual.type().size != expected + 1) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have "
                        + expected +
                        " arguments, but it has "
                        + (actual.type().size - 1)
            )
        }

        return this
    }

    fun hasNoArguments() = hasArgumentCount(0)

    fun hasCodeSequence() = CodeSequenceAssert(actual.stat_block(), this)
}