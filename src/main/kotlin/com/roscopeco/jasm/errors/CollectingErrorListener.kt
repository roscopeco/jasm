package com.roscopeco.jasm.errors

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CommonToken
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.misc.Interval

class CollectingErrorListener(private val unitName: String, private val errorCollector: ErrorCollector) : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        message: String?,
        e: RecognitionException?
    ) {
        if (offendingSymbol is CommonToken) {
            errorCollector.addError(
                CodeError(
                    unitName,
                    offendingSymbol,
                    offendingSymbol,
                    Interval(offendingSymbol.startIndex, offendingSymbol.stopIndex),
                    message ?: "<? Unknown error ?>"
                )
            )
        } else {
            errorCollector.addError(
                BaseError(unitName, message ?: "<? Unknown error ?>")
            )
        }
    }
}