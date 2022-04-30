/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmLexer;
import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.Token;
import org.assertj.core.api.ThrowingConsumer;

public class LexerParserAssertions {
    public static ClassAssert assertClass(@NonNull final JasmParser.ClassContext actual) {
        return new ClassAssert(actual);
    }

    public static TokenAssert assertToken(@NonNull final Token actual) {
        return new TokenAssert(actual);
    }

    public static TokenAssert assertNextToken(@NonNull final JasmLexer lexer) {
        return assertToken(lexer.nextToken());
    }

    public static void assertTokens(
            @NonNull final JasmLexer lexer,
            @NonNull final ThrowingConsumer<TokenChainAsserter> asserter
    ) {
        asserter.accept(() -> new TokenAssert(lexer.nextToken()));
    }

    public static MemberAssert assertMember(@NonNull final JasmParser.MemberContext actual) {
        return new MemberAssert(actual);
    }

    public interface TokenChainAsserter {
        TokenAssert next();
    }
}

