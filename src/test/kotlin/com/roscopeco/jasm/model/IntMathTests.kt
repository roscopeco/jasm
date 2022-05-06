package com.roscopeco.jasm.model

interface IntMathTests {
    fun testI2b(input: Int): Byte
    fun testI2c(input: Int): Char
    fun testI2d(input: Int): Double
    fun testI2f(input: Int): Float
    fun testI2l(input: Int): Long
    fun testI2s(input: Int): Short
    fun testIadd(d1: Int, d2: Int): Int
    fun testIand(a: Int, b: Int): Int
    fun testIdiv(a: Int, b: Int): Int
    fun testIinc(a: Int): Int
    fun testImul(a: Int, b: Int): Int
    fun testIneg(a: Int): Int
    fun testIor(a: Int, b: Int): Int
    fun testIrem(a: Int, b: Int): Int
    fun testIshl(a: Int, b: Int): Int
    fun testIshr(a: Int, b: Int): Int
    fun testIsub(a: Int, b: Int): Int
    fun testIushr(a: Int, b: Int): Int
    fun testIxor(a: Int, b: Int): Int
}