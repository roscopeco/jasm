/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.asserts.LexerParserAssertions;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.roscopeco.jasm.TestUtil.doParse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserTests {
    @Test
    void shouldFailOnEmptyFile() {
        assertThatThrownBy(() -> doParse("emptyfile.jasm"))
                .isInstanceOf(ParseCancellationException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = { "EmptyClass", "ClassWithEmptyBody", "com/roscopeco/jasm/EmptyClassInPackage" })
    void shouldParseEmptyClasses(@NonNull final String testCase) {
        final var test  = doParse(testCase + ".jasm");

        LexerParserAssertions.assertClass(test).hasName(testCase);
        assertThat(test.member()).isEmpty();
    }

    @Test
    void shouldParsePublicFinalEmptyClass() {
        final var test  = doParse("PublicFinalEmptyClass.jasm");

        LexerParserAssertions.assertClass(test).hasName("PublicFinalEmptyClass");
        assertThat(test.member()).isEmpty();

        assertThat(test.modifier(0).PUBLIC()).isNotNull();
        assertThat(test.modifier(1).FINAL()).isNotNull();
    }

    @Test
    void shouldParseClassWithSingleField() {
        final var test  = doParse("ClassWithSingleField.jasm");

        LexerParserAssertions.assertClass(test).hasName("ClassWithSingleField");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        LexerParserAssertions.assertMember(member)
                .isNotNull()
                .isField()
                .hasName("someField")
                .isInteger();
    }

    @Test
    void shouldParseClassWithObjectField() {
        final var test  = doParse("ClassWithObjectField.jasm");

        LexerParserAssertions.assertClass(test).hasName("ClassWithObjectField");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        LexerParserAssertions.assertMember(member)
                .isNotNull()
                .isField()
                    .hasName("someField")
                    .isReference("Ljava/lang/Object;");
    }

    @Test
    void shouldParseClassWithMinimalMethod() {
        final var test  = doParse("com/roscopeco/jasm/MinimalMethodTest.jasm");

        LexerParserAssertions.assertClass(test).hasName("com/roscopeco/jasm/MinimalMethodTest");

        assertThat(test.member()).hasSize(1);
        final var member = test.member(0);

        LexerParserAssertions.assertMember(member)
                .isNotNull()
                .isMethod()
                    .hasName("testMethod")
                    .isVoid()
                    .hasNoArguments()
                    .hasCodeSequence()
                        .vreturn()
                        .noMoreCode();
    }

    @Test
    void shouldParseClassWithSuperclassAndInterfaces() {
        final var test  = doParse("com/roscopeco/jasm/InheritAndInterfaceTest.jasm");

        LexerParserAssertions.assertClass(test)
                .hasName("com/roscopeco/jasm/InheritAndInterfaceTest")
                .hasSuperclass("com/roscopeco/jasm/model/Superclass")
                .hasInterfaces("com/roscopeco/jasm/model/Interface1", "com/roscopeco/jasm/model/Interface2");
    }
}
