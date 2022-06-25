package com.roscopeco.jasm.model

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodType

interface LdcAconstAreturn {
    fun testAconstNull(): Any?
    fun testLdcString(): String
    fun testLdcInt(): Int
    fun testLdcFloat(): Float
    fun testLdcLong(): Long
    fun testLdcDouble(): Double
    fun testLdcBool(): Boolean
    fun testLdcClass(): Class<*>
    fun testLdcMethodType(): MethodType
    fun testLdcMethodHandle(): MethodHandle
    fun testLdcDynamicConst(): String
}