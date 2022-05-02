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

import static com.roscopeco.jasm.TestUtil.doParse;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertClass;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserTests {
    @Test
    void shouldFailOnEmptyFile() {
        assertThatThrownBy(() -> doParse("emptyfile.jasm"))
                .isInstanceOf(SyntaxErrorException.class)
                .hasMessageContaining("emptyfile.jasm")
                .hasMessageContaining("<EOF>");
    }

    @ParameterizedTest
    @ValueSource(strings = { "EmptyClass", "ClassWithEmptyBody", "com/roscopeco/jasm/EmptyClassInPackage" })
    void shouldParseEmptyClasses(@NonNull final String testCase) {
        final var test  = doParse(testCase + ".jasm");

        assertClass(test).hasName(testCase);
        assertThat(test.member()).isEmpty();
    }

    @Test
    void shouldParsePublicFinalEmptyClass() {
        final var test  = doParse("PublicFinalEmptyClass.jasm");

        assertClass(test).hasName("PublicFinalEmptyClass");
        assertThat(test.member()).isEmpty();

        assertThat(test.type_modifier(0).PUBLIC()).isNotNull();
        assertThat(test.type_modifier(1).FINAL()).isNotNull();
    }

    @Test
    void shouldParseClassWithSingleField() {
        final var test  = doParse("ClassWithSingleField.jasm");

        assertClass(test).hasName("ClassWithSingleField");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        assertMember(member)
                .isNotNull()
                .isField()
                .hasName("someField")
                .isInteger();
    }

    @Test
    void shouldParseClassWithObjectField() {
        final var test  = doParse("ClassWithObjectField.jasm");

        assertClass(test).hasName("ClassWithObjectField");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        assertMember(member)
                .isNotNull()
                .isField()
                    .hasName("someField")
                    .isReference("java/lang/Object");
    }

    @Test
    void shouldParseClassWithMinimalMethod() {
        final var test  = doParse("com/roscopeco/jasm/MinimalMethodTest.jasm");

        assertClass(test).hasName("com/roscopeco/jasm/MinimalMethodTest");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

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

        assertThat(test.member()).hasSize(4);

        assertMember(test.member(0))
                .isNotNull()
                .isMethod()
                .hasName("allPrims")
                .isVoid()
                .hasDescriptor("(BCDFIJSZ)V");

        assertMember(test.member(1))
                .isNotNull()
                .isMethod()
                .hasName("allPrimsLong")
                .isVoid()
                .hasDescriptor("(BCDFIJSZ)V");

        assertMember(test.member(2))
                .isNotNull()
                .isMethod()
                .hasName("allRefs")
                .isReference("java/util/List")
                .hasDescriptor("(Ljava/lang/String;Ljava/lang/Object;)Ljava/util/List;");

        assertMember(test.member(3))
                .isNotNull()
                .isMethod()
                .hasName("mixPrimsAndRefsLongAndShort")
                .isReference("java/util/List")
                .hasDescriptor("(IJLjava/lang/String;ZLjava/util/List;ZZ)Ljava/util/List;");
    }

    @Test
    void shouldParseClassWithSuperclassAndInterfaces() {
        final var test  = doParse("com/roscopeco/jasm/InheritAndInterfaceTest.jasm");

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

        assertThat(test.member()).hasSize(5);

        assertMember(test.member(0))
                .isField()
                .hasName("arrayField")
                .isReference("[java/lang/Object");

        assertMember(test.member(1))
                .isField()
                .hasName("primArrayField")
                .isPrimitiveArray("[I");

        assertMember(test.member(2))
                .isMethod()
                .hasName("arrayTypesTest")
                .hasDescriptor("([I[[Ljava/lang/String;)Ljava/lang/Object;");

        assertMember(test.member(3))
                .isMethod()
                .hasName("arrayTypesTestMultiple")
                .hasDescriptor("(Ljava/lang/String;[Ljava/lang/String;[[Ljava/lang/String;)Ljava/lang/Object;");

        // Member 4 is uninteresting constructor
    }
}
