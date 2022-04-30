/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException() {
    }

    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxErrorException(Throwable cause) {
        super(cause);
    }

    public SyntaxErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
