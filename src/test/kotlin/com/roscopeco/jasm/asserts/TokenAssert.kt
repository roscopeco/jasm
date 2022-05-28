/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import org.assertj.core.api.AbstractAssert
import org.antlr.v4.runtime.Token
import com.roscopeco.jasm.antlr.JasmLexer

class TokenAssert internal constructor(actual: Token) :
    AbstractAssert<TokenAssert, Token>(actual, TokenAssert::class.java) {

    fun hasText(expected: String): TokenAssert {
        isNotNull

        if (expected != actual.text) {
            failWithMessage(
                "Expected token to have text '"
                        + expected
                        + "' but was '"
                        + actual.text
                        + "'"
            )
        }

        return this
    }

    fun hasType(expected: Int): TokenAssert {
        isNotNull

        if (actual.type != expected) {
            failWithMessage(
                "Expected token to have type "
                        + JasmLexer.VOCABULARY.getSymbolicName(expected)
                        + " but was "
                        + JasmLexer.VOCABULARY.getSymbolicName(actual.type) + "[" + actual.text + "]"
            )
        }

        return this
    }
}