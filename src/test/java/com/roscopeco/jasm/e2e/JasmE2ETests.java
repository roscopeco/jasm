/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.e2e;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.roscopeco.jasm.TestUtil.assembleAndDefine;
import static com.roscopeco.jasm.TestUtil.boolVoidInvoker;
import static com.roscopeco.jasm.TestUtil.floatVoidInvoker;
import static com.roscopeco.jasm.TestUtil.instantiate;
import static com.roscopeco.jasm.TestUtil.intVoidInvoker;
import static com.roscopeco.jasm.TestUtil.objectArgsInvoker;
import static com.roscopeco.jasm.TestUtil.objectVoidInvoker;
import static org.assertj.core.api.Assertions.assertThat;

class JasmE2ETests {
    @Test
    void shouldAssembleEmptyClassToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/EmptyClassInPackage.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.EmptyClassInPackage");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).isEmpty();
    }

    @Test
    void shouldAssembleBasicFieldTestsToValidJavaClass() throws NoSuchFieldException {
        final var clz = assembleAndDefine("com/roscopeco/jasm/BasicFieldTests.jasm");

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
        final var clz = assembleAndDefine("com/roscopeco/jasm/MinimalMethodTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.MinimalMethodTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredMethods()).hasSize(1);
    }

    @Test
    void shouldAssembleIconstVariantsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IconstVariants.jasm");

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
        final var clz = assembleAndDefine("com/roscopeco/jasm/ConstructorMethodTest.jasm");

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

    @Test
    void shouldAssembleAconstNullAreturnToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/LdcAconstAreturn.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.LdcAconstAreturn");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(5);

        final var obj = instantiate(clz);

        // Tests ACONST_NULL, ARETURN
        assertThat(objectVoidInvoker(obj, "testAconstNull").get()).isNull();

        // Tests LDC(str), ARETURN
        assertThat(objectVoidInvoker(obj, "testLdcString").get()).isEqualTo("The test string");

        // Tests LDC(int), IRETURN
        assertThat(intVoidInvoker(obj, "testLdcInt").get()).isEqualTo(10);

        // Tests LDC(float), FRETURN
        assertThat(floatVoidInvoker(obj, "testLdcFloat").get()).isEqualTo(5.5f);

        // Tests LDC(bool), IRETURN
        assertThat(boolVoidInvoker(obj, "testLdcBool").get()).isTrue();
    }

    @Test
    void shouldAssembleInvokeTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/InvokeTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.InvokeTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(3);

        final var obj = instantiate(clz);

        // Tests all four non-dynamic INVOKES
        final var list = new ArrayList<String>();

        assertThat(list).isEmpty();

        assertThat(objectArgsInvoker(obj, "testMethod", new Class[] { List.class }).apply(new Object[] { list }))
                .isSameAs(list);

        assertThat(list).containsExactly("Hello World");
    }

    @Test
    void shouldAssembleNewDupTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/NewDupTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.NewDupTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        // Tests both NEW and DUP
        final var obj = objectVoidInvoker(clz, "createList").get();

        assertThat(obj).isInstanceOf(ArrayList.class);

        final var list = (ArrayList<?>)obj;

        assertThat(list).isEmpty();
    }

    @Test
    void shouldAssembleGotoTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/GotoLabelTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.GotoLabelTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var list = new ArrayList<>();

        // Tests GOTO and labels - should skip adding "CANARY" to the list
        final var obj = objectArgsInvoker(clz, "testMethod", List.class).apply(new Object[] { list });

        assertThat(list).isEmpty();
    }
}
