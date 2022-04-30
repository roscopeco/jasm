/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import lombok.NonNull;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

class ThrowingErrorListener extends BaseErrorListener {
    private final String unitName;

    ThrowingErrorListener(@NonNull final String unitName) {
        this.unitName = unitName;
    }

    @Override
    public void syntaxError(
            final Recognizer<?, ?> recognizer,
            final Object offendingSymbol,
            final int line,
            final int charPositionInLine,
            final String msg,
            final RecognitionException e
    ) {
        throw new SyntaxErrorException(unitName + ": line " + line + ":" + charPositionInLine + " " + msg, e);
    }
}
