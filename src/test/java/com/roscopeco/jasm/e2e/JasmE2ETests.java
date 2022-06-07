/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.e2e;

import com.roscopeco.jasm.AssemblyException;
import com.roscopeco.jasm.model.AthrowTest;
import com.roscopeco.jasm.model.CheckcastTest;
import com.roscopeco.jasm.model.ConstFieldTests;
import com.roscopeco.jasm.model.DoubleMathTests;
import com.roscopeco.jasm.model.FloatMathTests;
import com.roscopeco.jasm.model.GetPutFieldTests;
import com.roscopeco.jasm.model.IfIcmpTests;
import com.roscopeco.jasm.model.IfTests;
import com.roscopeco.jasm.model.InstanceOfTest;
import com.roscopeco.jasm.model.IntMathTests;
import com.roscopeco.jasm.model.Interface1;
import com.roscopeco.jasm.model.Interface2;
import com.roscopeco.jasm.model.IfNullNonNullTest;
import com.roscopeco.jasm.model.InvokedynamicTest;
import com.roscopeco.jasm.model.JsrRetTest;
import com.roscopeco.jasm.model.LdcAconstAreturn;
import com.roscopeco.jasm.model.LoadsAndStoresTest;
import com.roscopeco.jasm.model.LongMathTests;
import com.roscopeco.jasm.model.StackOpsTest;
import com.roscopeco.jasm.model.PrimArrayTests;
import com.roscopeco.jasm.model.RefArrayTests;
import com.roscopeco.jasm.model.Superclass;
import com.roscopeco.jasm.model.SwitchTests;
import com.roscopeco.jasm.model.TryCatchTest;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.roscopeco.jasm.TestUtil.assemble;
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

        assertThat(objectArgsInvoker(obj, "testMethod", new Class[]{List.class}).apply(new Object[]{list}))
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

        final var list = (ArrayList<?>) obj;

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
        objectArgsInvoker(clz, "testMethod", List.class).apply(new Object[]{list});

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
        assertThat(clz.getDeclaredMethods()).hasSize(6);

        final var obj = instantiate(clz, RefArrayTests.class);
        final var ary = obj.newSingleElementArray();

        assertThat(ary).hasSize(1);
        assertThat(ary[0]).isNull();

        obj.putInArray(ary, "Testing");

        assertThat(ary[0]).isEqualTo("Testing");
        assertThat(obj.getFromArray(ary)).isEqualTo("Testing");

        assertThat(obj.getArrayLength(ary)).isEqualTo(1);

        assertThat(obj.multiANewArrayTest()).hasSameDimensionsAs(new String[2][1]);
        assertThat(obj.multiANewArrayExplicitDimsTest()).hasSameDimensionsAs(new String[2][1]);
    }

    @Test
    void shouldAssembleLoadStoreTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/LoadsAndStoresTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.LoadsAndStoresTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(5);

        final var obj = instantiate(clz, LoadsAndStoresTest.class);
        assertThat(obj.testAloadAstore()).isEqualTo("Test String");
        assertThat(obj.testDloadDstore(42d)).isEqualTo(42d);
        assertThat(obj.testFloadFstore(42f)).isEqualTo(42f);
        assertThat(obj.testIloadIstore(42)).isEqualTo(42);
        assertThat(obj.testLloadLstore(42L)).isEqualTo(42L);
    }

    @Test
    void shouldAssemblePrimArrayTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/PrimArrayTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.PrimArrayTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(15);

        final var obj = instantiate(clz, PrimArrayTests.class);

        final var bary = new byte[1];
        obj.testBipushBastore(bary);
        assertThat(bary[0]).isEqualTo((byte) 42);
        assertThat(obj.testBaload(bary)).isEqualTo((byte) 42);

        final var cary = new char[1];
        obj.testCastore(cary);
        assertThat(cary[0]).isEqualTo((char) 42);
        assertThat(obj.testCaload(cary)).isEqualTo((char) 42);

        final var dary = new double[1];
        obj.testDastore(dary, 42.0d);
        assertThat(dary[0]).isEqualTo(42.0d);
        assertThat(obj.testDaload(dary)).isEqualTo(42.0d);

        final var fary = new float[1];
        obj.testFastore(fary, 42.0f);
        assertThat(fary[0]).isEqualTo(42.0f);
        assertThat(obj.testFaload(fary)).isEqualTo(42.0f);

        final var iary = new int[1];
        obj.testIastore(iary, 42);
        assertThat(iary[0]).isEqualTo(42);
        assertThat(obj.testIaload(iary)).isEqualTo(42);

        final var lary = new long[1];
        obj.testLastore(lary, 42L);
        assertThat(lary[0]).isEqualTo(42L);
        assertThat(obj.testLaload(lary)).isEqualTo(42L);

        final var sary = new short[1];
        obj.testSastore(sary, (short)42);
        assertThat(sary[0]).isEqualTo((short)42);
        assertThat(obj.testSaload(sary)).isEqualTo((short)42);

        assertThat(obj.testNewarray(10)).hasSize(10);
    }

    @Test
    void shouldAssembleDoubleMathTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/DoubleMathTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.DoubleMathTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(13);

        final var obj = instantiate(clz, DoubleMathTests.class);

        assertThat(obj.testD2f(10.0d)).isEqualTo(10.0f);
        assertThat(obj.testD2i(10.0d)).isEqualTo(10);
        assertThat(obj.testD2l(10.0d)).isEqualTo(10);

        assertThat(obj.testDconst0()).isEqualTo(0.0d);
        assertThat(obj.testDconst1()).isEqualTo(1.0d);

        assertThat(obj.testDcmpg(10d, 1d)).isEqualTo(1);
        assertThat(obj.testDcmpg(1d, 10d)).isEqualTo(-1);
        assertThat(obj.testDcmpg(1d, 1d)).isEqualTo(0);
        assertThat(obj.testDcmpg(Double.NaN, 1d)).isEqualTo(1);

        assertThat(obj.testDcmpl(10d, 1d)).isEqualTo(1);
        assertThat(obj.testDcmpl(1d, 10d)).isEqualTo(-1);
        assertThat(obj.testDcmpl(1d, 1d)).isEqualTo(0);
        assertThat(obj.testDcmpl(Double.NaN, 1d)).isEqualTo(-1);

        assertThat(obj.testDadd(10d, 5d)).isEqualTo(15.0d);
        assertThat(obj.testDdiv(10d, 2d)).isEqualTo(5d);
        assertThat(obj.testDmul(10d, 2d)).isEqualTo(20d);
        assertThat(obj.testDneg(10d)).isEqualTo(-10d);
        assertThat(obj.testDrem(10d, 3d)).isEqualTo(1d);
        assertThat(obj.testDsub(10d, 5d)).isEqualTo(5d);
    }

    @Test
    void shouldAssembleInstructionsAsMemberNamesToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/InstructionsAsNames.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.InstructionsAsNames");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).hasSize(2);
        assertThat(clz.getDeclaredConstructors()).isEmpty();
        assertThat(clz.getDeclaredMethods()).hasSize(3);
    }

    @Test
    void shouldAssembleFloatMathTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/FloatMathTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.FloatMathTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(13);

        final var obj = instantiate(clz, FloatMathTests.class);

        assertThat(obj.testF2d(10.0f)).isEqualTo(10.0d);
        assertThat(obj.testF2i(10.0f)).isEqualTo(10);
        assertThat(obj.testF2l(10.0f)).isEqualTo(10);

        assertThat(obj.testFconst0()).isEqualTo(0.0f);
        assertThat(obj.testFconst1()).isEqualTo(1.0f);

        assertThat(obj.testFcmpg(10f, 1f)).isEqualTo(1);
        assertThat(obj.testFcmpg(1f, 10f)).isEqualTo(-1);
        assertThat(obj.testFcmpg(1f, 1f)).isEqualTo(0);
        assertThat(obj.testFcmpg(Float.NaN, 1f)).isEqualTo(1);

        assertThat(obj.testFcmpl(10f, 1f)).isEqualTo(1);
        assertThat(obj.testFcmpl(1f, 10f)).isEqualTo(-1);
        assertThat(obj.testFcmpl(1f, 1f)).isEqualTo(0);
        assertThat(obj.testFcmpl(Float.NaN, 1f)).isEqualTo(-1);

        assertThat(obj.testFadd(10f, 5f)).isEqualTo(15.0f);
        assertThat(obj.testFdiv(10f, 2f)).isEqualTo(5f);
        assertThat(obj.testFmul(10f, 2f)).isEqualTo(20f);
        assertThat(obj.testFneg(10f)).isEqualTo(-10f);
        assertThat(obj.testFrem(10f, 3f)).isEqualTo(1f);
        assertThat(obj.testFsub(10f, 5f)).isEqualTo(5f);
    }

    @Test
    void shouldAssembleGetPutFieldTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/GetPutFieldTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.GetPutFieldTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).hasSize(3);
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(3);

        final var obj = instantiate(clz, GetPutFieldTests.class);

        assertThat(obj.getConstField()).isEqualTo("Const value from <clinit>");
        assertThat(obj.getRefField()).isEqualTo("Ref Field");
        assertThat(obj.getPrimField()).isEqualTo(42);
    }

    @Test
    void shouldAssembleConstFieldTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/ConstFieldTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.ConstFieldTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).hasSize(3);
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(3);

        final var obj = instantiate(clz, ConstFieldTests.class);

        assertThat(obj.getConstString()).isEqualTo("Constant String");
        assertThat(obj.getConstInt()).isEqualTo(10);
        assertThat(obj.getConstFloat()).isEqualTo(42.0f);
    }

    @Test
    void shouldAssembleIntMathTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/IntMathTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.IntMathTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(19);

        final var obj = instantiate(clz, IntMathTests.class);

        assertThat(obj.testI2b(10)).isEqualTo((byte)10);
        assertThat(obj.testI2b(256)).isEqualTo((byte)0);
        assertThat(obj.testI2c(32)).isEqualTo((char)32);
        assertThat(obj.testI2d(1001)).isEqualTo(1001d);
        assertThat(obj.testI2f(1001)).isEqualTo(1001f);
        assertThat(obj.testI2l(Integer.MAX_VALUE)).isEqualTo(Integer.MAX_VALUE);
        assertThat(obj.testI2s(Integer.MAX_VALUE)).isEqualTo((short)-1);

        assertThat(obj.testIadd(10, 20)).isEqualTo(30);
        assertThat(obj.testIand(0x6, 0x4)).isEqualTo(0x4);
        assertThat(obj.testIdiv(20, 10)).isEqualTo(2);
        assertThat(obj.testIinc(10)).isEqualTo(52);
        assertThat(obj.testImul(10, 20)).isEqualTo(200);
        assertThat(obj.testIneg(10)).isEqualTo(-10);
        assertThat(obj.testIor(0x2, 0x4)).isEqualTo(0x6);
        assertThat(obj.testIrem(20, 15)).isEqualTo(5);
        assertThat(obj.testIshl(0x1, 0x2)).isEqualTo(0x4);
        assertThat(obj.testIshr(0x10, 0x2)).isEqualTo(0x4);
        assertThat(obj.testIsub(20, 10)).isEqualTo(10);
        assertThat(obj.testIxor(0x7, 0x5)).isEqualTo(0x2);

        assertThat(obj.testIshr(-16, 2)).isEqualTo(-4);
        assertThat(obj.testIushr(-16, 2)).isEqualTo(1073741820);
    }

    @Test
    void shouldAssembleInstanceOfTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/InstanceOfTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.InstanceOfTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var obj = instantiate(clz, InstanceOfTest.class);

        assertThat(obj.isList("Test")).isFalse();
        assertThat(obj.isList(new ArrayList<>())).isTrue();
    }

    @Test
    void shouldAssembleSwitchTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/SwitchTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.SwitchTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(2);

        final var obj = instantiate(clz, SwitchTests.class);

        assertThat(obj.testLookupswitch(0)).isEqualTo("No match");
        assertThat(obj.testLookupswitch(1)).isEqualTo("One");
        assertThat(obj.testLookupswitch(2)).isEqualTo("No match");
        assertThat(obj.testLookupswitch(99)).isEqualTo("No match");
        assertThat(obj.testLookupswitch(100)).isEqualTo("Hundred");
        assertThat(obj.testLookupswitch(101)).isEqualTo("No match");
        assertThat(obj.testLookupswitch(999)).isEqualTo("No match");
        assertThat(obj.testLookupswitch(1000)).isEqualTo("Thousand");
        assertThat(obj.testLookupswitch(1001)).isEqualTo("No match");

        assertThat(obj.testTableswitch(0)).isEqualTo("No match");
        assertThat(obj.testTableswitch(1)).isEqualTo("One");
        assertThat(obj.testTableswitch(2)).isEqualTo("Two");
        assertThat(obj.testTableswitch(3)).isEqualTo("No match");
        assertThat(obj.testTableswitch(4)).isEqualTo("No match");
        assertThat(obj.testTableswitch(5)).isEqualTo("Five");
    }

    @Test
    void shouldAssembleLongMathTestsToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/LongMathTests.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.LongMathTests");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(16);

        final var obj = instantiate(clz, LongMathTests.class);

        assertThat(obj.testL2d(1001)).isEqualTo(1001d);
        assertThat(obj.testL2f(1001)).isEqualTo(1001f);
        assertThat(obj.testL2i(Integer.MAX_VALUE)).isEqualTo(Integer.MAX_VALUE);
        assertThat(obj.testLadd(10, 20)).isEqualTo(30);
        assertThat(obj.testLand(0x6, 0x4)).isEqualTo(0x4);
        assertThat(obj.testLcmp(10, 1)).isEqualTo(1);
        assertThat(obj.testLcmp(1, 10)).isEqualTo(-1);
        assertThat(obj.testLcmp(1, 1)).isEqualTo(0);
        assertThat(obj.testLdiv(20, 10)).isEqualTo(2);
        assertThat(obj.testLmul(10, 20)).isEqualTo(200);
        assertThat(obj.testLneg(10)).isEqualTo(-10);
        assertThat(obj.testLor(0x2, 0x4)).isEqualTo(0x6);
        assertThat(obj.testLrem(20, 15)).isEqualTo(5);
        assertThat(obj.testLshl(0x1, 0x2)).isEqualTo(0x4);
        assertThat(obj.testLshr(0x10, 0x2)).isEqualTo(0x4);
        assertThat(obj.testLsub(20, 10)).isEqualTo(10);
        assertThat(obj.testLxor(0x7, 0x5)).isEqualTo(0x2);
        assertThat(obj.testLshr(-16, 2)).isEqualTo(-4);
        assertThat(obj.testLushr(-16, 2)).isEqualTo(4611686018427387900L);
    }

    @Test
    void shouldAssembleStackOpsTestToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/StackOpsTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.StackOpsTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(3);

        final var obj = instantiate(clz, StackOpsTest.class);

        assertThat(obj.testPop()).isEqualTo(1);
        assertThat(obj.testPop2()).isEqualTo(1);
        assertThat(obj.testSwap()).isEqualTo(1);
    }

    @Test
    void shouldAssembleJsrRetTestToValidJavaClass() {
        // Not supported with stack frames (format 50, Java 1.6), so generate a class for that 1.5 for this test...
        final var clz = assembleAndDefine("com/roscopeco/jasm/JsrRetTest.jasm", Opcodes.V1_5);

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.JsrRetTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(1);

        final var obj = instantiate(clz, JsrRetTest.class);

        assertThat(obj.testJsrAndRet()).isEqualTo(2);
    }

    @Test
    void shouldCollectMultipleErrors() {
        assertThatThrownBy(() -> assembleAndDefine("com/roscopeco/jasm/ClassWithMultipleErrors.jasm"))
            .isInstanceOf(AssemblyException.class)
            .hasMessageContaining("ClassWithMultipleErrors.jasm:[4:22]")
            .hasMessageContaining("mismatched input '('")
            .hasMessageContaining("ClassWithMultipleErrors.jasm:[2:4]")
            .hasMessageContaining("Field afield cannot have void type");
    }

    @Test
    void shouldAssembleTryCatchToValidJavaClass() {
        final var clz = assembleAndDefine("com/roscopeco/jasm/TryCatchTest.jasm");

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm.TryCatchTest");

        assertThat(clz.getDeclaredClasses()).isEmpty();
        assertThat(clz.getDeclaredFields()).isEmpty();
        assertThat(clz.getDeclaredConstructors()).hasSize(1);
        assertThat(clz.getDeclaredMethods()).hasSize(4);

        final var obj = instantiate(clz, TryCatchTest.class);

        assertThat(obj.manualExceptionHandlerTest()).hasMessageContaining("Pass");
        assertThat(obj.basicTryCatchTest()).isEqualTo("Pass");
        assertThat(obj.nestedTryCatchTest()).isEqualTo(1950);

        assertThat(obj.tryMultipleCatchTest(new IOException())).isEqualTo("IOE");
        assertThat(obj.tryMultipleCatchTest(new NullPointerException())).isEqualTo("NPE");
        assertThat(obj.tryMultipleCatchTest(new Exception())).isEqualTo("EXCEPTION");
    }

    @Test
    void shouldFailNicelyOnGetfieldWithMissingType() {
        assertThatThrownBy(() -> assemble("BadGetFieldTest.jasm", Opcodes.V11))
            .isInstanceOf(AssemblyException.class)
            .hasMessageContaining("mismatched input 'ldc'");
    }
}
