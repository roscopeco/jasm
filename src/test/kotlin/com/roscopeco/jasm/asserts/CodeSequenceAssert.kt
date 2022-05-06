/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm.asserts

import com.roscopeco.jasm.TypeVisitor
import com.roscopeco.jasm.antlr.JasmParser
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

    fun anewarray(expected: String) = genericStringOperandCheck("anewarray", expected, InstructionContext::insn_anewarray) {
            anewarray -> anewarray.QNAME().text
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

    fun d2f() = genericNoOperandCheck("d2f", InstructionContext::insn_d2f)

    fun d2i() = genericNoOperandCheck("d2i", InstructionContext::insn_d2i)

    fun d2l() = genericNoOperandCheck("d2l", InstructionContext::insn_d2l)

    fun dadd() = genericNoOperandCheck("dadd", InstructionContext::insn_dadd)

    fun daload() = genericNoOperandCheck("daload", InstructionContext::insn_daload)

    fun dastore() = genericNoOperandCheck("dastore", InstructionContext::insn_dastore)

    fun dcmpg() = genericNoOperandCheck("dcmpg", InstructionContext::insn_dcmpg)

    fun dcmpl() = genericNoOperandCheck("dcmpl", InstructionContext::insn_dcmpl)

    fun dconst(expected: Int) = genericIntOperandCheck("dconst", expected, InstructionContext::insn_dconst) {
            dconst -> dconst.int_atom().text
    }

    fun ddiv() = genericNoOperandCheck("ddiv", InstructionContext::insn_ddiv)

    fun dload(expected: Int) = genericIntOperandCheck("dload", expected, InstructionContext::insn_dload) {
            dload -> dload.int_atom().text
    }

    fun dmul() = genericNoOperandCheck("dmul", InstructionContext::insn_dmul)

    fun dneg() = genericNoOperandCheck("dneg", InstructionContext::insn_dneg)

    fun drem() = genericNoOperandCheck("drem", InstructionContext::insn_drem)

    fun dreturn() = genericNoOperandCheck("dreturn", InstructionContext::insn_dreturn)

    fun dstore(expected: Int) = genericIntOperandCheck("dstore", expected, InstructionContext::insn_dstore) {
            dstore -> dstore.int_atom().text
    }

    fun dsub() = genericNoOperandCheck("dsub", InstructionContext::insn_dsub)

    fun dup() = genericNoOperandCheck("dup", InstructionContext::insn_dup)

    fun dupX1() = genericNoOperandCheck("dup_x1", InstructionContext::insn_dup_x1)

    fun dupX2() = genericNoOperandCheck("dup_x2", InstructionContext::insn_dup_x2)

    fun dup2() = genericNoOperandCheck("dup2", InstructionContext::insn_dup2)

    fun dup2X1() = genericNoOperandCheck("dup2_x1", InstructionContext::insn_dup2_x1)

    fun dup2X2() = genericNoOperandCheck("dup2_x2", InstructionContext::insn_dup2_x2)

    fun f2d() = genericNoOperandCheck("f2d", InstructionContext::insn_f2d)

    fun f2i() = genericNoOperandCheck("f2i", InstructionContext::insn_f2i)

    fun f2l() = genericNoOperandCheck("f2l", InstructionContext::insn_f2l)

    fun fadd() = genericNoOperandCheck("fadd", InstructionContext::insn_fadd)
    
    fun faload() = genericNoOperandCheck("faload", InstructionContext::insn_faload)
    
    fun fastore() = genericNoOperandCheck("fastore", InstructionContext::insn_fastore)

    fun fcmpg() = genericNoOperandCheck("fcmpg", InstructionContext::insn_fcmpg)

    fun fcmpl() = genericNoOperandCheck("fcmpl", InstructionContext::insn_fcmpl)

    fun fconst(expected: Int) = genericIntOperandCheck("fconst", expected, InstructionContext::insn_fconst) {
            fconst -> fconst.int_atom().text
    }

    fun fdiv() = genericNoOperandCheck("fdiv", InstructionContext::insn_fdiv)

    fun fload(expected: Int) = genericIntOperandCheck("fload", expected, InstructionContext::insn_fload) {
            fload -> fload.int_atom().text
    }

    fun fmul() = genericNoOperandCheck("fmul", InstructionContext::insn_fmul)

    fun fneg() = genericNoOperandCheck("fneg", InstructionContext::insn_fneg)

    fun frem() = genericNoOperandCheck("frem", InstructionContext::insn_frem)

    fun freturn() = genericNoOperandCheck("freturn", InstructionContext::insn_freturn)

    fun fsub() = genericNoOperandCheck("fsub", InstructionContext::insn_fsub)

    fun fstore(expected: Int) = genericIntOperandCheck("fstore", expected, InstructionContext::insn_fstore) {
            fstore -> fstore.int_atom().text
    }

    fun getField(expectedOwner: String, expectedName: String, expectedDescriptor: String) =
        genericFieldAccessCheck(
            "getfield",
            expectedOwner,
            expectedName,
            expectedDescriptor,
            InstructionContext::insn_getfield,
            JasmParser.Insn_getfieldContext::owner,
            JasmParser.Insn_getfieldContext::membername,
            JasmParser.Insn_getfieldContext::type
        )

    fun getStatic(expectedOwner: String, expectedName: String, expectedDescriptor: String) =
        genericFieldAccessCheck(
            "getstatic",
            expectedOwner,
            expectedName,
            expectedDescriptor,
            InstructionContext::insn_getstatic,
            JasmParser.Insn_getstaticContext::owner,
            JasmParser.Insn_getstaticContext::membername,
            JasmParser.Insn_getstaticContext::type
        )

    fun _goto(expected: String) = genericStringOperandCheck("goto", expected, InstructionContext::insn_goto) {
            _goto -> _goto.NAME().text
    }

    fun i2b() = genericNoOperandCheck("i2b", InstructionContext::insn_i2b)

    fun i2c() = genericNoOperandCheck("i2c", InstructionContext::insn_i2c)

    fun i2d() = genericNoOperandCheck("i2d", InstructionContext::insn_i2d)

    fun i2f() = genericNoOperandCheck("i2f", InstructionContext::insn_i2f)

    fun i2l() = genericNoOperandCheck("i2l", InstructionContext::insn_i2l)

    fun i2s() = genericNoOperandCheck("i2s", InstructionContext::insn_i2s)

    fun iadd() = genericNoOperandCheck("iadd", InstructionContext::insn_iadd)
    
    fun iaload() = genericNoOperandCheck("iaload", InstructionContext::insn_iaload)

    fun iand() = genericNoOperandCheck("iand", InstructionContext::insn_iand)

    fun iastore() = genericNoOperandCheck("iastore", InstructionContext::insn_iastore)

    fun iconst(expected: Int) = genericIntOperandCheck("iconst", expected, InstructionContext::insn_iconst) {
            iconst -> iconst.ilconst_atom().text
    }

    fun idiv() = genericNoOperandCheck("idiv", InstructionContext::insn_idiv)

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

    fun iinc(expectedVarNum: Int, expectedAmount: Int): CodeSequenceAssert {
        isNotNull

        val insn = actual.stat()[pc]?.instruction()?.insn_iinc()
        val actualVarNum = insn?.int_atom(0)?.text?.toInt()
        val actualAmount = insn?.int_atom(1)?.text?.toInt()

        if (actualVarNum != expectedVarNum || actualAmount != expectedAmount) {
            failWithMessage(
                "Expected iinc instruction at pc($pc) with slot number $expectedVarNum"
                        + " and amount $expectedAmount, but was ${insn?.text ?: "<NONE>"}"
            )
        }

        pc++
        return this
    }

    fun iload(expected: Int) = genericIntOperandCheck("iload", expected, InstructionContext::insn_iload) {
            iload -> iload.int_atom().text
    }

    fun imul() = genericNoOperandCheck("imul", InstructionContext::insn_imul)

    fun ineg() = genericNoOperandCheck("ineg", InstructionContext::insn_ineg)

    fun instance_of(expected: String) = genericStringOperandCheck("iload", expected, InstructionContext::insn_instanceof) {
            instof -> instof.QNAME().text
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
        val extractedDesc = TypeVisitor().visitMethod_descriptor(insn.method_descriptor())

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

    fun ior() = genericNoOperandCheck("ior", InstructionContext::insn_ior)

    fun irem() = genericNoOperandCheck("irem", InstructionContext::insn_irem)

    fun ireturn() = genericNoOperandCheck("ireturn", InstructionContext::insn_ireturn)

    fun ishl() = genericNoOperandCheck("ishl", InstructionContext::insn_ishl)

    fun ishr() = genericNoOperandCheck("ishr", InstructionContext::insn_ishr)

    fun istore(expected: Int) = genericIntOperandCheck("istore", expected, InstructionContext::insn_istore) {
            istore -> istore.int_atom().text
    }

    fun isub() = genericNoOperandCheck("isub", InstructionContext::insn_isub)

    fun iushr() = genericNoOperandCheck("iushr", InstructionContext::insn_iushr)

    fun ixor() = genericNoOperandCheck("ixor", InstructionContext::insn_ixor)

    fun l2d() = genericNoOperandCheck("l2d", InstructionContext::insn_l2d)

    fun l2f() = genericNoOperandCheck("l2f", InstructionContext::insn_l2f)

    fun l2i() = genericNoOperandCheck("l2i", InstructionContext::insn_l2i)

    fun ladd() = genericNoOperandCheck("ladd", InstructionContext::insn_ladd)

    fun laload() = genericNoOperandCheck("laload", InstructionContext::insn_laload)

    fun land() = genericNoOperandCheck("land", InstructionContext::insn_land)

    fun lastore() = genericNoOperandCheck("lastore", InstructionContext::insn_lastore)

    fun lcmp() = genericNoOperandCheck("lcmp", InstructionContext::insn_lcmp)

    fun lconst(expected: Int) = genericIntOperandCheck("lconst", expected, InstructionContext::insn_lconst) {
            lconst -> lconst.ilconst_atom().text
    }

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

    fun ldiv() = genericNoOperandCheck("ldiv", InstructionContext::insn_ldiv)

    fun lload(expected: Int) = genericIntOperandCheck("lload", expected, InstructionContext::insn_lload) {
            lload -> lload.int_atom().text
    }

    fun lmul() = genericNoOperandCheck("lmul", InstructionContext::insn_lmul)

    fun lneg() = genericNoOperandCheck("lneg", InstructionContext::insn_lneg)

    fun lookupswitch(): SwitchAssert {
        isNotNull

        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed("lookupswitch")

        val stat = actual.stat()[pc]
        val lookup = stat?.instruction()?.insn_lookupswitch()

        if (lookup == null) {
            failWithMessage(
                "Expected lookupswitch instruction at pc($pc) but was ${stat.instruction().text}"
            )
        }

        pc++
        return SwitchAssert(lookup!!)
    }

    inner class SwitchAssert(private val actual: JasmParser.Insn_lookupswitchContext) {
        fun withDefault(expected: String): SwitchAssert {
            Assertions.assertThat(actual.NAME())
                .isNotNull
                .extracting{ l -> l.text }
                .isEqualTo(expected)

            return this
        }

        fun withCase(expectedNum: Int, expectedLabel: String): SwitchAssert {
            Assertions.assertThat(actual.switch_case())
                .isNotNull
                .isNotEmpty

            val match = actual.switch_case().find {
                    c -> expectedNum == c.int_atom().text.toInt() && expectedLabel == c.NAME().text
            }

            if (match == null) {
                failWithMessage(
                    "Expected lookupswitch instruction at pc(${pc - 1}) to contain "
                            + "a case matching {$expectedNum: $expectedLabel} but it did not"
                )
            }

            return this
        }

        fun end() = this@CodeSequenceAssert
    }

    fun lor() = genericNoOperandCheck("lor", InstructionContext::insn_lor)

    fun lrem() = genericNoOperandCheck("lrem", InstructionContext::insn_lrem)

    fun lreturn() = genericNoOperandCheck("lreturn", InstructionContext::insn_lreturn)

    fun lshl() = genericNoOperandCheck("lshl", InstructionContext::insn_lshl)

    fun lshr() = genericNoOperandCheck("lshr", InstructionContext::insn_lshr)

    fun lstore(expected: Int) = genericIntOperandCheck("lstore", expected, InstructionContext::insn_lstore) {
            lstore -> lstore.int_atom().text
    }

    fun lsub() = genericNoOperandCheck("lsub", InstructionContext::insn_lsub)

    fun lushr() = genericNoOperandCheck("lushr", InstructionContext::insn_lushr)

    fun lxor() = genericNoOperandCheck("lxor", InstructionContext::insn_lxor)

    fun monitorenter() = genericNoOperandCheck("monitorenter", InstructionContext::insn_monitorenter)

    fun monitorexit() = genericNoOperandCheck("monitorexit", InstructionContext::insn_monitorexit)

    fun nop() = genericNoOperandCheck("nop", InstructionContext::insn_nop)

    fun pop() = genericNoOperandCheck("pop", InstructionContext::insn_pop)

    fun pop2() = genericNoOperandCheck("pop2", InstructionContext::insn_pop2)

    fun multianewarray(expectedType: String) = multianewarray(expectedType, null)
    fun multianewarray(expectedType: String, expectedDims: Int?): CodeSequenceAssert {
        isNotNull
        Assertions.assertThat(actual.stat()).isNotNull
        hasNotUnderflowed("invokedynamic")

        val stat = actual.stat()[pc]
        val insn = stat.instruction()?.insn_multianewarray()

        if (insn == null) {
            failWithMessage("Expected multianewarray at pc($pc) but was ${insn?.text ?: "<unknown>"}")
        } else {
            if (expectedType != insn.array_type().text) {
                failWithMessage("Expected multianewarray with type $expectedType at pc($pc) but "
                        + "had type ${insn.array_type().text} instead")
            }

            if (expectedDims != null) {
                if (expectedDims != insn.int_atom().text.toInt()) {
                    failWithMessage("Expected multianewarray with $expectedDims dimensions at pc($pc) but "
                            + "had ${insn.int_atom().text} dimensions instead")
                }
            } else {
                if (insn.int_atom() != null) {
                    failWithMessage("Expected multianewarray with automatic dimensions at pc($pc) but "
                            + "had explicit ${insn.int_atom().text} dimensions instead")
                }
            }
        }

        pc++
        return this
    }

    fun newarray(expected: String) = genericStringOperandCheck("newarray", expected, InstructionContext::insn_newarray) {
            newarray -> newarray.prim_type().text
    }


    fun putField(expectedOwner: String, expectedName: String, expectedDescriptor: String) =
        genericFieldAccessCheck(
            "putfield",
            expectedOwner,
            expectedName,
            expectedDescriptor,
            InstructionContext::insn_putfield,
            JasmParser.Insn_putfieldContext::owner,
            JasmParser.Insn_putfieldContext::membername,
            JasmParser.Insn_putfieldContext::type
        )

    fun putStatic(expectedOwner: String, expectedName: String, expectedDescriptor: String) =
        genericFieldAccessCheck(
            "putstatic",
            expectedOwner,
            expectedName,
            expectedDescriptor,
            InstructionContext::insn_putstatic,
            JasmParser.Insn_putstaticContext::owner,
            JasmParser.Insn_putstaticContext::membername,
            JasmParser.Insn_putstaticContext::type
        )

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
        return genericOwnerNameTypeCheckCheck(
            invokeType,
            owner,
            name,
            descriptor,
            invokeExtractor,
            { t: T -> ownerExtractor.invoke(t).text },
            { t: T -> membernameExtractor.invoke(t).text },
            { t: T -> TypeVisitor().visitMethod_descriptor(descriptorExtractor.invoke(t)) }
        )
    }


    private fun <T> genericFieldAccessCheck(
        accessType: String,
        owner: String,
        name: String,
        descriptor: String,
        accessExtractor: (InstructionContext) -> T,
        ownerExtractor: (T) -> OwnerContext,
        membernameExtractor: (T) -> MembernameContext,
        typeExtractor: (T) -> JasmParser.TypeContext
    ): CodeSequenceAssert {
        return genericOwnerNameTypeCheckCheck(
            accessType,
            owner,
            name,
            descriptor,
            accessExtractor,
            { t: T -> ownerExtractor.invoke(t).text },
            { t: T -> membernameExtractor.invoke(t).text },
            { t: T -> TypeVisitor().visitType(typeExtractor.invoke(t)) }
        )
    }

    private fun <T> genericOwnerNameTypeCheckCheck(
        invokeType: String,
        owner: String,
        name: String,
        descriptor: String,
        invokeExtractor: (InstructionContext) -> T,
        ownerExtractor: (T) -> String,
        membernameExtractor: (T) -> String,
        descriptorExtractor: (T) -> String
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
        val extractedOwner = ownerExtractor.invoke(insn)
        val extractedName = membernameExtractor.invoke(insn)
        val extractedDesc = descriptorExtractor.invoke(insn)

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