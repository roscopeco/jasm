package com.roscopeco.jasm.model

interface FloatMathTests {
    fun testF2d(input: Float): Double
    fun testF2i(input: Float): Int
    fun testF2l(input: Float): Long
    fun testFadd(d1: Float, d2: Float): Float
    fun testFconst0(): Float
    fun testFconst1(): Float
    fun testFcmpg(a: Float, b: Float): Int
    fun testFcmpl(a: Float, b: Float): Int
    fun testFdiv(a: Float, b: Float): Float
    fun testFmul(a: Float, b: Float): Float
    fun testFneg(a: Float): Float
    fun testFrem(a: Float, b: Float): Float
    fun testFsub(a: Float, b: Float): Float
}