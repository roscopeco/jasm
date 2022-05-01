/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import org.assertj.core.api.Assertions
import org.assertj.core.api.AbstractAssert
import com.roscopeco.jasm.antlr.JasmParser.Stat_blockContext
import com.roscopeco.jasm.antlr.JasmParser.InstructionContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokeinterfaceContext
import com.roscopeco.jasm.antlr.JasmParser.OwnerContext
import com.roscopeco.jasm.antlr.JasmParser.MembernameContext
import com.roscopeco.jasm.antlr.JasmParser.Method_descriptorContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokespecialContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokestaticContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokevirtualContext
import com.roscopeco.jasm.antlr.JasmParser.Const_argContext

class CodeSequenceAssert internal constructor(actual: Stat_blockContext, private val caller: MethodAssert) :
    AbstractAssert<CodeSequenceAssert, Stat_blockContext>(actual, CodeSequenceAssert::class.java) {

    private var pc = 0
    
    fun noMoreCode(): MethodAssert {
        if (pc != actual.stat().size) {
            failWithMessage(
                "Expected end of code reached at "
                        + pc
                        + ", but found "
                        + actual.stat()[pc].text
                        + " instead"
            )
        }
        return caller
    }

    fun aaload() = genericNoOperandCheck("aaload", InstructionContext::insn_aaload)

    fun aastore() = genericNoOperandCheck("aastore", InstructionContext::insn_aastore)

    fun aconstNull() = genericNoOperandCheck("aconst_null", InstructionContext::insn_aconst_null)

    fun aload(expected: Int) = genericIntOperandCheck("aload", expected, InstructionContext::insn_aload) {
            aload -> aload.int_atom().text
    }

    fun anew(expected: String) = genericStringOperandCheck("new", expected, InstructionContext::insn_new) {
            anew -> anew.QNAME().text
    }

    fun areturn() = genericNoOperandCheck("areturn", InstructionContext::insn_areturn)

    fun arraylength() = genericNoOperandCheck("arraylength", InstructionContext::insn_arraylength)

    fun astore(expected: Int) = genericIntOperandCheck("astore", expected, InstructionContext::insn_astore) {
            astore -> astore.int_atom().text
    }

    fun athrow() = genericNoOperandCheck("athrow", InstructionContext::insn_athrow)

    fun baload() = genericNoOperandCheck("baload", InstructionContext::insn_baload)
    fun bastore() = genericNoOperandCheck("bastore", InstructionContext::insn_bastore)

    fun bipush(expected: Int) = genericIntOperandCheck("bipush", expected, InstructionContext::insn_bipush) {
            bipush -> bipush.int_atom().text
    }

    fun caload() = genericNoOperandCheck("caload", InstructionContext::insn_caload)
    fun castore() = genericNoOperandCheck("castore", InstructionContext::insn_castore)

    fun checkcast(expected: String) = genericStringOperandCheck("checkcast", expected, InstructionContext:: insn_checkcast) {
            checkcast -> checkcast.QNAME().text
    }

    fun daload() = genericNoOperandCheck("daload", InstructionContext::insn_daload)
    fun dastore() = genericNoOperandCheck("dastore", InstructionContext::insn_dastore)
    
    fun dload(expected: Int) = genericIntOperandCheck("dload", expected, InstructionContext::insn_dload) {
            dload -> dload.int_atom().text
    }

    fun dreturn() = genericNoOperandCheck("dreturn", InstructionContext::insn_dreturn)

    fun dstore(expected: Int) = genericIntOperandCheck("dstore", expected, InstructionContext::insn_dstore) {
            dstore -> dstore.int_atom().text
    }
    
    fun dup() = genericNoOperandCheck("dup", InstructionContext::insn_dup)

    fun faload() = genericNoOperandCheck("faload", InstructionContext::insn_faload)
    fun fastore() = genericNoOperandCheck("fastore", InstructionContext::insn_fastore)

    fun fload(expected: Int) = genericIntOperandCheck("fload", expected, InstructionContext::insn_fload) {
            fload -> fload.int_atom().text
    }

    fun freturn() = genericNoOperandCheck("freturn", InstructionContext::insn_freturn)
    
    fun fstore(expected: Int) = genericIntOperandCheck("fstore", expected, InstructionContext::insn_fstore) {
            fstore -> fstore.int_atom().text
    }

    fun _goto(expected: String) = genericStringOperandCheck("goto", expected, InstructionContext::insn_goto) {
            _goto -> _goto.NAME().text
    }

    fun iaload() = genericNoOperandCheck("iaload", InstructionContext::insn_iaload)
    fun iastore() = genericNoOperandCheck("iastore", InstructionContext::insn_iastore)

    fun iconst(expected: Int) = genericIntOperandCheck("iconst", expected, InstructionContext::insn_iconst) {
            iconst -> iconst.atom().text
    }

    fun ifeq(expected: String) = genericStringOperandCheck("ifeq", expected, InstructionContext::insn_ifeq) {
            ifeq -> ifeq.NAME().text
    }

    fun ifge(expected: String) = genericStringOperandCheck("ifge", expected, InstructionContext::insn_ifge) {
            ifge -> ifge.NAME().text
    }

    fun ifgt(expected: String) = genericStringOperandCheck("ifgt", expected, InstructionContext::insn_ifgt) {
            ifgt -> ifgt.NAME().text
    }

    fun ifle(expected: String) = genericStringOperandCheck("ifle", expected, InstructionContext::insn_ifle) {
            ifle -> ifle.NAME().text
    }

    fun iflt(expected: String) = genericStringOperandCheck("iflt", expected, InstructionContext::insn_iflt) {
            iflt -> iflt.NAME().text }

    fun ifne(expected: String) = genericStringOperandCheck("ifeq", expected, InstructionContext::insn_ifne) {
            ifne -> ifne.NAME().text
    }

    fun if_acmpeq(expected: String) = genericStringOperandCheck("if_acmpeq", expected, InstructionContext::insn_if_acmpeq) {
            if_acmpeq -> if_acmpeq.NAME().text
    }

    fun if_acmpne(expected: String) = genericStringOperandCheck("if_acmpeq", expected, InstructionContext::insn_if_acmpne) {
            if_acmpne -> if_acmpne.NAME().text
    }

    fun if_icmpeq(expected: String) = genericStringOperandCheck("if_icmpeq", expected, InstructionContext::insn_if_icmpeq) {
            if_icmpeq -> if_icmpeq.NAME().text
    }

    fun if_icmpge(expected: String) = genericStringOperandCheck("if_icmpge", expected, InstructionContext::insn_if_icmpge) {
            if_icmpge -> if_icmpge.NAME().text
    }

    fun if_icmpgt(expected: String) = genericStringOperandCheck("if_icmpgt", expected, InstructionContext::insn_if_icmpgt) {
            if_icmpgt -> if_icmpgt.NAME().text
    }

    fun if_icmple(expected: String) = genericStringOperandCheck("if_icmple", expected, InstructionContext::insn_if_icmple) {
            if_icmple -> if_icmple.NAME().text
    }

    fun if_icmplt(expected: String) = genericStringOperandCheck("if_icmplt", expected, InstructionContext::insn_if_icmplt) {
            if_icmplt -> if_icmplt.NAME().text
    }

    fun if_icmpne(expected: String) = genericStringOperandCheck("if_icmpeq", expected, InstructionContext::insn_if_icmpne) {
            if_icmpne -> if_icmpne.NAME().text
    }

    fun ifNull(expected: String) = genericStringOperandCheck("ifnull", expected, InstructionContext::insn_ifnull) {
            ifnull -> ifnull.NAME().text
    }

    fun ifNonNull(expected: String) = genericStringOperandCheck(
        "ifnonnull",
        expected,
        InstructionContext::insn_ifnonnull) {
            ifnonnull -> ifnonnull.NAME().text
    }
    
    fun iload(expected: Int) = genericIntOperandCheck("iload", expected, InstructionContext::insn_iload) {
            iload -> iload.int_atom().text
    }

    fun invokeDynamic(
        name: String,
        descriptor: String
    ): CodeSequenceAssert {

        val failMessageSupplier = { insn: InstructionContext ->
            ("Expected invokedynamic "
                    + name + descriptor
                    + " instruction at pc("
                    + pc
                    + "), but was "
                    + insn.text)
        }

        isNotNull
        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed("invokedynamic")

        val stat = actual.stat()[pc]

        if (stat.instruction()?.insn_invokedynamic() == null) {
            failWithMessage(failMessageSupplier.invoke(stat.instruction()))
        }

        val insn = stat.instruction().insn_invokedynamic()
        val extractedName = insn.membername().text
        val extractedDesc = insn.method_descriptor().text

        if (name != extractedName) {
            failWithMessage(
                "${failMessageSupplier.invoke(stat.instruction())}\n  <name mismatch: '$name' vs '${extractedName}'>"
            )
        }
        if (descriptor != extractedDesc) {
            failWithMessage(
                "${failMessageSupplier.invoke(stat.instruction())}\n  <descriptor mismatch'$descriptor' vs '${extractedDesc}'>"
            )
        }

        pc++
        return this
    }

    fun invokeInterface(
        owner: String,
        name: String,
        descriptor: String
    ) = genericNonDynamicInvokeCheck(
            "invokeinterface",
            owner,
            name,
            descriptor,
            InstructionContext::insn_invokeinterface,
            Insn_invokeinterfaceContext::owner,
            Insn_invokeinterfaceContext::membername,
            Insn_invokeinterfaceContext::method_descriptor
        )

    fun invokeSpecial(
        owner: String,
        name: String,
        descriptor: String
    ) = genericNonDynamicInvokeCheck(
            "invokespecial",
            owner,
            name,
            descriptor,
            InstructionContext::insn_invokespecial,
            Insn_invokespecialContext::owner,
            Insn_invokespecialContext::membername,
            Insn_invokespecialContext::method_descriptor
        )

    fun invokeStatic(
        owner: String,
        name: String,
        descriptor: String
    ) = genericNonDynamicInvokeCheck(
            "invokestatic",
            owner,
            name,
            descriptor,
            InstructionContext::insn_invokestatic,
            Insn_invokestaticContext::owner,
            Insn_invokestaticContext::membername,
            Insn_invokestaticContext::method_descriptor
        )

    fun invokeVirtual(
        owner: String,
        name: String,
        descriptor: String
    ) = genericNonDynamicInvokeCheck(
            "invokevirtual",
            owner,
            name,
            descriptor,
            InstructionContext::insn_invokevirtual,
            Insn_invokevirtualContext::owner,
            Insn_invokevirtualContext::membername,
            Insn_invokevirtualContext::method_descriptor
        )

    fun ireturn() = genericNoOperandCheck("ireturn", InstructionContext::insn_ireturn)

    fun istore(expected: Int) = genericIntOperandCheck("istore", expected, InstructionContext::insn_istore) {
            istore -> istore.int_atom().text
    }

    fun laload() = genericNoOperandCheck("laload", InstructionContext::insn_laload)
    fun lastore() = genericNoOperandCheck("lastore", InstructionContext::insn_lastore)

    fun label(expected: String) = genericStringOperandCheck("label", expected, InstructionContext::label) {
            label -> label.text
    }

    fun ldc(expected: Boolean) = genericLdcCheck("" + expected) { constarg -> expected == constarg.text.toBoolean() }
    fun ldc(expected: Int) = genericLdcCheck("" + expected) { constarg -> expected == constarg.text.toInt() }
    fun ldc(expected: Float) = genericLdcCheck("" + expected) { constarg -> expected == constarg.text.toFloat() }
    fun ldcClass(expected: String) = genericLdcCheck(expected) {
            constarg -> expected == constarg.QNAME().text
    }
    fun ldcMethodType(expected: String) = genericLdcCheck('"' + expected + '"') {
            constarg -> expected == constarg.method_descriptor().text
    }
    fun ldcMethodHandle(expected: String) = genericLdcCheck('"' + expected + '"') {
            constarg -> expected == constarg.method_handle().text
    }
    fun ldcConstDynamic(expected: String) = genericLdcCheck('"' + expected + '"') {
            constarg -> expected == constarg.constdynamic().text
    }
    fun ldcStr(expected: String) = genericLdcCheck('"' + expected + '"') {
            constarg -> expected == cleanConstantString(constarg.string_atom().text)
    }

    fun lload(expected: Int) = genericIntOperandCheck("lload", expected, InstructionContext::insn_lload) {
            lload -> lload.int_atom().text
    }

    fun lreturn() = genericNoOperandCheck("lreturn", InstructionContext::insn_lreturn)

    fun lstore(expected: Int) = genericIntOperandCheck("lstore", expected, InstructionContext::insn_lstore) {
            lstore -> lstore.int_atom().text
    }

    fun vreturn(): CodeSequenceAssert {
        return genericNoOperandCheck("vreturn", InstructionContext::insn_return)
    }

    private fun cleanConstantString(constant: String): String {
        return constant.substring(1, constant.length - 1)
    }

    private fun hasNotUnderflowed(expected: String) {
        if (pc >= actual.stat().size) {
            failWithMessage(
                "Code underflowed at pc ("
                        + pc
                        + "): expected " + expected + " but end of code found instead"
            )
        }
    }

    private fun genericNoOperandCheck(
        name: String,
        getInsnFunc: (InstructionContext) -> Any?
    ): CodeSequenceAssert {
        isNotNull

        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed(name)
        val stat = actual.stat()[pc]

        if (stat.instruction() == null || getInsnFunc.invoke(stat.instruction()) == null) {
            failWithMessage(
                "Expected "
                        + name
                        + " instruction at pc("
                        + pc
                        + "), but was "
                        + stat.instruction().text
            )
        }

        pc++
        return this
    }

    private fun <T> genericIntOperandCheck(
        name: String,
        expectedOperand: Int,
        getInsnFunc: (InstructionContext) -> T,
        getAtomTextFunc:(T) -> String
    ) = genericAnyOperandCheck(name, expectedOperand, getInsnFunc, getAtomTextFunc) { i -> i.toString() }

    private fun <T> genericStringOperandCheck(
        name: String,
        expectedOperand: String,
        getInsnFunc: (InstructionContext) -> T,
        getAtomTextFunc: (T) -> String
    ) = genericAnyOperandCheck(name, expectedOperand, getInsnFunc, getAtomTextFunc) { it }

    private fun <T, O> genericAnyOperandCheck(
        name: String,
        expectedOperand: O,
        getInsnFunc: (InstructionContext) -> T,
        getAtomTextFunc: (T) -> String,
        getOperandTextFunc: (O) -> String
    ): CodeSequenceAssert {

        isNotNull
        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed(name)

        val stat = actual.stat()[pc]

        if (getInsnFunc.invoke(stat.instruction()) == null
            || getOperandTextFunc.invoke(expectedOperand) != getAtomTextFunc.invoke(getInsnFunc.invoke(stat.instruction()))) {
            failWithMessage(
                "Expected "
                        + name
                        + " "
                        + expectedOperand
                        + " instruction at pc("
                        + pc
                        + "), but was "
                        + stat.instruction().text
            )
        }

        pc++
        return this
    }

    private fun genericLdcCheck(
        expectedStr: String,
        atomPredicate: (Const_argContext) -> Boolean
    ): CodeSequenceAssert {

        isNotNull
        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed("ldc")

        val insn = actual.stat()[pc].instruction()

        if (insn?.insn_ldc()?.const_arg() == null || !atomPredicate.invoke(insn.insn_ldc().const_arg())) {
            failWithMessage(
                "Expected ldc("
                        + expectedStr
                        + ") instruction at pc("
                        + pc
                        + "), but was "
                        + (insn?.insn_ldc()?.text ?: "<Unknown>")
            )
        }

        pc++
        return this
    }

    private fun <T> genericNonDynamicInvokeCheck(
        invokeType: String,
        owner: String,
        name: String,
        descriptor: String,
        invokeExtractor: (InstructionContext) -> T,
        ownerExtractor: (T) -> OwnerContext,
        membernameExtractor: (T) -> MembernameContext,
        descriptorExtractor: (T) -> Method_descriptorContext
    ): CodeSequenceAssert {

        val failMessageSupplier = { insn: InstructionContext ->
            ("Expected "
                    + invokeType
                    + owner + "." + name + descriptor
                    + " instruction at pc("
                    + pc
                    + "), but was "
                    + insn.text)
        }

        isNotNull
        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed(invokeType)

        val stat = actual.stat()[pc]

        if (stat.instruction() == null || invokeExtractor.invoke(stat.instruction()) == null) {
            failWithMessage(failMessageSupplier.invoke(stat.instruction()))
        }

        val insn = invokeExtractor.invoke(stat.instruction())
        val extractedOwner = ownerExtractor.invoke(insn).text
        val extractedName = membernameExtractor.invoke(insn).text
        val extractedDesc = descriptorExtractor.invoke(insn).text

        if (owner != extractedOwner) {
            failWithMessage(
                "${failMessageSupplier.invoke(stat.instruction())}\n  <owner mismatch: '$owner' vs '${extractedOwner}'>"
            )
        }
        if (name != extractedName) {
            failWithMessage(
                "${failMessageSupplier.invoke(stat.instruction())}\n  <name mismatch: '$name' vs '${extractedName}'>"
            )
        }
        if (descriptor != extractedDesc) {
            failWithMessage(
                "${failMessageSupplier.invoke(stat.instruction())}\n  <descriptor mismatch'$descriptor' vs '${extractedDesc}'>"
            )
        }

        pc++
        return this
    }
}