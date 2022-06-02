package com.roscopeco.jasm.errors

interface ErrorCollector {
    fun addError(error: BaseError)
    fun hasErrors(): Boolean
    fun getErrors(): List<BaseError>
}