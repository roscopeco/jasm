package com.roscopeco.jasm.tool

interface Task<R> {
    fun perform(): R
}