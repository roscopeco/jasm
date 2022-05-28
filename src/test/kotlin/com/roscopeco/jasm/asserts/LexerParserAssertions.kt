/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmLexer
import com.roscopeco.jasm.antlr.JasmParser
import org.antlr.v4.runtime.Token
import org.junit.jupiter.api.function.ThrowingConsumer

object LexerParserAssertions {
    @JvmStatic
    fun assertClass(actual: JasmParser.ClassContext) =ClassAssert(actual)

    @JvmStatic
    fun assertToken(actual: Token) = TokenAssert(actual)

    @JvmStatic
    fun assertNextToken(lexer: JasmLexer) = assertToken(nextNonSpaceToken(lexer))

    private fun nextNonSpaceToken(lexer: JasmLexer): Token {
        var token = lexer.nextToken()
        while (token.type == JasmLexer.SPACE || token.type == JasmLexer.COMMENT) token = lexer.nextToken()
        return token
    }

    @JvmStatic
    fun assertTokens(lexer: JasmLexer, asserter: ThrowingConsumer<TokenChainAsserter>) {
        asserter.accept(object : TokenChainAsserter {
            override fun next(): TokenAssert = TokenAssert(nextNonSpaceToken(lexer))
        })
    }

    @JvmStatic
    fun assertMember(actual: JasmParser.MemberContext)= MemberAssert(actual)

    interface TokenChainAsserter {
        operator fun next(): TokenAssert
    }
}