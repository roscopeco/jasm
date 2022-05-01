/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JasmBaseVisitor
import com.roscopeco.jasm.antlr.JasmParser.ClassContext
import com.roscopeco.jasm.antlr.JasmParser.FieldContext
import com.roscopeco.jasm.antlr.JasmParser.MethodContext
import com.roscopeco.jasm.antlr.JasmParser.LabelContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_aconst_nullContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_aloadContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_areturnContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_athrowContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_checkcastContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_dupContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_freturnContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_gotoContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_iconstContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifeqContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifgeContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifgtContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifleContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifltContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifneContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_acmpeqContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_acmpneContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpeqContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpgeContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpgtContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpleContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpltContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_if_icmpneContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifnullContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ifnonnullContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokedynamicContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokeinterfaceContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokespecialContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokestaticContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_invokevirtualContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ireturnContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_ldcContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_newContext
import com.roscopeco.jasm.antlr.JasmParser.Insn_returnContext
import com.roscopeco.jasm.antlr.JasmParser.AtomContext
import com.roscopeco.jasm.antlr.JasmParser.Method_handleContext
import com.roscopeco.jasm.antlr.JasmParser.Bootstrap_argContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ConstantDynamic
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.H_INVOKEINTERFACE
import org.objectweb.asm.Opcodes.H_INVOKESPECIAL
import org.objectweb.asm.Opcodes.H_INVOKESTATIC
import org.objectweb.asm.Opcodes.H_INVOKEVIRTUAL
import org.objectweb.asm.Opcodes.H_NEWINVOKESPECIAL
import org.objectweb.asm.Opcodes.H_GETFIELD
import org.objectweb.asm.Opcodes.H_GETSTATIC
import org.objectweb.asm.Opcodes.H_PUTFIELD
import org.objectweb.asm.Opcodes.H_PUTSTATIC
import org.objectweb.asm.Type

