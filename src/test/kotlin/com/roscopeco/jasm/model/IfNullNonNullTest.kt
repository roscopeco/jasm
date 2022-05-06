package com.roscopeco.jasm.model

interface IfNullNonNullTest {
    fun testIfNullWhenNullPasses(): Boolean
    fun testIfNullWhenNonNullPasses(): Boolean
    fun testIfNonNullWhenNullPasses(): Boolean
    fun testIfNonNullWhenNonNullPasses(): Boolean
}