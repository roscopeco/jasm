package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JasmBaseVisitor
import com.roscopeco.jasm.antlr.JasmParser
import com.roscopeco.jasm.errors.CodeError
import com.roscopeco.jasm.errors.ErrorCollector

class TypeVisitor(private val unitName: String, private val errorCollector: ErrorCollector) : JasmBaseVisitor<String>() {
    override fun aggregateResult(aggregate: String?, nextResult: String?): String {
        return (aggregate ?: "") + (nextResult ?: "")
    }

    override fun defaultResult() = ""

    override fun visitMethod_arguments(ctx: JasmParser.Method_argumentsContext?) =
        "(" + super.visitMethod_arguments(ctx) + ")"

    override fun visitPrim_type(ctx: JasmParser.Prim_typeContext) = when {
        ctx.TYPE_BOOL()   != null       -> "Z"
        ctx.TYPE_BYTE()   != null       -> "B"
        ctx.TYPE_CHAR()   != null       -> "C"
        ctx.TYPE_DOUBLE() != null       -> "D"
        ctx.TYPE_FLOAT()  != null       -> "F"
        ctx.TYPE_INT()    != null       -> "I"
        ctx.TYPE_LONG()   != null       -> "J"
        ctx.TYPE_SHORT()  != null       -> "S"
        else -> {
            errorCollector.addError(CodeError(unitName, ctx, "Invalid type ${ctx.text} encountered in method descriptor"))
            "I"
        }
    }

    override fun visitVoid_type(ctx: JasmParser.Void_typeContext) = when {
        ctx.TYPE_VOID()   != null       -> "V"
        else -> {
            errorCollector.addError(CodeError(unitName, ctx, "[BUG] Invalid type ${ctx.text} encountered as TYPE_VOID"))
            "V"
        }
    }

    override fun visitRef_type(ctx: JasmParser.Ref_typeContext) = "L" + ctx.text + ";"

    override fun visitArray_type(ctx: JasmParser.Array_typeContext) =
        ctx.LSQUARE().joinToString(separator = "") { it.text } + super.visitArray_type(ctx)
}