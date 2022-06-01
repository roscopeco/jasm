package com.roscopeco.jasm.errors

open class BaseError(val unitName: String, val message: String) {
    override fun toString(): String = "$unitName:[?,?]: $message"
}
