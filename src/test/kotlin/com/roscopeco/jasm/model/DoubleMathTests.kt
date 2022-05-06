package com.roscopeco.jasm.model

interface DoubleMathTests {
    fun testD2f(input: Double): Float
    fun testD2i(input: Double): Int
    fun testD2l(input: Double): Long
    fun testDadd(d1: Double, d2: Double): Double
    fun testDconst0(): Double
    fun testDconst1(): Double
    fun testDcmpg(a: Double, b: Double): Int
    fun testDcmpl(a: Double, b: Double): Int
    fun testDdiv(a: Double, b: Double): Double
    fun testDmul(a: Double, b: Double): Double
    fun testDneg(a: Double): Double
    fun testDrem(a: Double, b: Double): Double
    fun testDsub(a: Double, b: Double): Double
}