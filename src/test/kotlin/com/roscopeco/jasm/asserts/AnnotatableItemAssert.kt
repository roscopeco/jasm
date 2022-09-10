package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.AnnotationContext
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

abstract class AnnotatableItemAssert<Self, Actual>
    internal constructor(actual: Actual, self: Class<Self>, private val extractor: () -> List<AnnotationContext>) :
    AbstractAssert<AnnotatableItemAssert<Self, Actual>, Actual>(actual, self) {

    fun hasAnnotationNamed(name: String): AnnotationAssert<Self> {
        assertThat(extractor().filter { it.classname().text == name })
            .hasSize(1)

        return AnnotationAssert(extractor().filter { it.classname().text == name }[0], this as Self)
    }
}