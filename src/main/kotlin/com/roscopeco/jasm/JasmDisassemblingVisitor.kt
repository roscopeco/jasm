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

class JasmDisassemblingVisitor(private val modifiers: Modifiers) : ClassVisitor(Opcodes.ASM9) {
    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()

        private val OPCODE_NAMES = mapOf(
            Opcodes.ALOAD to "aload",
            Opcodes.ANEWARRAY to "anewarray",
            Opcodes.ARETURN to "areturn",
            Opcodes.ASTORE to "astore",
            Opcodes.CHECKCAST to "checkcast",
            Opcodes.DLOAD to "dload",
            Opcodes.DSTORE to "dstore",
            Opcodes.FLOAD to "fload",
            Opcodes.FSTORE to "fstore",
            Opcodes.GETFIELD to "getfield",
            Opcodes.GETSTATIC to "getstatic",
            Opcodes.ICONST_0 to "iconst 0",
            Opcodes.ILOAD to "iload",
            Opcodes.INSTANCEOF to "instanceof",
            Opcodes.INVOKEINTERFACE to "invokeinterface",
            Opcodes.INVOKESPECIAL to "invokespecial",
            Opcodes.INVOKESTATIC to "invokestatic",
            Opcodes.INVOKEVIRTUAL to "invokevirtual",
            Opcodes.ISTORE to "istore",
            Opcodes.LDC to "ldc",
            Opcodes.LLOAD to "lload",
            Opcodes.LSTORE to "lstore",
            Opcodes.NEW to "new",
            Opcodes.PUTFIELD to "putfield",
            Opcodes.PUTSTATIC to "putstatic",
            Opcodes.RET to "ret",
            Opcodes.RETURN to "return",
        )
    }
    
    private var access = 0
    private var name = ""
    private var methods = mutableListOf<JasmDisassemblingMethodVisitor>()

    constructor() : this(Modifiers())

    fun output() = output(SpaceIndenter(4))

    fun output(indenter: Indenter): String = classHeader(indenter) + classBody(indenter) + LINE_SEPARATOR
    
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
        private var nextLabelNum = 0;
        private val lines = mutableListOf<String>()

        fun output(indenter: Indenter): String
                = methodHeader(indenter) + methodBody(indenter.indent()) + indenter.indented("}")

        fun methodHeader(indenter: Indenter): String
                = indenter.indented("${formattedModifiers(access)}$name${disassembleDescriptor(descriptor)} {\n")

        fun methodBody(indenter: Indenter): String {
            return if (lines.isNotEmpty())
                lines.joinToString(LINE_SEPARATOR) { indenter.indented(it) } + LINE_SEPARATOR
            else
                ""
        }

        override fun visitInsn(opcode: Int) {
            lines.add(OPCODE_NAMES[opcode] ?: "// TODO unimplemented opcode $opcode")
        }

        override fun visitIntInsn(opcode: Int, operand: Int) {
            todo(opcode)
        }

        override fun visitTypeInsn(opcode: Int, type: String) {
            lines.add("${OPCODE_NAMES[opcode]} ${handleBareType(type)}")
        }

        override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
            lines.add("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name ${disassembleType(descriptor)}")
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            lines.add("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.$name${disassembleDescriptor(descriptor)}")
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
            todo(opcode)
        }

        override fun visitLabel(label: Label) {
            lines.add("${getLabelName(label)}:")
        }

        override fun visitLdcInsn(value: Any) {
            lines.add("${OPCODE_NAMES[Opcodes.LDC]} ${disassembleConstArg(value)}")
        }

        override fun visitIincInsn(varIndex: Int, increment: Int) {
            todo(Opcodes.IINC)
        }

        override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label, vararg labels: Label?) {
            todo(Opcodes.TABLESWITCH)
        }

        override fun visitLookupSwitchInsn(dflt: Label, keys: IntArray?, labels: Array<out Label>?) {
            todo(Opcodes.LOOKUPSWITCH)
        }

        override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {
            todo(Opcodes.MULTIANEWARRAY)
        }

        override fun visitVarInsn(opcode: Int, varIndex: Int) {
            lines.add("${OPCODE_NAMES[opcode]} $varIndex")
        }

        private fun todo(opcode: Int) {
            lines.add("// TODO unimplemented opcode $opcode")
        }

        private fun formattedModifiers(modifierBitmap: Int): String {
            val modsStr = modifiers.disassembleClassModifiers(modifierBitmap)

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
                bareType;
            }
        }

        private fun getLabelName(label: Label) = labels.getOrPut(label) { "label${nextLabelNum++}" }

        private fun disassembleConstArg(arg: Any): String = when (arg) {
            // TODO escapes etc!
            is String -> "\"${arg}\""
            is Type -> arg.internalName
            else -> TODO("Const arg type '${arg.javaClass}' not yet supported")
        }

        private fun disassembleDescriptor(descriptor: String): String {
            val ctx = JvmTypesParser(CommonTokenStream(JvmTypesLexer(CharStreams.fromString(descriptor)))).method_descriptor()
            return "(${ctx.param().joinToString(", ") { disassembleSingleType(it.type()) }})${disassembleSingleType(ctx.return_().type())}"
        }

        private fun disassembleType(type: String): String {
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
