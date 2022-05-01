package com.roscopeco.jasm

/*
 * This only exists to workaround IntelliJ warnings about Java code using internal
 * Kotlin classes from another module...
 */
internal class TestErrorListener(unitName: String) : ThrowingErrorListener(unitName)