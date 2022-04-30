/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import com.roscopeco.jasm.antlr.JasmLexer;
import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

/**
 * The JASM Assembler
 */
public final class JasmAssembler {
    private final String unitName;
    private final Supplier<InputStream> source;

    /**
     * Construct a new assembler for the given source.
     *
     * **Note**: The assembler **will** close the stream returned by the supplier when it is done!
     *
     * @param source A supplier of {@code InputStream}
     */
    public JasmAssembler(@NonNull final String unitName, @NonNull final Supplier<InputStream> source) {
        this.unitName = unitName;
        this.source = source;
    }

    /**
     * Assemble to Java bytecode.
     *
     * @return bytecode, suitable for passing to {@code MethodHandles.Lookup#defineClass}
     */
    public byte[] assemble() {
        try (final var input = source.get()) {
            final var parser = buildParser(new CommonTokenStream(buildLexer(CharStreams.fromStream(input))));
            final var classWriter = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
            final var assembler = new JasmAssemblingVisitor(classWriter, new Modifiers());

            parser.class_().accept(assembler);

            return classWriter.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private JasmLexer buildLexer(final CharStream input) {
        final var lexer = new JasmLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener(this.unitName));
        return lexer;
    }

    private JasmParser buildParser(final TokenStream tokens) {
        final var parser = new JasmParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener(this.unitName));
        return parser;
    }
}
