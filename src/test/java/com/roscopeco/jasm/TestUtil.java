/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

import com.roscopeco.jasm.antlr.JasmLexer;
import com.roscopeco.jasm.antlr.JasmParser;
import lombok.NonNull;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;
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

    public static JasmLexer testCaseLexer(@NonNull final String testCase) {
        try (final var input = inputStreamForTestCase(testCase)) {

            assertThat(input).as("InputStream for test-case source: "+ testCase).isNotNull();

            return buildLexer(testCase, CharStreams.fromStream(input));

        } catch (IOException e) {
            throw new AssertionFailedError("Unable to load testCase: " + testCase, e);
        }
    }

    public static JasmParser testCaseParser(@NonNull final String testCase) {
        return buildParser(testCase, new CommonTokenStream(testCaseLexer(testCase)));
    }

    public static JasmParser.ClassContext doParse(final String testCase) {
        return testCaseParser(testCase).class_();
    }

    public static JasmLexer buildLexer(@NonNull final String unitName, @NonNull final CharStream input) {
        final var lexer = new JasmLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new TestErrorListener(unitName));
        return lexer;
    }

    public static JasmParser buildParser(@NonNull final String unitName, @NonNull final TokenStream tokens) {
        final var parser = new JasmParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new TestErrorListener(unitName));
        return parser;
    }

    public static Method getAccessibleMethod(final Object receiver, final String name, Class<?>... paramTypes)
            throws NoSuchMethodException {
        final var m = receiver instanceof Class<?> c
                ? c.getDeclaredMethod(name, paramTypes)
                : receiver.getClass().getDeclaredMethod(name, paramTypes);

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

    public static Function<Object[], Object> objectArgsInvoker(
            final Object receiver,
            final String name,
            final Class<?>... paramTypes
    ) {
        return (args) -> {
            try {
                return getAccessibleMethod(receiver, name, paramTypes)
                        .invoke(receiver instanceof Class<?> ? null : receiver, args);
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

    public static <T> T instantiate(@NonNull final Class<?> implementation, @NonNull final Class<T> iface) {
        return iface.cast(instantiate(implementation));
    }

    public static byte[] assemble(final String testCase) {
        final var bytes = new JasmAssembler(testCase, () -> inputStreamForTestCase(testCase)).assemble();

        if (Boolean.parseBoolean(System.getProperty("jasmTestDumpClass"))) {
            final var classReader = new ClassReader(bytes);
            final var tcv = new TraceClassVisitor(new PrintWriter(System.out));

            classReader.accept(tcv, 0);
        }

        return bytes;
    }

    public static Class<?> assembleAndDefine(final String testCase) {
        return defineClass(assemble(testCase));
    }

    public static Class<?> defineClass(final byte[] bytes) {
        try {
            return jasmPackageLookup().defineClass(bytes);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Failed to define class", e);
        }
    }
}
