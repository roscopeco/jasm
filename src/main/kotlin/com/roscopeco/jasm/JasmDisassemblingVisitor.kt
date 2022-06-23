package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JvmTypesLexer
import com.roscopeco.jasm.antlr.JvmTypesParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class JasmDisassemblingVisitor(private val modifiers: Modifiers, private val unitName: String) : ClassVisitor(Opcodes.ASM9) {
    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()

        private val OPCODE_NAMES = mapOf(
            Opcodes.AALOAD to "aaload",
            Opcodes.AASTORE to "aastore",
            Opcodes.ACONST_NULL to "aconst_null",
            Opcodes.ALOAD to "aload",
            Opcodes.ANEWARRAY to "anewarray",
            Opcodes.ARETURN to "areturn",
            Opcodes.ARRAYLENGTH to "arraylength",
            Opcodes.ASTORE to "astore",
            Opcodes.ATHROW to "athrow",
            Opcodes.BALOAD to "baload",
            Opcodes.BASTORE to "bastore",
            Opcodes.BIPUSH to "bipush",
            Opcodes.CALOAD  to "caload",
            Opcodes.CASTORE to "castore",
            Opcodes.CHECKCAST to "checkcast",
            Opcodes.D2F to "d2f",
            Opcodes.D2I to "d2i",
            Opcodes.D2L to "d2l",
            Opcodes.DADD to "dadd",
            Opcodes.DALOAD to "daload",
            Opcodes.DASTORE to "dastore",
            Opcodes.DCMPG to "dcmpg",
            Opcodes.DCMPL to "dcmpl",
            Opcodes.DCONST_0 to "dconst 0",
            Opcodes.DCONST_1 to "dconst 1",
            Opcodes.DDIV to "ddiv",
            Opcodes.DLOAD to "dload",
            Opcodes.DMUL to "dmul",
            Opcodes.DNEG to "dneg",
            Opcodes.DREM to "drem",
            Opcodes.DRETURN to "dreturn",
            Opcodes.DSTORE to "dstore",
            Opcodes.DSUB to "dsub",
            Opcodes.DUP to "dup",
            Opcodes.DUP_X1 to "dup_x1",
            Opcodes.DUP_X2 to "dup_x2",
            Opcodes.DUP2_X1 to "dup2_x1",
            Opcodes.DUP2_X2 to "dup2_x2",
            Opcodes.F2D to "f2d",
            Opcodes.F2I to "f2i",
            Opcodes.F2L to "f2l",
            Opcodes.FADD to "fadd",
            Opcodes.FALOAD to "faload",
            Opcodes.FASTORE to "fastore",
            Opcodes.FCMPG to "fcmpg",
            Opcodes.FCMPL to "fcmpl",
            Opcodes.FCONST_0 to "fconst 0",
            Opcodes.FCONST_1 to "fconst 1",
            Opcodes.FCONST_2 to "fconst 2",
            Opcodes.FDIV to "fdiv",
            Opcodes.FLOAD to "fload",
            Opcodes.FMUL to "fmul",
            Opcodes.FNEG to "fneg",
            Opcodes.FREM to "frem",
            Opcodes.FRETURN to "freturn",
            Opcodes.FSUB to "fsub",
            Opcodes.FSTORE to "fstore",
            Opcodes.GETFIELD to "getfield",
            Opcodes.GETSTATIC to "getstatic",
            Opcodes.GOTO to "goto",
            Opcodes.I2B  to "i2b",
            Opcodes.I2C  to "i2c",
            Opcodes.I2D  to "i2d",
            Opcodes.I2F  to "i2f",
            Opcodes.I2L  to "i2l",
            Opcodes.I2S  to "i2s",
            Opcodes.IADD to "iadd",
            Opcodes.IALOAD to "iaload",
            Opcodes.IAND to "iand",
            Opcodes.IASTORE to "iastore",
            Opcodes.ICONST_M1 to "iconst -1",
            Opcodes.ICONST_0 to "iconst 0",
            Opcodes.ICONST_1 to "iconst 1",
            Opcodes.ICONST_2 to "iconst 2",
            Opcodes.ICONST_3 to "iconst 3",
            Opcodes.ICONST_4 to "iconst 4",
            Opcodes.ICONST_5 to "iconst 5",
            Opcodes.IDIV to "idiv",
            Opcodes.IFEQ to "ifeq",
            Opcodes.IFNE to "ifne",
            Opcodes.IFLT to "iflt",
            Opcodes.IFGE to "ifge",
            Opcodes.IFGT to "igt",
            Opcodes.IFLE to "ifle",
            Opcodes.IFNULL to "ifnull",
            Opcodes.IFNONNULL to "ifnonnull",
            Opcodes.IF_ACMPEQ to "if_acmpeq",
            Opcodes.IF_ACMPNE to "if_acmpne",
            Opcodes.IF_ICMPEQ to "if_icmpeq",
            Opcodes.IF_ICMPNE to "if_icmpne",
            Opcodes.IF_ICMPLT to "if_icmplt",
            Opcodes.IF_ICMPGE to "if_icmpge",
            Opcodes.IF_ICMPGT to "if_icmpgt",
            Opcodes.IF_ICMPLE to "if_icmple",
            Opcodes.IINC to "iinc",
            Opcodes.ILOAD to "iload",
            Opcodes.IMUL to "imul",
            Opcodes.INEG to "ineg",
            Opcodes.INSTANCEOF to "instanceof",
            Opcodes.INVOKEDYNAMIC to "invokedynamic",
            Opcodes.INVOKEINTERFACE to "invokeinterface",
            Opcodes.INVOKESPECIAL to "invokespecial",
            Opcodes.INVOKESTATIC to "invokestatic",
            Opcodes.INVOKEVIRTUAL to "invokevirtual",
            Opcodes.IOR  to "ior",
            Opcodes.IREM to "irem",
            Opcodes.IRETURN to "ireturn",
            Opcodes.ISHL to "ishl",
            Opcodes.ISHR to "ishr",
            Opcodes.ISTORE to "istore",
            Opcodes.ISUB to "isub",
            Opcodes.IUSHR to "iushr",
            Opcodes.IXOR to "ixor",
            Opcodes.JSR to "jsr",
            Opcodes.L2D to "l2d",
            Opcodes.L2F to "l2f",
            Opcodes.L2I to "l2i",
            Opcodes.LADD to "ladd",
            Opcodes.LALOAD to "laload",
            Opcodes.LAND to "land",
            Opcodes.LASTORE to "lastore",
            Opcodes.LCMP to "lcmp",
            Opcodes.LCONST_0 to "lconst 0",
            Opcodes.LCONST_1 to "lconst 0",
            Opcodes.LDC to "ldc",
            Opcodes.LDIV to "ldiv",
            Opcodes.LLOAD to "lload",
            Opcodes.LMUL to "lmul",
            Opcodes.LNEG to "lneg",
            Opcodes.LOOKUPSWITCH to "lookupswitch",
            Opcodes.LOR to "lor",
            Opcodes.LREM to "lrem",
            Opcodes.LRETURN to "lreturn",
            Opcodes.LSHL to "lshl",
            Opcodes.LSHR to "lshr",
            Opcodes.LSTORE to "lstore",
            Opcodes.LSUB to "lsub",
            Opcodes.LUSHR to "lushr",
            Opcodes.LXOR to "lxor",
            Opcodes.MONITORENTER to "monitorenter",
            Opcodes.MONITOREXIT to "monitorexit",
            Opcodes.MULTIANEWARRAY to "multianewarray",
            Opcodes.NEW to "new",
            Opcodes.NEWARRAY to "newarray",
            Opcodes.NOP to "nop",
            Opcodes.POP to "pop",
            Opcodes.POP2 to "pop2",
            Opcodes.PUTFIELD to "putfield",
            Opcodes.PUTSTATIC to "putstatic",
            Opcodes.RET to "ret",
            Opcodes.RETURN to "return",
            Opcodes.SALOAD to "saload",
            Opcodes.SASTORE to "sastore",
            Opcodes.SIPUSH to "sipush",
            Opcodes.SWAP to "swap",
            Opcodes.TABLESWITCH to "tableswitch"
        )
    }
    
    private var access = 0
    private var name = ""
    private var methods = mutableListOf<JasmDisassemblingMethodVisitor>()

    constructor(unitName: String) : this(Modifiers(), unitName)

    fun output() = output(SpaceIndenter(4))

    fun output(indenter: Indenter): String = fileHeader(indenter) + classHeader(indenter) + classBody(indenter) + LINE_SEPARATOR

    private fun fileHeader(indenter: Indenter): String
            = indenter.indented("/*${LINE_SEPARATOR}") +
                    indenter.indented(" * Disassembled from $unitName with JASM${LINE_SEPARATOR}") +
                    indenter.indented(" */${LINE_SEPARATOR}")

    private fun classHeader(indenter: Indenter): String = indenter.indented("${formattedModifiers(this.access)}class ${this.name}")

    private fun classBody(indenter: Indenter): String {
        return if (methods.isEmpty()) {
            ""
        } else {
            " {\n${classMembers(indenter.indent())}$LINE_SEPARATOR}"
        }
    }

    private fun classMembers(indenter: Indenter): String
            = methods.joinToString(LINE_SEPARATOR + LINE_SEPARATOR) { it.output(indenter) }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.name = name!!
        this.access = access

        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val visitor = JasmDisassemblingMethodVisitor(access, name, descriptor, signature, exceptions)
        methods.add(visitor)
        return visitor
    }

    private fun formattedModifiers(modifierBitmap: Int): String {
        val modsStr = modifiers.disassembleClassModifiers(modifierBitmap)

        return if (modsStr.isEmpty())
            ""
        else
            "$modsStr "
    }

    private interface MethodBlock {
        fun generate(indenter: Indenter): String
    }

    private class Line(val line: String) : MethodBlock {
        override fun generate(indenter: Indenter): String {
            return indenter.indented(line)
        }
    }

    private class LookupSwitch(val default: String, val keys: IntArray, val labels: List<String>) : MethodBlock {
        override fun generate(indenter: Indenter): String {
            val blockIndent = indenter.indent()

            return indenter.indented("${OPCODE_NAMES[Opcodes.LOOKUPSWITCH]!!} $default {$LINE_SEPARATOR") +
                    keys.zip(labels).joinToString(",$LINE_SEPARATOR") { (key, label) -> blockIndent.indented("$key: $label") } +
                    LINE_SEPARATOR +
                    indenter.indented("}")
        }
    }

    private class TableSwitch(val default: String, val min: Int, val max: Int, val labels: List<String>) : MethodBlock {
        override fun generate(indenter: Indenter): String {
            val blockIndent = indenter.indent()

            return indenter.indented("${OPCODE_NAMES[Opcodes.TABLESWITCH]!!} $default {$LINE_SEPARATOR") +
                    (min..max).zip(labels).joinToString(",$LINE_SEPARATOR") { (key, label) -> blockIndent.indented("$key: $label") } +
                    LINE_SEPARATOR +
                    indenter.indented("}")
        }
    }

    private inner class JasmDisassemblingMethodVisitor(
        val access: Int,
        val name: String,
        val descriptor: String,
        val signature: String?,
        val exceptions: Array<out String>?
    ) : MethodVisitor(Opcodes.ASM9) {

        private val labels = mutableMapOf<Label, String>()
        private var nextLabelNum = 0
        private val blocks = mutableListOf<MethodBlock>()

        fun output(indenter: Indenter): String
                = methodComment(indenter) +  methodHeader(indenter) + methodBody(indenter.indent()) + indenter.indented("}")

        fun methodComment(indenter: Indenter): String
                = indenter.indented("// ${signature ?: "<no signature>"}$LINE_SEPARATOR") +
                    indenter.indented("// ${exceptions?.joinToString(", ") ?: "<no exceptions>"}$LINE_SEPARATOR")

        fun methodHeader(indenter: Indenter): String
                = indenter.indented("${formattedModifiers(access)}$name${disassembleMethodDescriptor(descriptor)} {\n")

        fun methodBody(indenter: Indenter): String {
            return if (blocks.isNotEmpty())
                blocks.joinToString(LINE_SEPARATOR) { it.generate(indenter) } + LINE_SEPARATOR
            else
                ""
        }

        override fun visitTryCatchBlock(start: Label, end: Label, handler: Label, type: String) {
            blocks.add(Line("exception ${getLabelName(start)}, ${getLabelName(end)}, ${getLabelName(handler)}, ${
                handleBareType(type)
            }"))
        }

        override fun visitInsn(opcode: Int) {
            blocks.add(Line(OPCODE_NAMES[opcode]!!))
        }

        override fun visitIntInsn(opcode: Int, operand: Int) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} $operand"))
        }

        override fun visitTypeInsn(opcode: Int, type: String) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${handleBareType(type)}"))
        }

        override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name ${disassembleTypeDescriptor(descriptor)}"))
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name${disassembleMethodDescriptor(descriptor)}"))
        }

        override fun visitInvokeDynamicInsn(
            name: String,
            descriptor: String,
            bootstrapMethodHandle: Handle,
            vararg bootstrapMethodArguments: Any?
        ) {
            todo(Opcodes.INVOKEDYNAMIC)
        }

        override fun visitJumpInsn(opcode: Int, label: Label) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${getLabelName(label)}"))
        }

        override fun visitLabel(label: Label) {
            blocks.add(Line(""))
            blocks.add(Line("${getLabelName(label)}:"))
        }

        override fun visitLdcInsn(value: Any) {
            blocks.add(Line("${OPCODE_NAMES[Opcodes.LDC]!!} ${disassembleConstArg(value)}"))
        }

        override fun visitIincInsn(varIndex: Int, increment: Int) {
            blocks.add(Line("${OPCODE_NAMES[Opcodes.IINC]!!} $varIndex, [$increment]"))
        }

        override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label, vararg labels: Label) {
            blocks.add(TableSwitch(getLabelName(dflt), min, max, labels.map { getLabelName(it) }))
        }

        override fun visitLookupSwitchInsn(dflt: Label, keys: IntArray, labels: Array<out Label>) {
            blocks.add(LookupSwitch(getLabelName(dflt), keys, labels.map { getLabelName(it) }))
        }

        override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {
            blocks.add(Line("${OPCODE_NAMES[Opcodes.MULTIANEWARRAY]!!} ${disassembleTypeDescriptor(descriptor)}, $numDimensions"))
        }

        override fun visitVarInsn(opcode: Int, varIndex: Int) {
            blocks.add(Line("${OPCODE_NAMES[opcode]} $varIndex"))
        }

        private fun todo(opcode: Int) {
            blocks.add(Line("// TODO unimplemented opcode $opcode"))
        }

        private fun formattedModifiers(modifierBitmap: Int): String {
            val modsStr = modifiers.disassembleMethodModifiers(modifierBitmap)

            return if (modsStr.isNotEmpty())
                "$modsStr "
            else
                ""
        }

        // Manual fix up bare types that may be ref arrays
        private fun handleBareType(bareType: String): String {
            return if (bareType.contains("[")) {
                val lastLSquare = bareType.lastIndexOf("[")
                bareType.substring(0, lastLSquare + 1) + bareType.substring(lastLSquare + 1, bareType.length - 1)
            } else {
                bareType
            }
        }

        private fun getLabelName(label: Label) = labels.getOrPut(label) { "label${nextLabelNum++}" }

        private fun disassembleConstArg(arg: Any): String = when (arg) {
            // TODO escapes etc!
            is String -> "\"${arg}\""
            is Type -> arg.internalName
            else -> TODO("Const arg type '${arg.javaClass}' not yet supported")
        }

        private fun disassembleMethodDescriptor(descriptor: String): String {
            val ctx = JvmTypesParser(CommonTokenStream(JvmTypesLexer(CharStreams.fromString(descriptor)))).method_descriptor()
            return "(${ctx.param().joinToString(", ") { disassembleSingleType(it.type()) }})${disassembleSingleType(ctx.return_().type())}"
        }

        private fun disassembleTypeDescriptor(type: String): String {
            val ctx = JvmTypesParser(CommonTokenStream(JvmTypesLexer(CharStreams.fromString(type)))).type()
            return disassembleSingleType(ctx)
        }

        private fun disassembleSingleType(ctx: JvmTypesParser.TypeContext): String {
            return if (ctx.prim_type() != null) {
                ctx.prim_type().text
            } else if (ctx.ref_type() != null) {
                val ary = ctx.ref_type().ARRAY()?.joinToString("") { it.text } ?: ""
                val name = ctx.ref_type().REF().text
                "$ary${name.substring(1, name.length - 1)}"
            } else if (ctx.VOID() != null) {
                "V"
            } else {
                throw JasmException("[BUG]: Unparseable method descriptor $descriptor")
            }
        }
    }
}
