package com.roscopeco.jasm.model

interface CheckcastTest {
    fun castToList(obj: Any): List<*>
    fun castToStringArray(obj: Any): Array<String>
}