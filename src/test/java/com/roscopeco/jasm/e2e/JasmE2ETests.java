/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.e2e;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import com.roscopeco.jasm.JasmAssembler;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static com.roscopeco.jasm.TestUtil.inputStreamForTestCase;
import static com.roscopeco.jasm.TestUtil.jasmPackageLookup;
import static org.assertj.core.api.Assertions.assertThat;

class JasmE2ETests {
    @Test
    void shouldAssembleEmptyClassToValidJavaClass() {
        final var clz = assemble("com/roscopeco/jasm/EmptyClassInPackage.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.EmptyClassInPackage");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).isEmpty();
    }

    @Test
    void shouldAssembleBasicFieldTestsToValidJavaClass() throws NoSuchFieldException {
        final var clz = assemble("com/roscopeco/jasm/BasicFieldTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.BasicFieldTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).isEmpty();

        assertThat(clz.getDeclaredFields()).hasSize(6);

        assertThat(clz.getDeclaredField("defaultIntField"))
                .hasToString("int com.roscopeco.jasm.BasicFieldTests.defaultIntField");
        assertThat(clz.getDeclaredField("publicLongField"))
                .hasToString("public long com.roscopeco.jasm.BasicFieldTests.publicLongField");
        assertThat(clz.getDeclaredField("finalFloatField"))
                .hasToString("final float com.roscopeco.jasm.BasicFieldTests.finalFloatField");
        assertThat(clz.getDeclaredField("defaultDoubleField"))
                .hasToString("double com.roscopeco.jasm.BasicFieldTests.defaultDoubleField");
        assertThat(clz.getDeclaredField("defaultBoolField"))
                .hasToString("boolean com.roscopeco.jasm.BasicFieldTests.defaultBoolField");
        assertThat(clz.getDeclaredField("defaultRefField"))
                .hasToString("java.lang.Object com.roscopeco.jasm.BasicFieldTests.defaultRefField");
    }

    @Test
    void shouldAssembleMinimalMethodTestToValidJavaClass() {
        final var clz = assemble("com/roscopeco/jasm/MinimalMethodTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.MinimalMethodTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredMethods()).hasSize(1);
    }

    @Test
    void shouldAssembleIconstVariantsToValidJavaClass() {
        final var clz = assemble("com/roscopeco/jasm/IconstVariants.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IconstVariants");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredMethods()).hasSize(7);

        assertThat(intVoidInvoker(clz, "insnTestM1").get()).isEqualTo(-1);
        assertThat(intVoidInvoker(clz, "insnTest0").get()).isZero();
        assertThat(intVoidInvoker(clz, "insnTest1").get()).isEqualTo(1);
        assertThat(intVoidInvoker(clz, "insnTest2").get()).isEqualTo(2);
        assertThat(intVoidInvoker(clz, "insnTest3").get()).isEqualTo(3);
        assertThat(intVoidInvoker(clz, "insnTest4").get()).isEqualTo(4);
        assertThat(intVoidInvoker(clz, "insnTest5").get()).isEqualTo(5);
    }

    @Test
    void shouldAssembleConstructorMethodTestToValidJavaClass() {
        final var clz = assemble("com/roscopeco/jasm/ConstructorMethodTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.ConstructorMethodTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        // Tests default constructor, ALOAD, INVOKESPECIAL, RETURN
        final var obj = instantiate(clz);

        // Tests instance method generation, ICONST_M1, IRETURN
        assertThat(intVoidInvoker(obj, "testMethod").get()).isEqualTo(-1);
    }

    private Supplier<Integer> intVoidInvoker(final Object receiver, final String name) {
        return () -> {
            try {
                final var m = receiver instanceof Class<?> c
                        ? c.getMethod(name)
                        : receiver.getClass().getMethod(name);

                m.setAccessible(true);

                final var r = m.invoke(receiver instanceof Class<?> ? null : receiver);

                if (r instanceof Integer i) {
                    return i;
                } else {
                    throw new AssertionFailedError("Method " + name + " does not return integer - it returns " + r.getClass() + " instead!");
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new AssertionFailedError("Failed to invoke " + name + " on receiver of class " + receiver.getClass(), e);
            }
        };
    }

    private Object instantiate(final Class<?> clz) {
        try {
            final var ctor = clz.getConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AssertionFailedError("Failed to instantiate class " + clz + " with default constructor", e);
        }
    }

    private Class<?> assemble(final String testCase) {
        return defineClass(new JasmAssembler(() -> inputStreamForTestCase(testCase)).assemble());
    }

    private Class<?> defineClass(final byte[] bytes) {
        try {
            return jasmPackageLookup().defineClass(bytes);
        } catch (IllegalAccessException e) {
            throw new AssertionFailedError("Failed to define class", e);
        }
    }
}
