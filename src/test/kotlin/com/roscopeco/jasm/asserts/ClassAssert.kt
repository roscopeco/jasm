/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.LiteralNames
import com.roscopeco.jasm.antlr.JasmParser.AnnotationContext
import com.roscopeco.jasm.antlr.JasmParser.ClassContext
import org.assertj.core.api.AbstractAssert

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

class ClassAssert internal constructor(actual: ClassContext) :
    AnnotatableItemAssert<ClassAssert, ClassContext>(actual, ClassAssert::class.java, { actual.annotation() }) {

    fun hasName(name: String): ClassAssert {
        isNotNull
        
        if (name != LiteralNames.unescape(actual.classname().text)) {
            failWithMessage("Expected class to have name '" + name + "' but was '" + actual.classname().text + "'")
        }
        
        return this
    }

    fun hasSuperclass(qname: String): ClassAssert {
        isNotNull
        
        if (qname != actual.extends_().classname().QNAME().text) {
            failWithMessage(
                "Expected class to extend '"
                        + qname
                        + "' but was '"
                        + actual.extends_()?.text 
            )
        }
        
        return this
    }

    fun hasInterfaces(vararg qnames: String?): ClassAssert {
        isNotNull

        assertThat(actual.implements_().classname().stream().map { obj -> obj.QNAME().text })
            .`as`("Interface list")
            .containsExactly(*qnames)

        return this
    }

    fun isPublic(): ClassAssert {
        isNotNull

        assertThat(actual.type_modifier().map { it.text })
            .`as`("Modifier list")
            .contains("public")

        return this
    }

    fun isNotPublic(): ClassAssert {
        isNotNull

        assertThat(actual.type_modifier().map { it.text })
            .`as`("Modifier list")
            .doesNotContain("public")

        return this
    }

    fun isAbstract(): ClassAssert {
        isNotNull

        assertThat(actual.type_modifier().map { it.text })
            .`as`("Modifier list")
            .contains("abstract")

        return this
    }

    fun isInterface(): ClassAssert {
        isNotNull

        assertThat(actual.type_modifier().map { it.text })
            .`as`("Modifier list")
            .contains("interface")

        return this
    }

    fun isEnum(): ClassAssert {
        isNotNull

        assertThat(actual.type_modifier().map { it.text })
            .`as`("Modifier list")
            .contains("enum")

        return this
    }
}