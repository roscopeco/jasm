package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.LiteralNames
import com.roscopeco.jasm.antlr.JasmParser.AnnotationContext
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat

abstract class AnnotatableItemAssert<Self, Actual>
    internal constructor(actual: Actual, self: Class<Self>, private val extractor: () -> List<AnnotationContext>) :
    AbstractAssert<AnnotatableItemAssert<Self, Actual>, Actual>(actual, self) {


    fun hasInvisibleAnnotationNamed(name: String): AnnotationAssert<Self> {
        val getActualName = { it: AnnotationContext -> LiteralNames.unescape((it.invisible_annotation().visible_annotation()).ANNOTATION_NAME().text.substring(1)) }

        assertThat(extractor()
            .filter { it.invisible_annotation() != null }
            .filter { getActualName(it) == name })
            .hasSize(1)

        return AnnotationAssert(extractor()
            .filter { it.invisible_annotation() != null }
            .filter { getActualName(it) == name }[0], this as Self)
    }
    fun hasAnnotationNamed(name: String): AnnotationAssert<Self> {
        val getActualName = { it: AnnotationContext -> LiteralNames.unescape((it.visible_annotation() ?: it.invisible_annotation().visible_annotation()).ANNOTATION_NAME().text.substring(1)) }

        assertThat(extractor().filter { getActualName(it) == name })
            .hasSize(1)

        return AnnotationAssert(extractor().filter { getActualName(it) == name }[0], this as Self)
    }
}