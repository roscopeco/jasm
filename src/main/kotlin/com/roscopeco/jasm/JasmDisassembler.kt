/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import com.roscopeco.jasm.errors.StandardErrorCollector
import org.objectweb.asm.ClassReader
import java.io.IOException
import java.io.InputStream
import java.io.UncheckedIOException
import java.util.function.Supplier

/**
 * The JASM disassembler
 *
 * **Note**: The disassembler **will** close the stream returned by the supplier when it is done!
 *
 * @param unitName The (display) name of the compilation unit
 * @param source A supplier of `InputStream`
 */
class JasmDisassembler(
    private val unitName: String,
    private val lineNumbers: Boolean,
    private val source: Supplier<InputStream>,
) {

    /**
     * Disassemble Java bytecode to JASM source
     *
     * @return JASM source, suitable for passing to JasmAssembler
     */
    fun disassemble(): String {
        try {
            source.get().use { input ->
                val errorCollector = StandardErrorCollector()

                val classReader = ClassReader(input)

                val visitor = JasmDisassemblingVisitor(unitName, lineNumbers, errorCollector)
                classReader.accept(visitor, ClassReader.SKIP_FRAMES)

                if (errorCollector.hasErrors()) {
                    throw AssemblyException(errorCollector.getErrors())
                } else {
                    return visitor.output()
                }
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}