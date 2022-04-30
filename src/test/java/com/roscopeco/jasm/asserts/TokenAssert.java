/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts;

import com.roscopeco.jasm.antlr.JasmLexer;
import lombok.NonNull;
import org.antlr.v4.runtime.Token;
import org.assertj.core.api.AbstractAssert;

public final class TokenAssert extends AbstractAssert<TokenAssert, Token> {
    TokenAssert(final Token actual) {
        super(actual, TokenAssert.class);
    }

    public TokenAssert hasText(@NonNull final String expected) {
        isNotNull();

        if (!expected.equals(actual.getText())) {
            failWithMessage("Expected token to have text '"
                    + expected
                    + "' but was '"
                    + actual.getText()
                    + "'"
            );
        }

        return this;
    }

    public TokenAssert hasType(final int expected) {
        isNotNull();

        if (actual.getType() != expected) {
            failWithMessage("Expected token to have type "
                    + JasmLexer.VOCABULARY.getSymbolicName(expected)
                    + " but was "
                    + JasmLexer.VOCABULARY.getSymbolicName(actual.getType())
            );
        }

        return this;
    }
}
