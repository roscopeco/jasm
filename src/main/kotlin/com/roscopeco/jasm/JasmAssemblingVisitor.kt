/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JasmBaseVisitor
import com.roscopeco.jasm.antlr.JasmParser
import com.roscopeco.jasm.errors.CodeError
import com.roscopeco.jasm.errors.ErrorCollector
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ConstantDynamic
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.util.stream.Collectors
import java.util.stream.IntStream

/**
 * The main visitor which does the code generation to an ASM {@code ClassVisitor}.
 *
 * @param visitor An ASM class visitor to do generation with
 * @param modifiers An instance of the {@link Modifiers} class to handle modifier-related stuff
 * @param unitName The name of the compilation unit (shows up in com.roscopeco.jasm.errors and as an attribute in the class)
 * @param classFormat One of the ASM {@code Vxx} constants from the {@code org.objectweb.asm. class
 */
class JasmAssemblingVisitor(
    private val visitor: ClassVisitor,
    private val modifiers: Modifiers,
    private val unitName: String,
    private val classFormat: Int,
    private val errorCollector: ErrorCollector
) : JasmBaseVisitor<Unit>() {

    /**
     * Convenience constructor which will use the class format for Java 11 (55.0) and a default
     * Modifiers instance.
     *
     * @param visitor An ASM class visitor to do generation with
     * @param unitName The name of the compilation unit (shows up in com.roscopeco.jasm.errors and as an attribute in the class)
     */
    constructor(visitor: ClassVisitor, unitName: String, errorCollector: ErrorCollector) : this(visitor, unitName, Opcodes.V11, errorCollector)

    /**
     * Convenience constructor which will use the specified class format and a default
     * Modifiers instance.
     *
     * @param visitor An ASM class visitor to do generation with
     * @param unitName The name of the compilation unit (shows up in com.roscopeco.jasm.errors and as an attribute in the class)
     * @param classFormat One of the ASM {@code Vxx} constants from the {@code org.objectweb.asm.Opcodes} class
     */
    constructor(visitor: ClassVisitor, unitName: String, classFormat: Int, errorCollector: ErrorCollector)
            : this(visitor, Modifiers(), unitName, classFormat, errorCollector)

    override fun visitClass(ctx: JasmParser.ClassContext) {
        visitor.visit(
            classFormat,
            modifiers.mapModifiers(ctx.type_modifier()),
            ctx.classname().text,
            null,
            ctx.extends_()?.classname()?.QNAME()?.text ?: "java/lang/Object",
            ctx.implements_()?.classname()
                ?.map { it.QNAME().text }
                ?.toTypedArray()
                    ?: emptyArray<String>()
        )

        visitor.visitSource(unitName, "")

        super.visitClass(ctx)
        visitor.visitEnd()
    }

    override fun visitField(ctx: JasmParser.FieldContext) {
        if ((ctx.field_initializer() != null) && (modifiers.mapModifiers(ctx.field_modifier()) and Opcodes.ACC_STATIC) == 0) {
            errorCollector.addError(
                CodeError(
                    unitName,
                    ctx,
                    "Unexpected value for non-static field ${ctx.membername().text}"
                )
            )
        }

        val type = TypeVisitor(unitName, errorCollector).visitType(ctx.type())

        if ("V" == type) {
            errorCollector.addError(CodeError(unitName, ctx, "Field ${ctx.membername().text} cannot have void type"))
        }

        val fv = visitor.visitField(
            modifiers.mapModifiers(ctx.field_modifier()),
            ctx.membername().text,
            type,
            null,
            generateFieldInitializer(ctx.field_initializer())
        )

        super.visitField(ctx)
        fv.visitEnd()
    }

    private fun unescapeConstantString(constant: String) =
        constant.substring(1, constant.length - 1).replace("\"\"", "\"")

    private fun generateFieldInitializer(ctx: JasmParser.Field_initializerContext?) = when {
        ctx?.int_atom() != null     -> ctx.int_atom().text.toInt()
        ctx?.float_atom() != null   -> ctx.float_atom().text.toFloat()
        ctx?.string_atom() != null  -> unescapeConstantString(ctx.string_atom().text)
        else                        -> null
    }

    override fun visitMethod(ctx: JasmParser.MethodContext) {
        return JasmMethodVisitor(ctx).visitMethod(ctx)
    }

    private inner class JasmMethodVisitor(ctx: JasmParser.MethodContext) : JasmBaseVisitor<Unit>() {
        private val labels = HashMap<String, LabelHolder>()

        // Cheating slightly, but prevents us having to have an apparently-mutable visitor...
        private val methodVisitor: MethodVisitor = visitor.visitMethod(
            modifiers.mapModifiers(ctx.method_modifier()),
            ctx.membername().text,
            TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
            null,
            null
        )

        override fun visitMethod(ctx: JasmParser.MethodContext) {
             super.visitMethod(ctx)

            // Do this **before** computing frames, as if a label hasn't been visited
            // but is referenced in the code it can cause NPE from ASM (with message
            // "Cannot read field "inputLocals" because "dstFrame" is null").
            guardAllLabelsDeclared(ctx)

            methodVisitor.visitMaxs(0, 0)
            methodVisitor.visitEnd()
        }

        override fun visitLabel(ctx: JasmParser.LabelContext) {
            val label = declareLabel(ctx.LABEL().text)
            methodVisitor.visitLabel(label.label)
        }

        override fun visitInsn_aaload(ctx: JasmParser.Insn_aaloadContext) = methodVisitor.visitInsn(Opcodes.AALOAD)

        override fun visitInsn_aastore(ctx: JasmParser.Insn_aastoreContext) = methodVisitor.visitInsn(Opcodes.AASTORE)

        override fun visitInsn_aconst_null(ctx: JasmParser.Insn_aconst_nullContext)
                = methodVisitor.visitInsn(Opcodes.ACONST_NULL)

        override fun visitInsn_aload(ctx: JasmParser.Insn_aloadContext)
                = methodVisitor.visitVarInsn(Opcodes.ALOAD, ctx.int_atom().text.toInt())

        override fun visitInsn_anewarray(ctx: JasmParser.Insn_anewarrayContext)
                = methodVisitor.visitTypeInsn(Opcodes.ANEWARRAY, ctx.QNAME().text)

        override fun visitInsn_areturn(ctx: JasmParser.Insn_areturnContext) = methodVisitor.visitInsn(Opcodes.ARETURN)

        override fun visitInsn_arraylength(ctx: JasmParser.Insn_arraylengthContext)
                = methodVisitor.visitInsn(Opcodes.ARRAYLENGTH)

        override fun visitInsn_astore(ctx: JasmParser.Insn_astoreContext)
                = methodVisitor.visitVarInsn(Opcodes.ASTORE, ctx.int_atom().text.toInt())

        override fun visitInsn_athrow(ctx: JasmParser.Insn_athrowContext) = methodVisitor.visitInsn(Opcodes.ATHROW)

        override fun visitInsn_baload(ctx: JasmParser.Insn_baloadContext) = methodVisitor.visitInsn(Opcodes.BALOAD)

        override fun visitInsn_bastore(ctx: JasmParser.Insn_bastoreContext) = methodVisitor.visitInsn(Opcodes.BASTORE)

        override fun visitInsn_bipush(ctx: JasmParser.Insn_bipushContext)
                = methodVisitor.visitIntInsn(Opcodes.BIPUSH, ctx.int_atom().text.toInt() and 0xff)

        override fun visitInsn_caload(ctx: JasmParser.Insn_caloadContext) = methodVisitor.visitInsn(Opcodes.CALOAD)

        override fun visitInsn_castore(ctx: JasmParser.Insn_castoreContext) = methodVisitor.visitInsn(Opcodes.CASTORE)

        override fun visitInsn_checkcast(ctx: JasmParser.Insn_checkcastContext)
                = methodVisitor.visitTypeInsn(Opcodes.CHECKCAST, ctx.QNAME().text)

        override fun visitInsn_d2f(ctx: JasmParser.Insn_d2fContext) = methodVisitor.visitInsn(Opcodes.D2F)

        override fun visitInsn_d2i(ctx: JasmParser.Insn_d2iContext) = methodVisitor.visitInsn(Opcodes.D2I)

        override fun visitInsn_d2l(ctx: JasmParser.Insn_d2lContext) = methodVisitor.visitInsn(Opcodes.D2L)

        override fun visitInsn_dadd(ctx: JasmParser.Insn_daddContext) = methodVisitor.visitInsn(Opcodes.DADD)

        override fun visitInsn_daload(ctx: JasmParser.Insn_daloadContext) = methodVisitor.visitInsn(Opcodes.DALOAD)

        override fun visitInsn_dastore(ctx: JasmParser.Insn_dastoreContext) = methodVisitor.visitInsn(Opcodes.DASTORE)

        override fun visitInsn_dcmpg(ctx: JasmParser.Insn_dcmpgContext) = methodVisitor.visitInsn(Opcodes.DCMPG)

        override fun visitInsn_dcmpl(ctx: JasmParser.Insn_dcmplContext) = methodVisitor.visitInsn(Opcodes.DCMPL)

        override fun visitInsn_dconst(ctx: JasmParser.Insn_dconstContext) {
            when (ctx.int_atom().text.toInt()) {
                0 -> methodVisitor.visitInsn(Opcodes.DCONST_0)
                1 -> methodVisitor.visitInsn(Opcodes.DCONST_1)
                else -> errorCollector.addError(
                    CodeError(unitName, ctx,"Invalid operand to DCONST: ${ctx.int_atom().text} (expecting 0 or 1)")
                )
            }
        }

        override fun visitInsn_ddiv(ctx: JasmParser.Insn_ddivContext) = methodVisitor.visitInsn(Opcodes.DDIV)

        override fun visitInsn_dload(ctx: JasmParser.Insn_dloadContext)
                = methodVisitor.visitVarInsn(Opcodes.DLOAD, ctx.int_atom().text.toInt())

        override fun visitInsn_dmul(ctx: JasmParser.Insn_dmulContext) = methodVisitor.visitInsn(Opcodes.DMUL)

        override fun visitInsn_dneg(ctx: JasmParser.Insn_dnegContext) = methodVisitor.visitInsn(Opcodes.DNEG)

        override fun visitInsn_drem(ctx: JasmParser.Insn_dremContext) = methodVisitor.visitInsn(Opcodes.DREM)

        override fun visitInsn_dreturn(ctx: JasmParser.Insn_dreturnContext) = methodVisitor.visitInsn(Opcodes.DRETURN)

        override fun visitInsn_dstore(ctx: JasmParser.Insn_dstoreContext)
                = methodVisitor.visitVarInsn(Opcodes.DSTORE, ctx.int_atom().text.toInt())

        override fun visitInsn_dsub(ctx: JasmParser.Insn_dsubContext) = methodVisitor.visitInsn(Opcodes.DSUB)

        override fun visitInsn_dup(ctx: JasmParser.Insn_dupContext) = methodVisitor.visitInsn(Opcodes.DUP)

        override fun visitInsn_dup_x1(ctx: JasmParser.Insn_dup_x1Context) = methodVisitor.visitInsn(Opcodes.DUP_X1)

        override fun visitInsn_dup_x2(ctx: JasmParser.Insn_dup_x2Context) = methodVisitor.visitInsn(Opcodes.DUP_X2)

        override fun visitInsn_dup2(ctx: JasmParser.Insn_dup2Context) = methodVisitor.visitInsn(Opcodes.DUP2)

        override fun visitInsn_dup2_x2(ctx: JasmParser.Insn_dup2_x2Context) = methodVisitor.visitInsn(Opcodes.DUP2_X2)

        override fun visitInsn_dup2_x1(ctx: JasmParser.Insn_dup2_x1Context) = methodVisitor.visitInsn(Opcodes.DUP2_X1)

        override fun visitInsn_f2d(ctx: JasmParser.Insn_f2dContext) = methodVisitor.visitInsn(Opcodes.F2D)

        override fun visitInsn_f2i(ctx: JasmParser.Insn_f2iContext) = methodVisitor.visitInsn(Opcodes.F2I)

        override fun visitInsn_f2l(ctx: JasmParser.Insn_f2lContext) = methodVisitor.visitInsn(Opcodes.F2L)

        override fun visitInsn_fadd(ctx: JasmParser.Insn_faddContext) = methodVisitor.visitInsn(Opcodes.FADD)

        override fun visitInsn_faload(ctx: JasmParser.Insn_faloadContext) = methodVisitor.visitInsn(Opcodes.FALOAD)

        override fun visitInsn_fastore(ctx: JasmParser.Insn_fastoreContext) = methodVisitor.visitInsn(Opcodes.FASTORE)

        override fun visitInsn_fcmpg(ctx: JasmParser.Insn_fcmpgContext) = methodVisitor.visitInsn(Opcodes.FCMPG)

        override fun visitInsn_fcmpl(ctx: JasmParser.Insn_fcmplContext) = methodVisitor.visitInsn(Opcodes.FCMPL)

        override fun visitInsn_fconst(ctx: JasmParser.Insn_fconstContext) = when (ctx.int_atom().text.toInt()) {
            0 -> methodVisitor.visitInsn(Opcodes.FCONST_0)
            1 -> methodVisitor.visitInsn(Opcodes.FCONST_1)
            else -> errorCollector.addError(
                CodeError(unitName, ctx,"Invalid operand to FCONST: ${ctx.int_atom().text} (expecting 0 or 1)")
            )
        }

        override fun visitInsn_fdiv(ctx: JasmParser.Insn_fdivContext) = methodVisitor.visitInsn(Opcodes.FDIV)

        override fun visitInsn_fload(ctx: JasmParser.Insn_floadContext)
                = methodVisitor.visitVarInsn(Opcodes.FLOAD, ctx.int_atom().text.toInt())

        override fun visitInsn_fmul(ctx: JasmParser.Insn_fmulContext) = methodVisitor.visitInsn(Opcodes.FMUL)

        override fun visitInsn_fneg(ctx: JasmParser.Insn_fnegContext) = methodVisitor.visitInsn(Opcodes.FNEG)

        override fun visitInsn_frem(ctx: JasmParser.Insn_fremContext) = methodVisitor.visitInsn(Opcodes.FREM)

        override fun visitInsn_freturn(ctx: JasmParser.Insn_freturnContext) = methodVisitor.visitInsn(Opcodes.FRETURN)

        override fun visitInsn_fsub(ctx: JasmParser.Insn_fsubContext) = methodVisitor.visitInsn(Opcodes.FSUB)

        override fun visitInsn_fstore(ctx: JasmParser.Insn_fstoreContext)
                = methodVisitor.visitVarInsn(Opcodes.FSTORE, ctx.int_atom().text.toInt())

        override fun visitInsn_getfield(ctx: JasmParser.Insn_getfieldContext)
                = methodVisitor.visitFieldInsn(
                    Opcodes.GETFIELD,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitType(ctx.type())
                )

        override fun visitInsn_getstatic(ctx: JasmParser.Insn_getstaticContext)
                = methodVisitor.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitType(ctx.type())
                )

        override fun visitInsn_goto(ctx: JasmParser.Insn_gotoContext)
                = methodVisitor.visitJumpInsn(Opcodes.GOTO, getLabel(ctx.NAME().text).label)

        override fun visitInsn_i2b(ctx: JasmParser.Insn_i2bContext) = methodVisitor.visitInsn(Opcodes.I2B)

        override fun visitInsn_i2c(ctx: JasmParser.Insn_i2cContext) = methodVisitor.visitInsn(Opcodes.I2C)

        override fun visitInsn_i2d(ctx: JasmParser.Insn_i2dContext) = methodVisitor.visitInsn(Opcodes.I2D)

        override fun visitInsn_i2f(ctx: JasmParser.Insn_i2fContext) = methodVisitor.visitInsn(Opcodes.I2F)

        override fun visitInsn_i2l(ctx: JasmParser.Insn_i2lContext) = methodVisitor.visitInsn(Opcodes.I2L)

        override fun visitInsn_i2s(ctx: JasmParser.Insn_i2sContext) = methodVisitor.visitInsn(Opcodes.I2S)

        override fun visitInsn_iadd(ctx: JasmParser.Insn_iaddContext) = methodVisitor.visitInsn(Opcodes.IADD)

        override fun visitInsn_iaload(ctx: JasmParser.Insn_ialoadContext) = methodVisitor.visitInsn(Opcodes.IALOAD)

        override fun visitInsn_iand(ctx: JasmParser.Insn_iandContext) = methodVisitor.visitInsn(Opcodes.IAND)

        override fun visitInsn_iastore(ctx: JasmParser.Insn_iastoreContext) = methodVisitor.visitInsn(Opcodes.IASTORE)

        override fun visitInsn_iconst(ctx: JasmParser.Insn_iconstContext)
                = methodVisitor.visitInsn(generateIconstOpcode(ctx.ilconst_atom()))

        override fun visitInsn_idiv(ctx: JasmParser.Insn_idivContext) = methodVisitor.visitInsn(Opcodes.IDIV)

        override fun visitInsn_ifeq(ctx: JasmParser.Insn_ifeqContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFEQ, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifge(ctx: JasmParser.Insn_ifgeContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFGE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifgt(ctx: JasmParser.Insn_ifgtContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFGT, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifle(ctx: JasmParser.Insn_ifleContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFLE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_iflt(ctx: JasmParser.Insn_ifltContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFLT, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifne(ctx: JasmParser.Insn_ifneContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFNE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_acmpeq(ctx: JasmParser.Insn_if_acmpeqContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ACMPEQ, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_acmpne(ctx: JasmParser.Insn_if_acmpneContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ACMPNE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmpeq(ctx: JasmParser.Insn_if_icmpeqContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmpge(ctx: JasmParser.Insn_if_icmpgeContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmpgt(ctx: JasmParser.Insn_if_icmpgtContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmple(ctx: JasmParser.Insn_if_icmpleContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmplt(ctx: JasmParser.Insn_if_icmpltContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, getLabel(ctx.NAME().text).label)

        override fun visitInsn_if_icmpne(ctx: JasmParser.Insn_if_icmpneContext)
                = methodVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifnull(ctx: JasmParser.Insn_ifnullContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFNULL, getLabel(ctx.NAME().text).label)

        override fun visitInsn_ifnonnull(ctx: JasmParser.Insn_ifnonnullContext)
                = methodVisitor.visitJumpInsn(Opcodes.IFNONNULL, getLabel(ctx.NAME().text).label)

        override fun visitInsn_iinc(ctx: JasmParser.Insn_iincContext)
                = methodVisitor.visitIincInsn(ctx.int_atom(0).text.toInt(), ctx.int_atom(1).text.toInt())

        override fun visitInsn_iload(ctx: JasmParser.Insn_iloadContext)
                = methodVisitor.visitVarInsn(Opcodes.ILOAD, ctx.int_atom().text.toInt())

        override fun visitInsn_imul(ctx: JasmParser.Insn_imulContext) = methodVisitor.visitInsn(Opcodes.IMUL)

        override fun visitInsn_ineg(ctx: JasmParser.Insn_inegContext) = methodVisitor.visitInsn(Opcodes.INEG)

        override fun visitInsn_instanceof(ctx: JasmParser.Insn_instanceofContext)
                = methodVisitor.visitTypeInsn(Opcodes.INSTANCEOF, ctx.QNAME().text)

        override fun visitInsn_invokedynamic(ctx: JasmParser.Insn_invokedynamicContext)
                 = methodVisitor.visitInvokeDynamicInsn(
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
                    buildBootstrapHandle(ctx.invokedynamic_body().method_handle()),
                    *generateConstArgs(ctx.invokedynamic_body().const_args().const_arg())
                )

        override fun visitInsn_invokeinterface(ctx: JasmParser.Insn_invokeinterfaceContext)
                = visitNonDynamicInvoke(
                    Opcodes.INVOKEINTERFACE,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
                    true
                )

        override fun visitInsn_invokespecial(ctx: JasmParser.Insn_invokespecialContext)
                = visitNonDynamicInvoke(
                    Opcodes.INVOKESPECIAL,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
                    false
                )

        override fun visitInsn_invokestatic(ctx: JasmParser.Insn_invokestaticContext)
                = visitNonDynamicInvoke(
                    Opcodes.INVOKESTATIC,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
                    false
                )

        override fun visitInsn_invokevirtual(ctx: JasmParser.Insn_invokevirtualContext)
                = visitNonDynamicInvoke(
                    Opcodes.INVOKEVIRTUAL,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()),
                    false
                )

        override fun visitInsn_ior(ctx: JasmParser.Insn_iorContext) = methodVisitor.visitInsn(Opcodes.IOR)

        override fun visitInsn_irem(ctx: JasmParser.Insn_iremContext) = methodVisitor.visitInsn(Opcodes.IREM)

        override fun visitInsn_ireturn(ctx: JasmParser.Insn_ireturnContext) = methodVisitor.visitInsn(Opcodes.IRETURN)

        override fun visitInsn_ishl(ctx: JasmParser.Insn_ishlContext) = methodVisitor.visitInsn(Opcodes.ISHL)

        override fun visitInsn_ishr(ctx: JasmParser.Insn_ishrContext) = methodVisitor.visitInsn(Opcodes.ISHR)

        override fun visitInsn_istore(ctx: JasmParser.Insn_istoreContext)
                = methodVisitor.visitVarInsn(Opcodes.ISTORE, ctx.int_atom().text.toInt())

        override fun visitInsn_isub(ctx: JasmParser.Insn_isubContext) = methodVisitor.visitInsn(Opcodes.ISUB)


        override fun visitInsn_iushr(ctx: JasmParser.Insn_iushrContext) = methodVisitor.visitInsn(Opcodes.IUSHR)

        override fun visitInsn_ixor(ctx: JasmParser.Insn_ixorContext) = methodVisitor.visitInsn(Opcodes.IXOR)

        override fun visitInsn_jsr(ctx: JasmParser.Insn_jsrContext)
                = methodVisitor.visitJumpInsn(Opcodes.JSR, getLabel(ctx.NAME().text).label)

        override fun visitInsn_l2d(ctx: JasmParser.Insn_l2dContext) = methodVisitor.visitInsn(Opcodes.L2D)

        override fun visitInsn_l2f(ctx: JasmParser.Insn_l2fContext) = methodVisitor.visitInsn(Opcodes.L2F)

        override fun visitInsn_l2i(ctx: JasmParser.Insn_l2iContext) = methodVisitor.visitInsn(Opcodes.L2I)

        override fun visitInsn_ladd(ctx: JasmParser.Insn_laddContext) = methodVisitor.visitInsn(Opcodes.LADD)

        override fun visitInsn_laload(ctx: JasmParser.Insn_laloadContext) = methodVisitor.visitInsn(Opcodes.LALOAD)

        override fun visitInsn_land(ctx: JasmParser.Insn_landContext) = methodVisitor.visitInsn(Opcodes.LAND)

        override fun visitInsn_lastore(ctx: JasmParser.Insn_lastoreContext) = methodVisitor.visitInsn(Opcodes.LASTORE)

        override fun visitInsn_lcmp(ctx: JasmParser.Insn_lcmpContext) = methodVisitor.visitInsn(Opcodes.LCMP)

        override fun visitInsn_lconst(ctx: JasmParser.Insn_lconstContext) = when (ctx.ilconst_atom().text) {
            "0", "false" -> methodVisitor.visitInsn(Opcodes.LCONST_0)
            "1", "true" -> methodVisitor.visitInsn(Opcodes.LCONST_1)
            else -> errorCollector.addError(
                CodeError(unitName, ctx, "Invalid operand to LCONST: ${ctx.ilconst_atom().text} (expecting 0, 1, true or false)")
            )
        }

        override fun visitInsn_ldc(ctx: JasmParser.Insn_ldcContext)
                = methodVisitor.visitLdcInsn(generateSingleConstArg(0, ctx.const_arg()))

        override fun visitInsn_ldiv(ctx: JasmParser.Insn_ldivContext) = methodVisitor.visitInsn(Opcodes.LDIV)

        override fun visitInsn_lload(ctx: JasmParser.Insn_lloadContext)
                = methodVisitor.visitVarInsn(Opcodes.LLOAD, ctx.int_atom().text.toInt())

        override fun visitInsn_lmul(ctx: JasmParser.Insn_lmulContext) = methodVisitor.visitInsn(Opcodes.LMUL)

        override fun visitInsn_lneg(ctx: JasmParser.Insn_lnegContext) = methodVisitor.visitInsn(Opcodes.LNEG)

        override fun visitInsn_lookupswitch(ctx: JasmParser.Insn_lookupswitchContext) {
            val keys = ctx.switch_case().map { c -> c.int_atom().text.toInt() }.toIntArray()
            val labels = ctx.switch_case().map { c -> getLabel(c.NAME().text).label }.toTypedArray()

            methodVisitor.visitLookupSwitchInsn(
                getLabel(ctx.NAME().text).label,
                keys,
                labels
            )
        }

        override fun visitInsn_lor(ctx: JasmParser.Insn_lorContext) = methodVisitor.visitInsn(Opcodes.LOR)

        override fun visitInsn_lrem(ctx: JasmParser.Insn_lremContext) = methodVisitor.visitInsn(Opcodes.LREM)

        override fun visitInsn_lreturn(ctx: JasmParser.Insn_lreturnContext) = methodVisitor.visitInsn(Opcodes.LRETURN)

        override fun visitInsn_lshl(ctx: JasmParser.Insn_lshlContext) = methodVisitor.visitInsn(Opcodes.LSHL)

        override fun visitInsn_lshr(ctx: JasmParser.Insn_lshrContext) = methodVisitor.visitInsn(Opcodes.LSHR)

        override fun visitInsn_lstore(ctx: JasmParser.Insn_lstoreContext)
                = methodVisitor.visitVarInsn(Opcodes.LSTORE, ctx.int_atom().text.toInt())

        override fun visitInsn_lsub(ctx: JasmParser.Insn_lsubContext) = methodVisitor.visitInsn(Opcodes.LSUB)

        override fun visitInsn_lushr(ctx: JasmParser.Insn_lushrContext) = methodVisitor.visitInsn(Opcodes.LUSHR)

        override fun visitInsn_lxor(ctx: JasmParser.Insn_lxorContext) = methodVisitor.visitInsn(Opcodes.LXOR)

        override fun visitInsn_monitorenter(ctx: JasmParser.Insn_monitorenterContext)
                = methodVisitor.visitInsn(Opcodes.MONITORENTER)

        override fun visitInsn_monitorexit(ctx: JasmParser.Insn_monitorexitContext)
                = methodVisitor.visitInsn(Opcodes.MONITOREXIT)

        override fun visitInsn_multianewarray(ctx: JasmParser.Insn_multianewarrayContext)
                = methodVisitor.visitMultiANewArrayInsn(
                    TypeVisitor(unitName, errorCollector).visitArray_type(ctx.array_type()),
                    getOrComputeArrayDims(ctx)
                )

        override fun visitInsn_new(ctx: JasmParser.Insn_newContext)
                = methodVisitor.visitTypeInsn(Opcodes.NEW, ctx.QNAME().text)

        override fun visitInsn_newarray(ctx: JasmParser.Insn_newarrayContext)
                = methodVisitor.visitIntInsn(Opcodes.NEWARRAY, typeForNewarray(ctx.prim_type()))

        override fun visitInsn_nop(ctx: JasmParser.Insn_nopContext) = methodVisitor.visitInsn(Opcodes.NOP)

        override fun visitInsn_pop(ctx: JasmParser.Insn_popContext) = methodVisitor.visitInsn(Opcodes.POP)

        override fun visitInsn_pop2(ctx: JasmParser.Insn_pop2Context) = methodVisitor.visitInsn(Opcodes.POP2)

        override fun visitInsn_ret(ctx: JasmParser.Insn_retContext)
            = methodVisitor.visitIntInsn(Opcodes.RET, ctx.int_atom().text.toInt())

        override fun visitInsn_putfield(ctx: JasmParser.Insn_putfieldContext)
                = methodVisitor.visitFieldInsn(
                    Opcodes.PUTFIELD,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitType(ctx.type())
                )

        override fun visitInsn_putstatic(ctx: JasmParser.Insn_putstaticContext)
                = methodVisitor.visitFieldInsn(
                    Opcodes.PUTSTATIC,
                    ctx.owner().text,
                    ctx.membername().text,
                    TypeVisitor(unitName, errorCollector).visitType(ctx.type())
                )

        override fun visitInsn_return(ctx: JasmParser.Insn_returnContext) = methodVisitor.visitInsn(Opcodes.RETURN)

        override fun visitInsn_saload(ctx: JasmParser.Insn_saloadContext)
                = methodVisitor.visitInsn(Opcodes.SALOAD)

        override fun visitInsn_sastore(ctx: JasmParser.Insn_sastoreContext)
                = methodVisitor.visitInsn(Opcodes.SASTORE)

        override fun visitInsn_sipush(ctx: JasmParser.Insn_sipushContext)
                = methodVisitor.visitIntInsn(Opcodes.SIPUSH, ctx.int_atom().text.toInt() and 0xffff)

        override fun visitInsn_swap(ctx: JasmParser.Insn_swapContext)
                = methodVisitor.visitInsn(Opcodes.SWAP)


        override fun visitInsn_tableswitch(ctx: JasmParser.Insn_tableswitchContext) {
            val keys = ctx.switch_case().map { c -> c.int_atom().text.toInt() }
            val labels = ctx.switch_case().map { c -> getLabel(c.NAME().text).label }
            val default = getLabel(ctx.NAME().text).label
            val pairs = (keys zip labels)
            val min = pairs.minOf { p -> p.first }
            val max = pairs.maxOf { p -> p.first }

            methodVisitor.visitTableSwitchInsn(
                min,
                max,
                default,
                *IntStream.rangeClosed(min, max).mapToObj {
                        i -> pairs.find { p -> p.first == i }?.second ?: default
                }.collect(Collectors.toList()).toTypedArray()
            )
        }

        private fun visitNonDynamicInvoke(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            methodVisitor.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }

        private fun getOrComputeArrayDims(ctx: JasmParser.Insn_multianewarrayContext)
                = ctx.int_atom()?.text?.toInt() ?: ctx.array_type().text.count { c -> '[' == c }

        private fun typeForNewarray(ctx: JasmParser.Prim_typeContext) = when {
            ctx.TYPE_BOOL() != null     -> Opcodes.T_BOOLEAN
            ctx.TYPE_BYTE() != null     -> Opcodes.T_BYTE
            ctx.TYPE_CHAR() != null     -> Opcodes.T_CHAR
            ctx.TYPE_DOUBLE() != null   -> Opcodes.T_DOUBLE
            ctx.TYPE_FLOAT() != null    -> Opcodes.T_FLOAT
            ctx.TYPE_INT() != null      -> Opcodes.T_INT
            ctx.TYPE_LONG() != null     -> Opcodes.T_LONG
            ctx.TYPE_SHORT() != null    -> Opcodes.T_SHORT
            else -> {
                errorCollector.addError(CodeError(unitName, ctx, "Unknown primitive type for newarray " + ctx.text))
                Opcodes.T_INT
            }
        }

        private fun buildBootstrapHandle(ctx: JasmParser.Method_handleContext): Handle {
            return Handle(
                generateTagForHandle(ctx),
                ctx.bootstrap_spec().owner().text,
                ctx.bootstrap_spec().membername().text,
                TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.bootstrap_spec().method_descriptor()),
                ctx.handle_tag().INVOKEINTERFACE() != null,
            )
        }

        private fun generateTagForHandle(ctx: JasmParser.Method_handleContext) = when {
            ctx.handle_tag().INVOKEINTERFACE() != null  -> Opcodes.H_INVOKEINTERFACE
            ctx.handle_tag().INVOKESPECIAL() != null    -> Opcodes.H_INVOKESPECIAL
            ctx.handle_tag().INVOKESTATIC() != null     -> Opcodes.H_INVOKESTATIC
            ctx.handle_tag().INVOKEVIRTUAL() != null    -> Opcodes.H_INVOKEVIRTUAL
            ctx.handle_tag().NEWINVOKESPECIAL() != null -> Opcodes.H_NEWINVOKESPECIAL
            ctx.handle_tag().GETFIELD() != null         -> Opcodes.H_GETFIELD
            ctx.handle_tag().GETSTATIC() != null        -> Opcodes.H_GETSTATIC
            ctx.handle_tag().PUTFIELD() != null         -> Opcodes.H_PUTFIELD
            ctx.handle_tag().PUTSTATIC() != null        -> Opcodes.H_PUTSTATIC
            else -> {
                errorCollector.addError(CodeError(unitName, ctx, "Unknown handle tag " + ctx.handle_tag().text))
                Opcodes.H_INVOKESTATIC
            }
        }

        private fun generateConstArgs(ctx: MutableList<JasmParser.Const_argContext>): Array<Any> {
            return ctx.mapIndexed { i, arg -> generateSingleConstArg(i, arg) }.toTypedArray()
        }

        private fun generateSingleConstArg(idx: Int, ctx: JasmParser.Const_argContext): Any {
            return when {
                ctx.int_atom() != null          -> Integer.parseInt(ctx.int_atom().text)
                ctx.float_atom() != null        -> java.lang.Float.parseFloat(ctx.float_atom().text)
                ctx.string_atom() != null       -> unescapeConstantString(ctx.string_atom().text)
                ctx.bool_atom() != null         -> if (java.lang.Boolean.parseBoolean(ctx.bool_atom().text)) 1 else 0
                ctx.QNAME() != null             -> Type.getType("L" + ctx.QNAME().text + ";")
                ctx.method_handle() != null     -> buildBootstrapHandle(ctx.method_handle())
                ctx.method_descriptor() != null -> Type.getMethodType(TypeVisitor(unitName, errorCollector).visitMethod_descriptor(ctx.method_descriptor()))
                ctx.constdynamic() != null      -> ConstantDynamic(
                    ctx.constdynamic().membername().text,
                    TypeVisitor(unitName, errorCollector).visitType(ctx.constdynamic().type()),
                    buildBootstrapHandle(ctx.constdynamic().method_handle()),
                    *generateConstArgs(ctx.constdynamic().const_arg())
                )
                else -> {
                    errorCollector.addError(CodeError(unitName, ctx, "Unsupported constant arg at #${idx}: " + ctx.text))

                    "<error>"
                }
            }
        }

        private fun generateIconstOpcode(ctx: JasmParser.Ilconst_atomContext): Int = when (ctx.text) {
            "-1"        -> Opcodes.ICONST_M1
            "0", "false"-> Opcodes.ICONST_0
            "1", "true" -> Opcodes.ICONST_1
            "2"         -> Opcodes.ICONST_2
            "3"         -> Opcodes.ICONST_3
            "4"         -> Opcodes.ICONST_4
            "5"         -> Opcodes.ICONST_5
            else -> {
                errorCollector.addError(
                    CodeError(unitName, ctx, "Invalid operand to ICONST (must be in range -1 to 5, or true/false)")
                )

                Opcodes.ICONST_0
            }
        }

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

        private fun guardAllLabelsDeclared(ctx: JasmParser.MethodContext) {
            val undeclaredLabels = labels.entries
                .filter { (_, value) -> !value.declared }
                .joinToString { (key, _) -> key }

            if (undeclaredLabels.isNotEmpty()) {
                errorCollector.addError(CodeError(unitName, ctx, "Labels used but not declared: [$undeclaredLabels]"))
            }
        }

        private inner class LabelHolder(val label: Label, val declared: Boolean)
    }
}