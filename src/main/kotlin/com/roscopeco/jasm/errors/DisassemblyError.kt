package com.roscopeco.jasm.errors

enum class DisassemblyContext {
    ConstArg,
    Descriptor
}
class DisassemblyError(unitName: String, val context: DisassemblyContext, message: String) : BaseError(unitName, message) {
    override val displayMessage: String
        get() = "$unitName:[${context}]: $message"

    override fun toString(): String = "$unitName:[${context}]: $message"
}