class JasmAssemblingVisitor(
    private val visitor: ClassVisitor,
    private val modifiers: Modifiers,
    private val unitName: String
) : JasmBaseVisitor<Unit>() {

    override fun visitClass(ctx: ClassContext) {
        visitor.visit(
            Opcodes.V17,
            modifiers.mapModifiers(ctx.modifier()),
            ctx.classname().text,
            null,
            ctx.extends_()?.QNAME()?.text ?: "java/lang/Object",
            ctx.implements_()?.QNAME()
                ?.map { it.text }
                ?.toTypedArray()
                    ?: emptyArray<String>()
        )

        visitor.visitSource(unitName, "")

        super.visitClass(ctx)
        visitor.visitEnd()
    }

    override fun visitField(ctx: FieldContext) {
        val fv = visitor.visitField(
            modifiers.mapModifiers(ctx.modifier()),
            ctx.membername().text,
            ctx.type().text,
            null,
            null
        )

        super.visitField(ctx)
        fv.visitEnd()
    }

    override fun visitMethod(ctx: MethodContext) {
        return JasmMethodVisitor(ctx).visitMethod(ctx)
    }

    private inner class JasmMethodVisitor(ctx: MethodContext) : JasmBaseVisitor<Unit>() {
        private val labels = HashMap<String, LabelHolder>()

        // Cheating slightly, but prevents us having to have an apparently-mutable visitor...
        private val methodVisitor: MethodVisitor = visitor.visitMethod(
            modifiers.mapModifiers(ctx.modifier()),
            ctx.membername().text,
            generateMethodDescriptor(ctx),
            null,
            null
        )

        override fun visitMethod(ctx: MethodContext) {
             super.visitMethod(ctx)

            // Do this **before** computing frames, as if a label hasn't been visited
            // but is referenced in the code it can cause NPE from ASM (with message
            // "Cannot read field "inputLocals" because "dstFrame" is null").
            guardAllLabelsDeclared()

            methodVisitor.visitMaxs(0, 0)
            methodVisitor.visitEnd()
        }

        override fun visitLabel(ctx: LabelContext) {
            val label = declareLabel(ctx.LABEL().text)
            methodVisitor.visitLabel(label.label)
            super.visitLabel(ctx)
        }

        override fun visitInsn_aconst_null(ctx: Insn_aconst_nullContext) {
            methodVisitor.visitInsn(Opcodes.ACONST_NULL)
            super.visitInsn_aconst_null(ctx)
        }

        override fun visitInsn_aload(ctx: Insn_aloadContext) {
            methodVisitor.visitIntInsn(Opcodes.ALOAD, ctx.atom().text.toInt())
            super.visitInsn_aload(ctx)
        }

        override fun visitInsn_areturn(ctx: Insn_areturnContext) {
            methodVisitor.visitInsn(Opcodes.ARETURN)
            super.visitInsn_areturn(ctx)
        }

        override fun visitInsn_athrow(ctx: Insn_athrowContext) {
            methodVisitor.visitInsn(Opcodes.ATHROW)
            super.visitInsn_athrow(ctx)
        }

        override fun visitInsn_checkcast(ctx: Insn_checkcastContext) {
            methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ctx.QNAME().text)
            super.visitInsn_checkcast(ctx)
        }

        override fun visitInsn_dup(ctx: Insn_dupContext) {
            methodVisitor.visitInsn(Opcodes.DUP)
            super.visitInsn_dup(ctx)
        }

        override fun visitInsn_freturn(ctx: Insn_freturnContext) {
            methodVisitor.visitInsn(Opcodes.FRETURN)
            super.visitInsn_freturn(ctx)
        }

        override fun visitInsn_goto(ctx: Insn_gotoContext) {
            methodVisitor.visitJumpInsn(Opcodes.GOTO, getLabel(ctx.NAME().text).label)
            super.visitInsn_goto(ctx)
        }

        override fun visitInsn_iconst(ctx: Insn_iconstContext) {
            methodVisitor.visitInsn(generateIconstOpcode(ctx.atom()))
            super.visitInsn_iconst(ctx)
        }

        override fun visitInsn_ifeq(ctx: Insn_ifeqContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFEQ, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifeq(ctx)
        }

        override fun visitInsn_ifge(ctx: Insn_ifgeContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFGE, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifge(ctx)
        }

        override fun visitInsn_ifgt(ctx: Insn_ifgtContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFGT, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifgt(ctx)
        }

        override fun visitInsn_ifle(ctx: Insn_ifleContext) {
            val label = getLabel(ctx.NAME().text)
            methodVisitor.visitJumpInsn(Opcodes.IFLE, label.label)
            super.visitInsn_ifle(ctx)
        }

        override fun visitInsn_iflt(ctx: Insn_ifltContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFLT, getLabel(ctx.NAME().text).label)
            super.visitInsn_iflt(ctx)
        }

        override fun visitInsn_ifne(ctx: Insn_ifneContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFNE, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifne(ctx)
        }

        override fun visitInsn_if_acmpeq(ctx: Insn_if_acmpeqContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ACMPEQ, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_acmpeq(ctx)
        }

        override fun visitInsn_if_acmpne(ctx: Insn_if_acmpneContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ACMPNE, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_acmpne(ctx)
        }

        override fun visitInsn_if_icmpeq(ctx: Insn_if_icmpeqContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_icmpeq(ctx)
        }

        override fun visitInsn_if_icmpge(ctx: Insn_if_icmpgeContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_icmpge(ctx)
        }

        override fun visitInsn_if_icmpgt(ctx: Insn_if_icmpgtContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_icmpgt(ctx)
        }

        override fun visitInsn_if_icmple(ctx: Insn_if_icmpleContext) {
            val label = getLabel(ctx.NAME().text)
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, label.label)
            super.visitInsn_if_icmple(ctx)
        }

        override fun visitInsn_if_icmplt(ctx: Insn_if_icmpltContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_icmplt(ctx)
        }

        override fun visitInsn_if_icmpne(ctx: Insn_if_icmpneContext) {
            methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, getLabel(ctx.NAME().text).label)
            super.visitInsn_if_icmpne(ctx)
        }

        override fun visitInsn_ifnull(ctx: Insn_ifnullContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFNULL, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifnull(ctx)
        }

        override fun visitInsn_ifnonnull(ctx: Insn_ifnonnullContext) {
            methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, getLabel(ctx.NAME().text).label)
            super.visitInsn_ifnonnull(ctx)
        }

        override fun visitInsn_invokedynamic(ctx: Insn_invokedynamicContext) {
            methodVisitor.visitInvokeDynamicInsn(
                ctx.membername().text,
                ctx.method_descriptor().text,
                buildBootstrapHandle(ctx.method_handle()),
                *buildBootstrapArgs(ctx.bootstrap_arg())
            )
        }

        private fun buildBootstrapHandle(ctx: Method_handleContext): Handle {
            return Handle(
                generateTagForHandle(ctx),
                ctx.bootstrap_spec().owner().text,
                ctx.bootstrap_spec().membername().text,
                fixDescriptor(ctx.bootstrap_spec().method_descriptor().text),
                ctx.handle_tag().INVOKEINTERFACE() != null,
            )
        }

        private fun generateTagForHandle(ctx: Method_handleContext) = when {
            ctx.handle_tag().INVOKEINTERFACE() != null -> H_INVOKEINTERFACE
            ctx.handle_tag().INVOKESPECIAL() != null -> H_INVOKESPECIAL
            ctx.handle_tag().INVOKESTATIC() != null -> H_INVOKESTATIC
            ctx.handle_tag().INVOKEVIRTUAL() != null -> H_INVOKEVIRTUAL
            ctx.handle_tag().NEWINVOKESPECIAL() != null -> H_NEWINVOKESPECIAL
            ctx.handle_tag().GETFIELD() != null -> H_GETFIELD
            ctx.handle_tag().GETSTATIC() != null -> H_GETSTATIC
            ctx.handle_tag().PUTFIELD() != null -> H_PUTFIELD
            ctx.handle_tag().PUTSTATIC() != null -> H_PUTSTATIC
            else -> throw SyntaxErrorException("Unknown handle tag " + ctx.handle_tag().text)
        }

        private fun buildBootstrapArgs(ctx: MutableList<Bootstrap_argContext>): Array<Any> {
            return ctx.mapIndexed { i, arg -> buildSingleBootstrapArg(i, arg) }.toTypedArray()
        }

        private fun buildSingleBootstrapArg(idx: Int, ctx: Bootstrap_argContext): Any {
            return when {
                ctx.int_atom() != null -> Integer.parseInt(ctx.int_atom().text)
                ctx.float_atom() != null -> java.lang.Float.parseFloat(ctx.float_atom().text)
                ctx.string_atom() != null -> unescapeConstantString(ctx.string_atom().text)
                ctx.QNAME() != null -> Type.getType("L" + ctx.QNAME().text + ";")
                ctx.method_handle() != null -> buildBootstrapHandle(ctx.method_handle())
                ctx.method_descriptor() != null -> Type.getMethodType(fixDescriptor(ctx.method_descriptor().text))
                ctx.constdynamic() != null -> ConstantDynamic(
                    ctx.constdynamic().membername().text,
                    ctx.constdynamic().type().text,
                    buildBootstrapHandle(ctx.constdynamic().method_handle()),
                    *buildBootstrapArgs(ctx.constdynamic().bootstrap_arg())
                )
                else -> throw SyntaxErrorException("Unsupported bootstrap arg at #${idx}: " + ctx.text)
            }
        }

        override fun visitInsn_invokeinterface(ctx: Insn_invokeinterfaceContext) {
            visitNonDynamicInvoke(
                Opcodes.INVOKEINTERFACE,
                ctx.owner().text,
                ctx.membername().text,
                ctx.method_descriptor().text,
                true
            )

            super.visitInsn_invokeinterface(ctx)
        }

        override fun visitInsn_invokespecial(ctx: Insn_invokespecialContext) {
            visitNonDynamicInvoke(
                Opcodes.INVOKESPECIAL,
                ctx.owner().text,
                ctx.membername().text,
                ctx.method_descriptor().text,
                false
            )

            super.visitInsn_invokespecial(ctx)
        }

        override fun visitInsn_invokestatic(ctx: Insn_invokestaticContext) {
            visitNonDynamicInvoke(
                Opcodes.INVOKESTATIC,
                ctx.owner().text,
                ctx.membername().text,
                ctx.method_descriptor().text,
                false
            )

            super.visitInsn_invokestatic(ctx)
        }

        override fun visitInsn_invokevirtual(ctx: Insn_invokevirtualContext) {
            visitNonDynamicInvoke(
                Opcodes.INVOKEVIRTUAL,
                ctx.owner().text,
                ctx.membername().text,
                ctx.method_descriptor().text,
                false
            )

            super.visitInsn_invokevirtual(ctx)
        }

        private fun visitNonDynamicInvoke(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            methodVisitor.visitMethodInsn(opcode, owner, name, fixDescriptor(descriptor), isInterface)
        }

        override fun visitInsn_ireturn(ctx: Insn_ireturnContext) {
            methodVisitor.visitInsn(Opcodes.IRETURN)
            super.visitInsn_ireturn(ctx)
        }

        override fun visitInsn_ldc(ctx: Insn_ldcContext) {
            methodVisitor.visitLdcInsn(generateLdcObject(ctx))
            super.visitInsn_ldc(ctx)
        }

        override fun visitInsn_new(ctx: Insn_newContext) {
            methodVisitor.visitTypeInsn(Opcodes.NEW, ctx.QNAME().text)
            super.visitInsn_new(ctx)
        }

        override fun visitInsn_return(ctx: Insn_returnContext) {
            methodVisitor.visitInsn(Opcodes.RETURN)
            super.visitInsn_return(ctx)
        }

        private fun generateMethodDescriptor(ctx: MethodContext): String {
            val type = ctx.type()
            val returnType = type[type.size - 1].text

            val paramTypes = type.subList(0, type.size - 1).joinToString { it.text }

            return "($paramTypes)$returnType"
        }

        private fun generateIconstOpcode(ctx: AtomContext): Int = try {
                when (ctx.text.toInt()) {
                    -1 -> Opcodes.ICONST_M1
                    0  -> Opcodes.ICONST_0
                    1  -> Opcodes.ICONST_1
                    2  -> Opcodes.ICONST_2
                    3  -> Opcodes.ICONST_3
                    4  -> Opcodes.ICONST_4
                    5  -> Opcodes.ICONST_5
                    else -> throw SyntaxErrorException("Invalid operand to ICONST (must be in range -1 to 5)")
                }
            } catch (e: NumberFormatException) {
                throw SyntaxErrorException("Invalid non-numeric operand for ICONST (found '" + ctx.text + "')")
            }

        private fun generateLdcObject(ctx: Insn_ldcContext) =
            if (ctx.atom().int_atom() != null) {
                ctx.atom().text.toInt()
            } else if (ctx.atom().float_atom() != null) {
                ctx.atom().text.toFloat()
            } else if (ctx.atom().bool_atom() != null) {
                if (java.lang.Boolean.parseBoolean(ctx.atom().text)) 1 else 0
            } else if (ctx.atom().name_atom() != null) {
                throw UnsupportedOperationException("TODO")
            } else if (ctx.atom().string_atom() != null) {
                unescapeConstantString(ctx.atom().text)
            } else {
                throw SyntaxErrorException("Unable to generate LDC for unknown type (" + ctx.text + ")")
            }

        private fun unescapeConstantString(constant: String) =
            constant.substring(1, constant.length - 1).replace("\"\"", "\"")

        private fun fixDescriptor(languageDescriptor: String) =
            languageDescriptor.replace("([IJFDZV]);".toRegex(), "$1")

        private fun normaliseLabelName(labelName: String) =
            if (labelName.endsWith(":")) {
                labelName.substring(0, labelName.length - 1)
            } else {
                labelName
            }

        private fun getLabel(name: String) =
            labels.computeIfAbsent(normaliseLabelName(name)) { LabelHolder(Label(), false) }

        private fun declareLabel(name: String): LabelHolder {
            val normalName = normaliseLabelName(name)
            val label = labels[normalName]

            if (label == null) {
                labels[normalName] = LabelHolder(Label(), true)
            } else if (!label.declared) {
                labels[normalName] = LabelHolder(label.label, true)
            }

            return labels[normalName]!!
        }

        private fun guardAllLabelsDeclared() {
            val undeclaredLabels = labels.entries
                .filter { (_, value): Map.Entry<String, LabelHolder> -> !value.declared }
                .joinToString { (key, _) -> key }

            if (undeclaredLabels.isNotEmpty()) {
                throw SyntaxErrorException("Labels used but not declared: [$undeclaredLabels]")
            }
        }

        private inner class LabelHolder(val label: Label, val declared: Boolean)
    }
}