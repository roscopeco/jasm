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
            Opcodes.ALOAD to "aload",
            Opcodes.ANEWARRAY to "anewarray",
            Opcodes.ARETURN to "areturn",
            Opcodes.ASTORE to "astore",
            Opcodes.ATHROW to "athrow",
            Opcodes.BIPUSH to "bipush",
            Opcodes.CHECKCAST to "checkcast",
            Opcodes.DLOAD to "dload",
            Opcodes.DSTORE to "dstore",
            Opcodes.DUP to "dup",
            Opcodes.DUP_X1 to "dup_x1",
            Opcodes.DUP_X2 to "dup_x2",
            Opcodes.DUP2_X1 to "dup2_x1",
            Opcodes.DUP2_X2 to "dup2_x2",
            Opcodes.FLOAD to "fload",
            Opcodes.FSTORE to "fstore",
            Opcodes.GETFIELD to "getfield",
            Opcodes.GETSTATIC to "getstatic",
            Opcodes.GOTO to "goto",
            Opcodes.ICONST_0 to "iconst 0",
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
            Opcodes.INSTANCEOF to "instanceof",
            Opcodes.INVOKEINTERFACE to "invokeinterface",
            Opcodes.INVOKESPECIAL to "invokespecial",
            Opcodes.INVOKESTATIC to "invokestatic",
            Opcodes.INVOKEVIRTUAL to "invokevirtual",
            Opcodes.ISTORE to "istore",
            Opcodes.JSR to "jsr",
            Opcodes.LDC to "ldc",
            Opcodes.LLOAD to "lload",
            Opcodes.LSTORE to "lstore",
            Opcodes.MULTIANEWARRAY to "multianewarray",
            Opcodes.NEW to "new",
            Opcodes.NEWARRAY to "newarray",
            Opcodes.POP to "pop",
            Opcodes.POP2 to "pop2",
            Opcodes.PUTFIELD to "putfield",
            Opcodes.PUTSTATIC to "putstatic",
            Opcodes.RET to "ret",
            Opcodes.RETURN to "return",
            Opcodes.SIPUSH to "sipush",
            Opcodes.SWAP to "swap",
        )
    }
    
    private var access = 0
    private var name = ""
    private var methods = mutableListOf<JasmDisassemblingMethodVisitor>()

    constructor(unitName: String) : this(Modifiers(), unitName)

    fun output() = output(SpaceIndenter(4))

    fun output(indenter: Indenter): String = fileHeader(indenter) + classHeader(indenter) + classBody(indenter) + LINE_SEPARATOR

    fun fileHeader(indenter: Indenter): String
            = indenter.indented("/*${LINE_SEPARATOR}") +
                    indenter.indented(" * Disassembled from ${unitName} with JASM${LINE_SEPARATOR}") +
                    indenter.indented(" */${LINE_SEPARATOR}")

    fun classHeader(indenter: Indenter): String = indenter.indented("${formattedModifiers(this.access)}class ${this.name}")

    fun classBody(indenter: Indenter): String {
        return if (methods.isEmpty()) {
            ""
        } else {
            " {\n${classMembers(indenter.indent())}$LINE_SEPARATOR}"
        }
    }

    fun classMembers(indenter: Indenter): String 
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

    private inner class JasmDisassemblingMethodVisitor(
        val access: Int,
        val name: String,
        val descriptor: String,
        val signature: String?,
        val exceptions: Array<out String>?
    ) : MethodVisitor(Opcodes.ASM9) {

        private val labels = mutableMapOf<Label, String>()
        private var nextLabelNum = 0
        private val lines = mutableListOf<String>()

        fun output(indenter: Indenter): String
                = methodComment(indenter) +  methodHeader(indenter) + methodBody(indenter.indent()) + indenter.indented("}")

        fun methodComment(indenter: Indenter): String
                = indenter.indented("// ${signature ?: "<no signature>"}$LINE_SEPARATOR") +
                    indenter.indented("// ${exceptions?.joinToString(", ") ?: "<no exceptions>"}$LINE_SEPARATOR")

        fun methodHeader(indenter: Indenter): String
                = indenter.indented("${formattedModifiers(access)}$name${disassembleMethodDescriptor(descriptor)} {\n")

        fun methodBody(indenter: Indenter): String {
            return if (lines.isNotEmpty())
                lines.joinToString(LINE_SEPARATOR) { indenter.indented(it) } + LINE_SEPARATOR
            else
                ""
        }

        override fun visitTryCatchBlock(start: Label, end: Label, handler: Label, type: String) {
            lines.add("exception ${getLabelName(start)}, ${getLabelName(end)}, ${getLabelName(handler)}, ${
                handleBareType(type)
            }")
        }

        override fun visitInsn(opcode: Int) {
            lines.add(OPCODE_NAMES[opcode]!!)
        }

        override fun visitIntInsn(opcode: Int, operand: Int) {
            lines.add("${OPCODE_NAMES[opcode]!!} $operand")
        }

        override fun visitTypeInsn(opcode: Int, type: String) {
            lines.add("${OPCODE_NAMES[opcode]!!} ${handleBareType(type)}")
        }

        override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
            lines.add("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name ${disassembleTypeDescriptor(descriptor)}")
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            lines.add("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name${disassembleMethodDescriptor(descriptor)}")
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
            lines.add("${OPCODE_NAMES[opcode]!!} ${getLabelName(label)}")
        }

        override fun visitLabel(label: Label) {
            lines.add("")
            lines.add("${getLabelName(label)}:")
        }

        override fun visitLdcInsn(value: Any) {
            lines.add("${OPCODE_NAMES[Opcodes.LDC]!!} ${disassembleConstArg(value)}")
        }

        override fun visitIincInsn(varIndex: Int, increment: Int) {
            lines.add("${OPCODE_NAMES[Opcodes.IINC]!!} $varIndex, [$increment]")
        }

        override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label, vararg labels: Label?) {
            todo(Opcodes.TABLESWITCH)
        }

        override fun visitLookupSwitchInsn(dflt: Label, keys: IntArray?, labels: Array<out Label>?) {
            todo(Opcodes.LOOKUPSWITCH)
        }

        override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {
            lines.add("${OPCODE_NAMES[Opcodes.MULTIANEWARRAY]!!} ${disassembleTypeDescriptor(descriptor)}, $numDimensions")
        }

        override fun visitVarInsn(opcode: Int, varIndex: Int) {
            lines.add("${OPCODE_NAMES[opcode]} $varIndex")
        }

        private fun todo(opcode: Int) {
            lines.add("// TODO unimplemented opcode $opcode")
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
