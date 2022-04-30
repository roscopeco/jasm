/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm;

import com.roscopeco.jasm.asserts.LexerParserAssertions;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.Test;

import static com.roscopeco.jasm.TestUtil.doParse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserTests {
    @Test
    void shouldFailOnEmptyFile() {
        assertThatThrownBy(() -> doParse("emptyfile.jasm"))
                .isInstanceOf(ParseCancellationException.class);
    }

    @Test
    void shouldParseEmptyClass() {
        final var test  = doParse("EmptyClass.jasm");

        LexerParserAssertions.assertClass(test).hasName("EmptyClass");
        assertThat(test.member()).isEmpty();
    }

    @Test
    void shouldParseClassWithEmptyBody() {
        final var test  = doParse("ClassWithEmptyBody.jasm");

        LexerParserAssertions.assertClass(test).hasName("ClassWithEmptyBody");
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
    void shouldParseEmptyClassInPackage() {
        final var test  = doParse("com/roscopeco/jasm/EmptyClassInPackage.jasm");

        LexerParserAssertions.assertClass(test).hasName("com/roscopeco/jasm/EmptyClassInPackage");
        assertThat(test.member()).isEmpty();
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
}
