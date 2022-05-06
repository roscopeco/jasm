package com.roscopeco.jasm.model

interface RefArrayTests {
    fun newSingleElementArray(): Array<String?>
    fun putInArray(array: Array<String?>, data: String)
    fun getFromArray(array: Array<String?>): String
    fun getArrayLength(array: Array<String?>): Int
    fun multiANewArrayTest(): Array<Array<String?>?>
    fun multiANewArrayExplicitDimsTest(): Array<Array<String?>?>
}