package com.roscopeco.jasm.tool

import com.roscopeco.jasm.JasmDisassembler
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DisassembleTask(src: File, dest: File) : FileTransformTask<AssemblyResult>(src, dest) {
    private val assembler = JasmDisassembler(unitName()) { FileInputStream(src) }

    private fun unitName(): String = src.name
    
    override fun perform(): AssemblyResult {
        try {
            File(dest.parent ?: ".").mkdirs()
            FileOutputStream(dest).use { it.write(assembler.disassemble().toByteArray()) }
        } catch (e: Exception) {
            return AssemblyResult(unitName(), false, e.message ?: "[BUG]: <Unknown> [${e}")
        }

        return AssemblyResult(unitName(), true)
    }
}