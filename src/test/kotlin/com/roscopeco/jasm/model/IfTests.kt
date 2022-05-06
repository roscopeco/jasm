package com.roscopeco.jasm.model

interface IfTests {
    fun testEqWhenEqualPasses(): Boolean
    fun testEqNotEqualPasses(): Boolean
    fun testGeWhenLessPasses(): Boolean
    fun testGeWhenEqualPasses(): Boolean
    fun testGeWhenGreaterPasses(): Boolean
    fun testGtWhenLessPasses(): Boolean
    fun testGtWhenGreaterPasses(): Boolean
    fun testLeWhenLessPasses(): Boolean
    fun testLeWhenEqualPasses(): Boolean
    fun testLeWhenGreaterPasses(): Boolean
    fun testLtWhenLessPasses(): Boolean
    fun testLtWhenGreaterPasses(): Boolean
    fun testNeWhenEqualPasses(): Boolean
    fun testNeNotEqualPasses(): Boolean
}