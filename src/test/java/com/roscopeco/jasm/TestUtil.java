/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

import com.roscopeco.jasm.antlr.JasmLexer;
import com.roscopeco.jasm.antlr.JasmParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {
    private static final MethodHandles.Lookup JASM_LOOKUP = MethodHandles.lookup();

    public static MethodHandles.Lookup jasmPackageLookup() {
        return JASM_LOOKUP;
    }

    public static InputStream inputStreamForTestCase(final String testCase) {
        return TestUtil.class.getClassLoader().getResourceAsStream("jasm/" + testCase);
    }

    public static JasmLexer testCaseLexer(final String testCase) {
        try (final var input = inputStreamForTestCase(testCase)) {

            assertThat(input).as("InputStream for test-case source").isNotNull();

            return buildLexer(CharStreams.fromStream(input));

        } catch (IOException e) {
            throw new AssertionFailedError("Unable to load testCase: " + testCase, e);
        }
    }

    public static JasmParser testCaseParser(final String testCase) {
        return buildParser(new CommonTokenStream(testCaseLexer(testCase)));
    }

    public static JasmParser.ClassContext doParse(final String testCase) {
        return testCaseParser(testCase).class_();
    }

    public static JasmLexer buildLexer(final CharStream input) {
        final var lexer = new JasmLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);
        return lexer;
    }

    public static JasmParser buildParser(final TokenStream tokens) {
        final var parser = new JasmParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        return parser;
    }

}
