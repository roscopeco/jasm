package com.roscopeco.jasm.errors

open class BaseError(val unitName: String, val message: String) {
    open val displayMessage: String
        get() = toString()

    override fun toString(): String = "$unitName:[?,?]: $message"
}
