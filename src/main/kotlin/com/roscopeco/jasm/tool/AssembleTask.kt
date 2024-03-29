package com.roscopeco.jasm.tool

import com.roscopeco.jasm.JasmAssembler
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AssembleTask(src: File, dest: File, val targetVersion: Int) : FileTransformTask<AssemblyResult>(src, dest) {
    private val assembler = JasmAssembler(unitName(), targetVersion) { FileInputStream(src) }

    private fun unitName(): String = src.name
    
    override fun perform(): AssemblyResult {
        try {
            File(dest.parent ?: ".").mkdirs()
            FileOutputStream(dest).use { it.write(assembler.assemble()) }
        } catch (e: Exception) {
            return AssemblyResult(unitName(), false, e.message ?: "[BUG]: <Unknown> [${e}")
        }

        return AssemblyResult(unitName(), true)
    }
}