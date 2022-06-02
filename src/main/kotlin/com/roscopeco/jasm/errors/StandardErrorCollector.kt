package com.roscopeco.jasm.errors

class StandardErrorCollector: ErrorCollector {
    private val errors: MutableList<BaseError> = mutableListOf()

    override fun addError(error: BaseError) {
        errors.add(error)
    }

    override fun hasErrors() = errors.isNotEmpty()

    override fun getErrors(): List<BaseError> = errors.toList()
}
