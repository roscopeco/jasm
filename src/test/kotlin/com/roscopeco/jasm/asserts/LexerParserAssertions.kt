/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.ClassContext
import com.roscopeco.jasm.antlr.JasmLexer
import com.roscopeco.jasm.antlr.JasmParser.MemberContext
import org.antlr.v4.runtime.Token
import org.junit.jupiter.api.function.ThrowingConsumer

object LexerParserAssertions {
    @JvmStatic
    fun assertClass(actual: ClassContext) =ClassAssert(actual)

    @JvmStatic
    fun assertToken(actual: Token) = TokenAssert(actual)

    @JvmStatic
    fun assertNextToken(lexer: JasmLexer) = assertToken(lexer.nextToken())

    @JvmStatic
    fun assertTokens(lexer: JasmLexer, asserter: ThrowingConsumer<TokenChainAsserter>) {
        asserter.accept(object : TokenChainAsserter {
            override fun next(): TokenAssert = TokenAssert(lexer.nextToken())
        })
    }

    @JvmStatic
    fun assertMember(actual: MemberContext)= MemberAssert(actual)

    interface TokenChainAsserter {
        operator fun next(): TokenAssert
    }
}