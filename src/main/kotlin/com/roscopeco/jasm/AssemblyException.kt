package com.roscopeco.jasm

import com.roscopeco.jasm.errors.BaseError

class AssemblyException(val codeErrors: List<BaseError>) : JasmException() {
    companion object {
        val EOL: String = System.lineSeparator();
    }

    override val message: String
        get() = "Errors: $EOL" + codeErrors.joinToString(separator = EOL) { "    $it" }
}