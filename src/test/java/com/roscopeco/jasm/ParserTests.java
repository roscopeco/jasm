/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.objectweb.asm.Type;

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertClass;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserTests {
    @Test
    void shouldFailOnEmptyFile() {
        assertThatThrownBy(() -> doParse("emptyfile.jasm"))
            .isInstanceOf(ErrorForTestsException.class)
            .hasMessageContaining("emptyfile.jasm")
            .hasMessageContaining("<EOF>");
    }

    @ParameterizedTest
    @ValueSource(strings = {"EmptyClass", "com/roscopeco/jasm/EmptyClassInPackage"})
    void shouldParseEmptyClasses(@NonNull final String testCase) {
        final var test = doParse(testCase + ".jasm");

        assertClass(test).hasName(testCase);

        assertThat(test.classbody()).isNull();
    }

    @Test
    void shouldParseClassWithEmptyBody() {
        final var test = doParse("ClassWithEmptyBody.jasm");

        assertClass(test).hasName("ClassWithEmptyBody");

        assertThat(test.classbody().member()).isEmpty();
    }

    @Test
    void shouldParsePublicFinalEmptyClass() {
        final var test = doParse("PublicFinalEmptyClass.jasm");

        assertClass(test).hasName("PublicFinalEmptyClass");
        assertThat(test.classbody()).isNull();

        assertThat(test.type_modifier(0).PUBLIC()).isNotNull();
        assertThat(test.type_modifier(1).FINAL()).isNotNull();
    }

    @Test
    void shouldParseClassWithSingleField() {
        final var test = doParse("ClassWithSingleField.jasm");

        assertClass(test).hasName("ClassWithSingleField");

        assertThat(test.classbody().member()).hasSize(1);
        final var member = test.classbody().member(0);

        assertMember(member)
            .isNotNull()
            .isField()
            .hasName("someField")
            .isInteger();
    }

    @Test
    void shouldParseClassWithObjectField() {
        final var test = doParse("ClassWithObjectField.jasm");

        assertClass(test).hasName("ClassWithObjectField");

        assertThat(test.classbody().member()).hasSize(1);
        final var member = test.classbody().member(0);

        assertMember(member)
            .isNotNull()
            .isField()
            .hasName("someField")
            .isReference("java/lang/Object");
    }

    @Test
    void shouldParseClassWithMinimalMethod() {
        final var test = doParse("com/roscopeco/jasm/MinimalMethodTest.jasm");

        assertClass(test).hasName("com/roscopeco/jasm/MinimalMethodTest");

        assertThat(test.classbody().member()).hasSize(1);
        final var member = test.classbody().member(0);

        assertMember(member)
            .isNotNull()
            .isMethod()
            .hasName("testMethod")
            .isVoid()
            .hasCodeSequence()
            .vreturn()
            .noMoreCode();
    }

    @Test
    void shouldParseMethodArgumentsCorrectly() {
        final var test = doParse("MethodArgParsingTests.jasm");

        assertClass(test).hasName("MethodArgParsingTests");

        assertThat(test.classbody().member()).hasSize(4);

        assertMember(test.classbody().member(0))
            .isNotNull()
            .isMethod()
            .hasName("allPrims")
            .isVoid()
            .hasDescriptor("(BCDFIJSZ)V");

        assertMember(test.classbody().member(1))
            .isNotNull()
            .isMethod()
            .hasName("allPrimsLong")
            .isVoid()
            .hasDescriptor("(BCDFIJSZ)V");

        assertMember(test.classbody().member(2))
            .isNotNull()
            .isMethod()
            .hasName("allRefs")
            .isReference("java/util/List")
            .hasDescriptor("(Ljava/lang/String;Ljava/lang/Object;)Ljava/util/List;");

        assertMember(test.classbody().member(3))
            .isNotNull()
            .isMethod()
            .hasName("mixPrimsAndRefsLongAndShort")
            .isReference("java/util/List")
            .hasDescriptor("(IJLjava/lang/String;ZLjava/util/List;ZZ)Ljava/util/List;");
    }

    @Test
    void shouldParseClassWithSuperclassAndInterfaces() {
        final var test = doParse("com/roscopeco/jasm/InheritAndInterfaceTest.jasm");

        assertClass(test)
            .hasName("com/roscopeco/jasm/InheritAndInterfaceTest")
            .hasSuperclass("com/roscopeco/jasm/model/Superclass")
            .hasInterfaces("com/roscopeco/jasm/model/Interface1", "com/roscopeco/jasm/model/Interface2");
    }

    @Test
    void shouldParseClassWithArrayTypes() {
        final var test = doParse("com/roscopeco/jasm/ArrayTypesTest.jasm");

        assertClass(test)
            .hasName("com/roscopeco/jasm/ArrayTypesTest");

        assertThat(test.classbody().member()).hasSize(5);

        assertMember(test.classbody().member(0))
            .isField()
            .hasName("arrayField")
            .isReference("[java/lang/Object");

        assertMember(test.classbody().member(1))
            .isField()
            .hasName("primArrayField")
            .isPrimitiveArray("[I");

        assertMember(test.classbody().member(2))
            .isMethod()
            .hasName("arrayTypesTest")
            .hasDescriptor("([I[[Ljava/lang/String;)Ljava/lang/Object;");

        assertMember(test.classbody().member(3))
            .isMethod()
            .hasName("arrayTypesTestMultiple")
            .hasDescriptor("(Ljava/lang/String;[Ljava/lang/String;[[Ljava/lang/String;)Ljava/lang/Object;");

        // Member 4 is uninteresting constructor
    }

    @Test
    void shouldParseClassWithTryCatch() {
        final var test = doParse("com/roscopeco/jasm/TryCatchTest.jasm");

        assertClass(test)
            .hasName("com/roscopeco/jasm/TryCatchTest");

        assertThat(test.classbody().member()).hasSize(5);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("manualExceptionHandlerTest")
            .hasDescriptor("()Ljava/lang/Exception;")
            .hasCodeSequence()
                .exception("tryBegin", "tryEnd", "catchBegin", "java/lang/Exception")
            .label("tryBegin:")
                .anew("java/lang/Exception")
                .dup()
                .ldcStr("Pass")
                .invokeSpecial("java/lang/Exception", "<init>", "(Ljava/lang/String;)V")
                .athrow()
            .label("tryEnd:")
                .anew("java/lang/Exception")
                .dup()
                .ldcStr("Fail")
                .invokeSpecial("java/lang/Exception", "<init>", "(Ljava/lang/String;)V")
                .areturn()
            .label("catchBegin:")
                .checkcast("java/lang/Exception")
                .areturn()
                .noMoreCode();

        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("basicTryCatchTest")
            .hasDescriptor("()Ljava/lang/String;")
            .hasCodeSequence()
                .tryBlock()
                    .anew("java/lang/Exception")
                    .dup()
                    .invokeSpecial("java/lang/Exception", "<init>", "()V")
                    .athrow()
                    .noMoreCode()
                .catchBlock(0, "java/lang/Exception")
                    .ldcStr("Pass")
                    .areturn()
                    .noMoreCode()
                .ldcStr("Fail")
                .areturn()
                .noMoreCode();

        assertMember(test.classbody().member(2))
            .isMethod()
            .hasName("nestedTryCatchTest")
            .hasDescriptor("()I")
            .hasCodeSequence()
                .ldc(100)
                .istore(1)
                .tryBlock()
                    .tryBlock()
                        .anew("java/lang/Exception")
                        .dup()
                        .invokeSpecial("java/lang/Exception", "<init>", "()V")
                        .athrow()
                        .noMoreCode()
                    .catchBlock(0, "java/lang/Exception")
                        .iload(1)
                        .ldc(20)
                        .imul()
                        .istore(1)
                        .noMoreCode()
                    .anew("java/lang/RuntimeException")
                    .dup()
                    .invokeSpecial("java/lang/RuntimeException", "<init>", "()V")
                    .athrow()
                    .noMoreCode()
                .catchBlock(0, "java/lang/RuntimeException")
                    .iload(1)
                    .ldc(50)
                    .isub()
                    .istore(1)
                    .noMoreCode()
                .iload(1)
                .ireturn()
                .noMoreCode();

        assertMember(test.classbody().member(3))
            .isMethod()
            .hasName("tryMultipleCatchTest")
            .hasDescriptor("(Ljava/lang/Exception;)Ljava/lang/String;")
            .hasCodeSequence()
                .tryBlock()
                    .aload(1)
                    .athrow()
                    .noMoreCode()
                .catchBlock(0, "java/io/IOException")
                    .ldcStr("IOE")
                    .areturn()
                    .noMoreCode()
                .catchBlock(1, "java/lang/NullPointerException")
                    .ldcStr("NPE")
                    .areturn()
                    .noMoreCode()
                .catchBlock(2, "java/lang/Exception")
                    .ldcStr("EXCEPTION")
                    .areturn()
                    .noMoreCode()
                .ldcStr("Fail")
                .areturn()
                .noMoreCode();

        // Member 4 is uninteresting constructor
    }

    @Test
    void shouldParseLiteralNames() {
        // https://github.com/roscopeco/jasm/issues/35
        final var test = doParse("com/roscopeco/jasm/LiteralNames.jasm");

        assertClass(test)
            .hasName("com/roscopeco/jasm/Literal Names");

        assertThat(test.classbody().member()).hasSize(7);

        assertMember(test.classbody().member(0))
            .isField()
            .hasName("0");

        assertMember(test.classbody().member(1))
            .isField()
            .hasName("1");

        assertMember(test.classbody().member(2))
            .isMethod()
            .hasName("test1")
            .hasDescriptor("()Ljava/lang/String;")
            .hasCodeSequence()
                .getStatic("com/roscopeco/jasm/Literal Names", "0", "Ljava/lang/String;")
                .areturn();

        assertMember(test.classbody().member(3))
            .isMethod()
            .hasName("test2")
            .hasDescriptor("()Ljava/lang/String;")
            .hasCodeSequence()
                .aload(0)
                .getField("com/roscopeco/jasm/Literal Names", "1", "Ljava/lang/String;")
                .areturn();

        assertMember(test.classbody().member(4))
            .isMethod()
            .hasName("final native")
            .hasDescriptor("()I")
            .hasCodeSequence()
                ._goto("my label")
                .ldc(24)
                .ireturn()
                .label("my label:")
                .ldc(42)
                .ireturn();

        assertMember(test.classbody().member(5))
            .isMethod()
            .hasName("test3")
            .hasDescriptor("()I")
            .hasCodeSequence()
                .aload(0)
                .invokeVirtual("com/roscopeco/jasm/Literal Names", "final native", "()I", false)
                .ireturn();

        assertMember(test.classbody().member(6))
            .isMethod()
            .hasName("<init>")
            .hasDescriptor("(Ljava/lang/String;)V")
            .hasCodeSequence()
                .aload(0)
                .dup()
                .invokeSpecial("java/lang/Object", "<init>", "()V")
                .aload(1)
                .putField("com/roscopeco/jasm/Literal Names", "1", "Ljava/lang/String;")
                .vreturn();
    }

    @Test
    void shouldParseSimpleAnnotatedClass() {
        final var test = doParse("SimpleAnnotatedClass.jasm");

        assertClass(test)
            .hasName("SimpleAnnotatedClass")
            .hasAnnotationNamed("com/roscopeco/jasm/model/annotations/TestAnnotation");
    }

    @Test
    void shouldParseComplexAnnotatedClass() {
        final var test = doParse("ComplexAnnotatedClass.jasm");

        assertClass(test)
            .hasName("ComplexAnnotatedClass")
            .hasAnnotationNamed("com/roscopeco/jasm/model/annotations/TestAnnotation")
                .hasNamedParamWithValue("stringArg", "Yolo")
                .hasNamedParamWithValue("classArg", java.util.List.class)
                .hasNamedParamWithValue("arrayArg", new String[] { "one", "two" });
    }
}
