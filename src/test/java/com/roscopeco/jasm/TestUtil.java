/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

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

    public static Method getAccessibleMethod(final Object receiver, final String name)
            throws NoSuchMethodException {
        final var m = receiver instanceof Class<?> c
                ? c.getDeclaredMethod(name)
                : receiver.getClass().getDeclaredMethod(name);

        m.setAccessible(true);

        return m;
    }

    public static Supplier<Boolean> boolVoidInvoker(final Object receiver, final String name) {
        return () -> {
            try {
                final var r = getAccessibleMethod(receiver, name)
                        .invoke(receiver instanceof Class<?> ? null : receiver);

                return castOrFail(name, r, Boolean.class);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionFailedError("Failed to invoke " + name + " on receiver of class "
                        + receiver.getClass(), e);
            }
        };
    }

    public static Supplier<Float> floatVoidInvoker(final Object receiver, final String name) {
        return () -> {
            try {
                final var r = getAccessibleMethod(receiver, name)
                        .invoke(receiver instanceof Class<?> ? null : receiver);

                return castOrFail(name, r, Float.class);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionFailedError("Failed to invoke " + name + " on receiver of class "
                        + receiver.getClass(), e);
            }
        };
    }

    public static Supplier<Integer> intVoidInvoker(final Object receiver, final String name) {
        return () -> {
            try {
                final var r = getAccessibleMethod(receiver, name)
                        .invoke(receiver instanceof Class<?> ? null : receiver);

                return castOrFail(name, r, Integer.class);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionFailedError("Failed to invoke " + name + " on receiver of class "
                        + receiver.getClass(), e);
            }
        };
    }

    @SuppressWarnings("unchecked" /* manual checking */)
    private static <T> T castOrFail(final String mname, final Object o, final Class<T> clz) {
        if (clz.isInstance(o)) {
            return (T)o;
        } else {
            throw new AssertionFailedError("Method " + mname + " does not return " + clz.getName() + " - it returns "
                    + o.getClass() + " instead!");
        }
    }

    public static Supplier<Object> objectVoidInvoker(final Object receiver, final String name) {
        return () -> {
            try {
                return getAccessibleMethod(receiver, name)
                        .invoke(receiver instanceof Class<?> ? null : receiver);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionFailedError("Failed to invoke " + name + " on receiver of class " + receiver.getClass(), e);
            }
        };
    }

    public static Object instantiate(final Class<?> clz) {
        try {
            final var ctor = clz.getConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AssertionFailedError("Failed to instantiate class " + clz + " with default constructor", e);
        }
    }

    public static Class<?> assemble(final String testCase) {
        return defineClass(new JasmAssembler(() -> inputStreamForTestCase(testCase)).assemble());
    }

    public static Class<?> defineClass(final byte[] bytes) {
        try {
            return jasmPackageLookup().defineClass(bytes);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Failed to define class", e);
        }
    }

}
