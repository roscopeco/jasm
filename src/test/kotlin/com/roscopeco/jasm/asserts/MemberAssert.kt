/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.MemberContext
import org.assertj.core.api.AbstractAssert

class MemberAssert internal constructor(actual: MemberContext) :
    AbstractAssert<MemberAssert, MemberContext>(actual, MemberAssert::class.java) {

    fun isField(): FieldAssert {
        isNotNull

        if (actual!!.field() == null) {
            failWithMessage("Expected member to be a field, but it is not - it is " + actual.text)
        }

        return FieldAssert(actual.field())
    }

    fun isMethod(): MethodAssert {
        isNotNull

        if (actual!!.method() == null) {
            failWithMessage("Expected member to be a method, but it is not - it is " + actual.text)
        }

        return MethodAssert(actual.method())
    }
}