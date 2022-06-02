package com.roscopeco.jasm

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

internal open class TestErrorListener(private val unitName: String) : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) = throw ErrorForTestsException("$unitName: line $line:$charPositionInLine $msg", e ?: RuntimeException())
}