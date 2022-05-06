package com.roscopeco.jasm.model

interface LongMathTests {
    fun testL2d(input: Long): Double
    fun testL2f(input: Long): Float
    fun testL2i(input: Long): Int
    fun testLadd(d1: Long, d2: Long): Long
    fun testLand(a: Long, b: Long): Long
    fun testLcmp(a: Long, b: Long): Int
    fun testLdiv(a: Long, b: Long): Long
    fun testLmul(a: Long, b: Long): Long
    fun testLneg(a: Long): Long
    fun testLor(a: Long, b: Long): Long
    fun testLrem(a: Long, b: Long): Long
    fun testLshl(a: Long, b: Int): Long
    fun testLshr(a: Long, b: Int): Long
    fun testLsub(a: Long, b: Long): Long
    fun testLushr(a: Long, b: Int): Long
    fun testLxor(a: Long, b: Long): Long
}