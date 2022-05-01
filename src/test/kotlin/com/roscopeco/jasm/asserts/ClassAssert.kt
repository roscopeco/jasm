/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser.ClassContext
import org.assertj.core.api.AbstractAssert
import org.antlr.v4.runtime.tree.TerminalNode

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat

class ClassAssert internal constructor(actual: ClassContext) :
    AbstractAssert<ClassAssert, ClassContext>(actual, ClassAssert::class.java) {

    fun hasName(name: String): ClassAssert {
        isNotNull
        
        if (name != actual.classname().text) {
            failWithMessage("Expected class to have name '" + name + "' but was '" + actual.classname().text + "'")
        }
        
        return this
    }

    fun hasSuperclass(qname: String): ClassAssert {
        isNotNull
        
        if (qname != actual.extends_().QNAME().text) {
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

        assertThat(actual.implements_().QNAME().stream().map { obj: TerminalNode -> obj.text })
            .`as`("Interface list")
            .containsExactly(*qnames)

        return this
    }
}