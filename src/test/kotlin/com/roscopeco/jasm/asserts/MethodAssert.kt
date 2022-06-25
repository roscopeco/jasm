/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import TestErrorCollector
import com.roscopeco.jasm.TypeVisitor
import com.roscopeco.jasm.antlr.JasmParser.MethodContext
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForInterfaceTypes
import org.assertj.core.api.ListAssert

class MethodAssert internal constructor(actual: MethodContext) :
    AbstractAssert<MethodAssert, MethodContext>(actual, MethodAssert::class.java) {

    private val errorCollector = TestErrorCollector()

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

        if (actual.method_descriptor()?.type()?.void_type()?.TYPE_VOID() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type V, but is "
                        + (actual.method_descriptor()?.type()?.text ?: "unknown")
            )
        }

        return this
    }

    fun isInteger(): MethodAssert {
        isNotNull

        if (actual.method_descriptor()?.type()?.prim_type()?.TYPE_INT() == null) {
            failWithMessage(
                "Expected method "
                        + actual.membername().text
                        + " to have return type I, but is "
                        + (actual.method_descriptor()?.type() ?: "unknown")
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
                    + (actual.method_descriptor()?.type()?.text ?: "Unknown")
            )
        }

        val type = actual.method_descriptor()?.type()
        if (type?.ref_type()?.text != expected && type?.array_type()?.ref_type()?.text != expected) {
            failWithMessage(failureMessage.invoke())
        }

        return this
    }

    fun hasDescriptor(types: String): MethodAssert {
        isNotNull

        assertThat(actual.method_descriptor()?.method_arguments()?.method_argument())
            .isNotNull

        assertThat(TypeVisitor("<testcase>", errorCollector).visitMethod_descriptor(actual.method_descriptor()))
            .`as`("Method parameter types for ${actual.membername().text}")
            .isEqualTo(types)

        return this
    }

    fun hasCodeSequence() = CodeSequenceAssert(actual.stat_block(), this)

    fun isPublic() = modifierAssert { it.contains("public") }

    fun isPrivate() = modifierAssert { it.contains("private") }

    fun isNotPublic() = modifierAssert { it.doesNotContain("public") }

    fun isAbstract() = modifierAssert { it.contains("abstract") }

    fun isStatic() = modifierAssert { it.contains("static") }

    private fun modifierAssert(assert: (ListAssert<String>) -> Unit): MethodAssert {
        isNotNull

        assert.invoke(AssertionsForInterfaceTypes.assertThat(actual.method_modifier().map { it.text })
            .`as`("Modifier list for ${actual.membername().text}"))

        return this
    }
}