package com.roscopeco.jasm.model

interface PrimArrayTests {
    fun testBipushBastore(input: ByteArray)
    fun testBaload(input: ByteArray): Byte
    fun testCastore(input: CharArray)
    fun testCaload(input: CharArray): Char
    fun testDastore(input: DoubleArray, testval: Double)
    fun testDaload(input: DoubleArray): Double
    fun testFastore(input: FloatArray, testval: Float)
    fun testFaload(input: FloatArray): Float
    fun testIastore(input: IntArray, testval: Int)
    fun testIaload(input: IntArray): Int
    fun testLastore(input: LongArray, testval: Long)
    fun testLaload(input: LongArray): Long
    fun testSastore(input: ShortArray, testval: Short)
    fun testSaload(input: ShortArray): Short
    fun testNewarray(size: Int): IntArray
}