/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.util.TraceClassVisitor;

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.TestUtil.testCaseParser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JasmIntegrationTests {

    private ByteArrayOutputStream baos;
    private JasmAssemblingVisitor assembler;

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                /* ************************************************************************************************ */
                Arguments.of("EmptyClass.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class EmptyClass {
                
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("ClassWithEmptyBody.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class ClassWithEmptyBody {
                
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("PublicFinalEmptyClass.jasm", """
                // class version 61.0 (61)
                // access flags 0x11
                public final class PublicFinalEmptyClass {
                
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("ClassWithSingleField.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class ClassWithSingleField {
                
                
                  // access flags 0x0
                  I someField
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/EmptyClassInPackage.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/EmptyClassInPackage {
                
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("ClassWithObjectField.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class ClassWithObjectField {
                
                
                  // access flags 0x0
                  Ljava/lang/Object; someField
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/MinimalMethodTest.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/MinimalMethodTest {
                
                
                  // access flags 0x0
                  testMethod()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Iconst.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Iconst {
                
                
                  // access flags 0x0
                  insnTest()V
                    ICONST_0
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Return.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Return {
                
                
                  // access flags 0x0
                  insnTest()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/IconstVariants.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/IconstVariants {
                
                
                  // access flags 0x9
                  public static insnTestM1()I
                    ICONST_M1
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                  
                  // access flags 0x9
                  public static insnTest0()I
                    ICONST_0
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                  
                  // access flags 0x9
                  public static insnTest1()I
                    ICONST_1
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x9
                  public static insnTest2()I
                    ICONST_2
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x9
                  public static insnTest3()I
                    ICONST_3
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x9
                  public static insnTest4()I
                    ICONST_4
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x9
                  public static insnTest5()I
                    ICONST_5
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/ConstructorMethodTest.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/ConstructorMethodTest {
                
                
                  // access flags 0x1
                  public <init>()V
                    ALOAD 0
                    INVOKESPECIAL java/lang/Object.<init> ()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x1
                  public testMethod()I
                    ICONST_M1
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Areturn.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Areturn {
                
                
                  // access flags 0x0
                  insnTest()Ljava/lang/Object;
                    ARETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Freturn.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Freturn {
                
                
                  // access flags 0x0
                  insnTest()F
                    FRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/AconstNull.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/AconstNull {
                
                
                  // access flags 0x0
                  insnTest()V
                    ACONST_NULL
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Ldc.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Ldc {
                
                
                  // access flags 0x0
                  insnTest()V
                    LDC 10
                    LDC 5.0
                    LDC "Test string"
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/InvokeTests.jasm", """
                // class version 61.0 (61)
                // access flags 0x1
                public class com/roscopeco/jasm/InvokeTests {
                
                
                  // access flags 0x1
                  public <init>()V
                    ALOAD 0
                    INVOKESPECIAL java/lang/Object.<init> ()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0xA
                  private static staticMethod()Ljava/lang/String;
                    LDC "Hello World"
                    ARETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x2
                  private addStrToList(Ljava/util/List;)Ljava/util/List;
                    ALOAD 1
                    INVOKESTATIC com/roscopeco/jasm/InvokeTests.staticMethod ()Ljava/lang/String;
                    INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z (itf)
                    ALOAD 1
                    ARETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                    
                  // access flags 0x1
                  public testMethod(Ljava/util/List;)Ljava/util/List;
                    ALOAD 0
                    ALOAD 1
                    INVOKEVIRTUAL com/roscopeco/jasm/InvokeTests.addStrToList (Ljava/util/List;)Ljava/util/List;
                    ALOAD 1
                    ARETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/Dup.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/Dup {
                
                
                  // access flags 0x0
                  insnTest()V
                    DUP
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/insntest/New.jasm", """
                // class version 61.0 (61)
                // access flags 0x0
                class com/roscopeco/jasm/insntest/New {
                
                
                  // access flags 0x0
                  insnTest()V
                    NEW java/util/ArrayList
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/GotoLabelTest.jasm", """
                // class version 61.0 (61)
                // access flags 0x1
                public class com/roscopeco/jasm/GotoLabelTest {
                
                
                  // access flags 0x9
                  public static testMethod(Ljava/util/List;)V
                    GOTO L0
                    ALOAD 0
                    LDC "CANARY"
                    INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z (itf)
                   L0
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/IfAcmpTests.jasm", """
                // class version 61.0 (61)
                // access flags 0x1
                public class com/roscopeco/jasm/IfAcmpTests {
                
                
                  // access flags 0x1
                  public <init>()V
                    ALOAD 0
                    INVOKESPECIAL java/lang/Object.<init> ()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                
                  // access flags 0x1
                  public testEqWhenEqualPasses()Z
                    NEW java/lang/String
                    DUP
                    IF_ACMPEQ L0
                    LDC 0
                    IRETURN
                   L0
                    LDC 1
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                
                  // access flags 0x1
                  public testEqNotEqualPasses()Z
                    NEW java/lang/String
                    NEW java/lang/String
                    IF_ACMPEQ L0
                    LDC 1
                    IRETURN
                   L0
                    LDC 0
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                
                  // access flags 0x1
                  public testNeWhenEqualPasses()Z
                    NEW java/lang/String
                    DUP
                    IF_ACMPNE L0
                    LDC 1
                    IRETURN
                   L0
                    LDC 0
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                
                  // access flags 0x1
                  public testNeNotEqualPasses()Z
                    NEW java/lang/String
                    NEW java/lang/String
                    IF_ACMPNE L0
                    LDC 0
                    IRETURN
                   L0
                    LDC 1
                    IRETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """),


                /* ************************************************************************************************ */
                Arguments.of("com/roscopeco/jasm/InheritAndInterfaceTest.jasm", """
                // class version 61.0 (61)
                // access flags 0x1
                public class com/roscopeco/jasm/InheritAndInterfaceTest extends com/roscopeco/jasm/model/Superclass implements com/roscopeco/jasm/model/Interface1 com/roscopeco/jasm/model/Interface2 {
                
                
                  // access flags 0x1
                  public <init>()V
                    ALOAD 0
                    INVOKESPECIAL com/roscopeco/jasm/model/Superclass.<init> ()V
                    RETURN
                    MAXSTACK = 0
                    MAXLOCALS = 0
                }
                """)
        );
    }

    @BeforeEach
    void setup() {
        this.baos = new ByteArrayOutputStream();
        this.assembler = new JasmAssemblingVisitor(new TraceClassVisitor(new PrintWriter(baos)), new Modifiers());
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void traceClassOutputShouldBeAsExpected(final String testCaseSource, final String expectedTraceClassOutput) {
        testCaseParser(testCaseSource).class_().accept(assembler);
        assertThat(baos).hasToString(expectedTraceClassOutput);
    }

    @Test
    void assemblyShouldFailOnUndeclaredLabel() {
        final var parsed = testCaseParser("com/roscopeco/jasm/ClassWithUndeclaredLabel.jasm").class_();
        assertThatThrownBy(() -> parsed.accept(assembler))
                .isInstanceOf(SyntaxErrorException.class)
                .hasMessageContaining("undeclaredLabel");
    }
}
