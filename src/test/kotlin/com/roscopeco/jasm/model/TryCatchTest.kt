package com.roscopeco.jasm.model

interface TryCatchTest {
    fun manualExceptionHandlerTest(): Exception
    fun basicTryCatchTest(): String
    fun nestedTryCatchTest(): Int
    fun tryMultipleCatchTest(ex: Exception): String
}