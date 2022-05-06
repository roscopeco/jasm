package com.roscopeco.jasm.model

interface LoadsAndStoresTest {
    fun testAloadAstore(): Any
    fun testDloadDstore(input: Double): Double
    fun testFloadFstore(input: Float): Float
    fun testIloadIstore(input: Int): Int
    fun testLloadLstore(input: Long): Long
}