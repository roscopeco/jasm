/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JasmLexer
import com.roscopeco.jasm.antlr.JasmParser
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.TokenStream
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.IOException
import java.io.InputStream
import java.io.UncheckedIOException
import java.util.*
import java.util.function.Supplier

/**
 * The JASM assembler
 *
 * **Note**: The assembler **will** close the stream returned by the supplier when it is done!
 *
 * @param unitName The (display) name of the compilation unit
 * @param source A supplier of `InputStream`
 */
class JasmAssembler(private val unitName: String, private val classFormat: Int, private val source: Supplier<InputStream>) {

    /**
     * Convenience constructor which will use the class format for Java 17 (61.0).
     *
     * @param unitName The name of the compilation unit (shows up in errors and as an attribute in the class)
     * @param source A supplier of `InputStream`
     */
    constructor(unitName: String, source: Supplier<InputStream>)
            : this(unitName, Opcodes.V17, source)

    /**
     * Assemble to Java bytecode.
     *
     * @return bytecode, suitable for passing to `MethodHandles.Lookup#defineClass`
     */
    fun assemble(): ByteArray {
        try {
            source.get().use { input ->
                val parser = buildParser(
                    CommonTokenStream(
                        buildLexer(
                            Objects.requireNonNull(CharStreams.fromStream(input),
                                "Failed to open stream for ${unitName}")
                        )
                    )
                )
                val classWriter =
                    ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
                val assembler = JasmAssemblingVisitor(classWriter, unitName, classFormat)

                parser.class_().accept(assembler)

                return classWriter.toByteArray()
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    private fun buildLexer(input: CharStream): JasmLexer {
        val lexer = JasmLexer(input)
        lexer.removeErrorListeners()
        lexer.addErrorListener(ThrowingErrorListener(unitName))
        return lexer
    }

    private fun buildParser(tokens: TokenStream): JasmParser {
        val parser = JasmParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(ThrowingErrorListener(unitName))
        return parser
    }
}