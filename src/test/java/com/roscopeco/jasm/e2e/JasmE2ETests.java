/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.e2e;

import com.roscopeco.jasm.model.AthrowTest;
import com.roscopeco.jasm.model.CheckcastTest;
import com.roscopeco.jasm.model.IfIcmpTests;
import com.roscopeco.jasm.model.IfTests;
import com.roscopeco.jasm.model.Interface1;
import com.roscopeco.jasm.model.Interface2;
import com.roscopeco.jasm.model.IfNullNonNullTest;
import com.roscopeco.jasm.model.InvokedynamicTest;
import com.roscopeco.jasm.model.LdcAconstAreturn;
import com.roscopeco.jasm.model.LoadsAndStoresTest;
import com.roscopeco.jasm.model.RefArrayTests;
import com.roscopeco.jasm.model.Superclass;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.roscopeco.jasm.TestUtil.assembleAndDefine;
import static com.roscopeco.jasm.TestUtil.boolVoidInvoker;
import static com.roscopeco.jasm.TestUtil.instantiate;
import static com.roscopeco.jasm.TestUtil.intVoidInvoker;
import static com.roscopeco.jasm.TestUtil.objectArgsInvoker;
import static com.roscopeco.jasm.TestUtil.objectVoidInvoker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

@SuppressWarnings("java:S5961" /* Some methods need to assert combinatorial explosion of multiple variants */)
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
    void shouldAssembleLdcAconstNullAreturnToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/LdcAconstAreturn.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.LdcAconstAreturn");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();

        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(9);

        final var obj = instantiate(clz, LdcAconstAreturn.class);

        // Tests ACONST_NULL, ARETURN
        assertThat(obj.testAconstNull()).isNull();

        // Tests LDC(str), ARETURN
        assertThat(obj.testLdcString()).isEqualTo("The test string");

        // Tests LDC(int), IRETURN
        assertThat(obj.testLdcInt()).isEqualTo(10);

        // Tests LDC(float), FRETURN
        assertThat(obj.testLdcFloat()).isEqualTo(5.5f);

        // Tests LDC(bool), IRETURN
        assertThat(obj.testLdcBool()).isTrue();

        // Tests LDC(class), ARETURN
        assertThat(obj.testLdcClass()).isEqualTo(List.class);

        // Tests LDC(methodtype), ARETURN
        assertThat(obj.testLdcMethodType().returnType()).isEqualTo(int.class);
        assertThat(obj.testLdcMethodType().parameterList()).containsExactly(List.class);

        // Tests LDC(methodHandle), ARETURN, INVOKESTATIC
        try {
            assertThat(obj.testLdcMethodHandle().invoke()).isEqualTo("Handle is good");
        } catch (Throwable t) {
            fail("LDC MethodHandle test should not have thrown " + t);
        }

        // Tests LDC(constantdynamic), ARETURN, INVOKESTATIC
        // Value here comes from DYNAMIC_CONST_FOR_TEST in TestBoostrap class, and is loaded via
        // java.lang.invoke.ConstantBootstraps.getStaticFinal
        assertThat(obj.testLdcDynamicConst()).isEqualTo("The expected result");
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
        objectArgsInvoker(clz, "testMethod", List.class).apply(new Object[] { list });

        assertThat(list).isEmpty();
    }

    @Test
    void shouldAssembleIfAcmpTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IfAcmpTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IfAcmpTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(4);

        final var obj = instantiate(clz);

        // Tests IF_ACMPEQ and IF_ACMPNE
        assertThat(boolVoidInvoker(obj, "testEqWhenEqualPasses").get()).isTrue();
        assertThat(boolVoidInvoker(obj, "testEqNotEqualPasses").get()).isTrue();
        assertThat(boolVoidInvoker(obj, "testNeWhenEqualPasses").get()).isTrue();
        assertThat(boolVoidInvoker(obj, "testNeNotEqualPasses").get()).isTrue();
    }

    @Test
    void shouldAssembleInheritanceAndInterfaceTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/InheritAndInterfaceTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.InheritAndInterfaceTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).isEmpty();

        assertThat(Superclass.class).isAssignableFrom(clz);
        assertThat(Interface1.class).isAssignableFrom(clz);
        assertThat(Interface2.class).isAssignableFrom(clz);
    }

    @Test
    void shouldAssembleIfIcmpTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IfIcmpTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IfIcmpTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(14);

        final var obj = instantiate(clz, IfIcmpTests.class);

        // Tests IF_ICMPxx (all variants)
        assertThat(obj.testEqWhenEqualPasses()).isTrue();
        assertThat(obj.testEqNotEqualPasses()).isTrue();
        assertThat(obj.testGeWhenLessPasses()).isTrue();
        assertThat(obj.testGeWhenEqualPasses()).isTrue();
        assertThat(obj.testGeWhenGreaterPasses()).isTrue();
        assertThat(obj.testGtWhenLessPasses()).isTrue();
        assertThat(obj.testGtWhenGreaterPasses()).isTrue();
        assertThat(obj.testLeWhenLessPasses()).isTrue();
        assertThat(obj.testLeWhenEqualPasses()).isTrue();
        assertThat(obj.testLeWhenGreaterPasses()).isTrue();
        assertThat(obj.testLtWhenLessPasses()).isTrue();
        assertThat(obj.testLtWhenGreaterPasses()).isTrue();
        assertThat(obj.testNeWhenEqualPasses()).isTrue();
        assertThat(obj.testNeNotEqualPasses()).isTrue();
    }

    @Test
    void shouldAssembleIfTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IfTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IfTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(14);

        final var obj = instantiate(clz, IfTests.class);

        // Tests IFxx (all variants)
        assertThat(obj.testEqWhenEqualPasses()).isTrue();
        assertThat(obj.testEqNotEqualPasses()).isTrue();
        assertThat(obj.testGeWhenLessPasses()).isTrue();
        assertThat(obj.testGeWhenEqualPasses()).isTrue();
        assertThat(obj.testGeWhenGreaterPasses()).isTrue();
        assertThat(obj.testGtWhenLessPasses()).isTrue();
        assertThat(obj.testGtWhenGreaterPasses()).isTrue();
        assertThat(obj.testLeWhenLessPasses()).isTrue();
        assertThat(obj.testLeWhenEqualPasses()).isTrue();
        assertThat(obj.testLeWhenGreaterPasses()).isTrue();
        assertThat(obj.testLtWhenLessPasses()).isTrue();
        assertThat(obj.testLtWhenGreaterPasses()).isTrue();
        assertThat(obj.testNeWhenEqualPasses()).isTrue();
        assertThat(obj.testNeNotEqualPasses()).isTrue();
    }

    @Test
    void shouldAssembleIfNullNonNullTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IfNullNonNullTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IfNullNonNullTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(4);

        final var obj = instantiate(clz, IfNullNonNullTest.class);

        // Tests IFNULL and IFNONNULL
        assertThat(obj.testIfNullWhenNullPasses()).isTrue();
        assertThat(obj.testIfNullWhenNonNullPasses()).isTrue();
        assertThat(obj.testIfNonNullWhenNullPasses()).isTrue();
        assertThat(obj.testIfNonNullWhenNonNullPasses()).isTrue();
    }

    @Test
    void shouldAssembleAthrowTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/AthrowTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.AthrowTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var obj = instantiate(clz, AthrowTest.class);
        final var exception = new Exception();

        assertThatThrownBy(() -> obj.sneakyThrow(exception))
                .isSameAs(exception);
    }

    @Test
    void shouldAssembleCheckcastTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/CheckcastTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.CheckcastTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var obj = instantiate(clz, CheckcastTest.class);
        final var list = new ArrayList<>();
        final var nonList = "";

        assertThat(obj.castToList(list)).isSameAs(list);

        assertThatThrownBy(() -> obj.castToList(nonList))
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void shouldAssembleInvokeDynamicToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/InvokeDynamicTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.InvokeDynamic");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(2);

        final var obj = instantiate(clz, InvokedynamicTest.class);

        assertThat(obj.doBasicInvokeDynamicTest()).isEqualTo("The expected basic result");
        assertThat(obj.doInvokeDynamicTest()).isEqualTo("The expected result");
    }

    @Test
    void shouldAssembleArrayTypesParamsValidJavaClass() throws NoSuchFieldException, NoSuchMethodException {
        final var clz = assembleAndDefine("com/roscopeco/jasm/ArrayTypesTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.ArrayTypesTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).hasSize(2);
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(2);

        final var arrayField = clz.getDeclaredField("arrayField");
        final var primArrayField = clz.getDeclaredField("primArrayField");
        final var oneArg = clz.getDeclaredMethod("arrayTypesTest", int[].class, String[][].class);
        final var threeArg = clz.getDeclaredMethod(
                "arrayTypesTestMultiple",
                String.class,
                String[].class,
                String[][].class
        );

        assertThat(arrayField).isNotNull();
        assertThat(arrayField.getType()).isEqualTo(Object[].class);

        assertThat(primArrayField).isNotNull();
        assertThat(primArrayField.getType()).isEqualTo(int[].class);

        // Probably don't actually need these, getDeclaredMethod would have failed if they weren't true,
        // but here for clarity and to prevent warnings...
        assertThat(oneArg).isNotNull();
        assertThat(oneArg.getParameterTypes()).containsExactly(int[].class, String[][].class);

        assertThat(threeArg).isNotNull();
        assertThat(threeArg.getParameterTypes()).containsExactly(String.class, String[].class, String[][].class);
    }

    @Test
    void shouldAssembleRefArrayTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/RefArrayTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.RefArrayTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(4);

        final var obj = instantiate(clz, RefArrayTests.class);
        final var ary = obj.newSingleElementArray();

        assertThat(ary).hasSize(1);
        assertThat(ary[0]).isNull();

        obj.putInArray(ary, "Testing");

        assertThat(ary[0]).isEqualTo("Testing");
        assertThat(obj.getFromArray(ary)).isEqualTo("Testing");

        assertThat(obj.getArrayLength(ary)).isEqualTo(1);
    }

    @Test
    void shouldAssembleLoadStoreTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/LoadsAndStoresTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.LoadsAndStoresTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var obj = instantiate(clz, LoadsAndStoresTest.class);
        assertThat(obj.testAloadAstore()).isEqualTo("Test String");
    }
}
