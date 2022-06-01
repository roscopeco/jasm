package com.roscopeco.jasm.errors

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval

class CodeError(unitName: String, val start: Token, val stop: Token, val interval: Interval, message: String) : BaseError(unitName, message) {
    constructor(unitName: String, ctx: ParserRuleContext, message: String) : this(unitName, ctx.start, ctx.stop, ctx.sourceInterval, message)

    override fun toString(): String = "$unitName:[${start.line}:${start.charPositionInLine}]: $message"
}