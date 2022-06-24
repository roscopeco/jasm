/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

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
                val classReader = ClassReader(input)

                val visitor = JasmDisassemblingVisitor(unitName, true)
                classReader.accept(visitor, ClassReader.SKIP_FRAMES)
                return visitor.output()
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }
}