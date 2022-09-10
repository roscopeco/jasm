package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.antlr.JasmParser
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.objectweb.asm.Type

class AnnotationAssert<Caller> internal constructor(actual: JasmParser.AnnotationContext, private val caller: Caller) :
    AbstractAssert<AnnotationAssert<Caller>, JasmParser.AnnotationContext>(actual, AnnotationAssert::class.java) {
    
    fun hasNamedParamWithValue(name: String, value: Any?): AnnotationAssert<Caller> {
        val matches = actual.annotation_param().filter { it.NAME()?.text == name || it.LITERAL_NAME()?.text == "`$name`" }
        
        assertThat(matches).hasSize(1)
        val match = matches[0].annotation_arg()
        
        when {
            match.int_atom() != null          -> assertThat(generateInteger(match.int_atom())).isEqualTo(value)
            match.float_atom() != null        -> assertThat(generateFloatingPoint(match.float_atom())).isEqualTo(value)
            match.string_atom() != null       -> assertThat(unescapeConstantString(match.string_atom().text)).isEqualTo(value)
            match.bool_atom() != null         -> assertThat(match.bool_atom().text.toBoolean()).isEqualTo(value as Boolean)
            match.NAME() != null              -> assertClassParam(value) { match.NAME().text }
            match.LITERAL_NAME() != null      -> assertClassParam(value) { match.LITERAL_NAME().text }
            match.QNAME() != null             -> assertClassParam(value) { match.QNAME().text }
            match.annotation_array_literal() != null -> {
                if (value is Array<*>) {
                    // TODO only String array supported at present
                    val actualArray = match.annotation_array_literal().annotation_arg().map {
                        it.string_atom().STRING().text.substring(1, it.string_atom().STRING().text.length - 1)
                    }

                    assertThat(actualArray).hasSameElementsAs((value as Array<String>).toList())
                } else {
                    fail<Any>("Expected ${value?.javaClass ?: "null"} but was an array: ${match.annotation_array_literal()}")
                }
            }
            else -> {
                if (value != null) {
                    fail<Any>("Expected value to be $value but it was null")
                }
            }
        }

        return this;
    }

    private fun assertClassParam(value: Any?, extractor: () -> String) {
        assertThat(value).isNotNull
        assertThat(value?.javaClass).isEqualTo(Class::class.java)
        assertThat(Type.getType("L" + extractor() + ";")).isEqualTo(Type.getType(value as Class<*>))
    }
    
    fun end(): Caller = caller

    private fun unescapeConstantString(constant: String) =
        constant.substring(1, constant.length - 1).replace("\"\"", "\"")

    private fun generateInteger(atom: JasmParser.Int_atomContext): Any = when {
        atom.INT() != null          -> atom.INT().text.toInt()
        atom.LONG() != null         -> atom.LONG().text.substring(0, atom.LONG().text.length - 1).toLong()
        else                        -> {
            /* should never happen! */
            fail<Any>("Invalid integer: ${atom.text}")
        }
    }

    private fun generateFloatingPoint(atom: JasmParser.Float_atomContext) = when {
        atom.FLOAT() != null        -> atom.FLOAT().text.toFloat()
        atom.DOUBLE() != null       -> atom.DOUBLE().text.substring(0, atom.DOUBLE().text.length - 1).toDouble()
        else                        -> {
            /* should never happen! */
            fail<Any>("Invalid float: ${atom.text}")
        }
    }
}