package com.roscopeco.jasm

import com.roscopeco.jasm.antlr.JvmTypesLexer
import com.roscopeco.jasm.antlr.JvmTypesParser
import com.roscopeco.jasm.errors.DisassemblyContext
import com.roscopeco.jasm.errors.DisassemblyError
import com.roscopeco.jasm.errors.ErrorCollector
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ConstantDynamic
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class JasmDisassemblingVisitor(
    private val modifiers: Modifiers,
    private val unitName: String,
    private val lineNumbers: Boolean,
    private val errorCollector: ErrorCollector
) : ClassVisitor(Opcodes.ASM9) {
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

        private val HANDLE_TAGS = listOf(
            "<DISASMBUG>",
            "getfield",
            "getstatic",
            "putfield",
            "putstatic",
            "invokevirtual",
            "invokestatic",
            "invokespecial",
            "newinvokespecial",
            "invokeinterface"
        )

        private val NEWARRAY_TYPES = listOf(
            "<DISASMBUG>",
            "<DISASMBUG>",
            "<DISASMBUG>",
            "<DISASMBUG>",
            "Z",
            "C",
            "F",
            "D",
            "B",
            "S",
            "I",
            "J"
        )
    }
    
    private var access = 0
    private var name = ""
    private var originalSourceName = "<unknown>"
    private val annotations = mutableListOf<JasmDisassemblingAnnotationVisitor>()
    private val methods = mutableListOf<JasmDisassemblingMethodVisitor>()
    private val fields = mutableListOf<JasmDisassemblingFieldVisitor>()
    private var debug = ""
    private var originalVersion = 0
    private var signature = "<no signature>"
    private var supername = ""
    private val interfaces = mutableListOf<String>()

    constructor(unitName: String, errorCollector: ErrorCollector) : this(unitName, false, errorCollector)
    constructor(unitName: String, lineNumbers: Boolean, errorCollector: ErrorCollector) : this(Modifiers(), unitName, lineNumbers, errorCollector)

    fun output() = output(SpaceIndenter(4))

    fun output(indenter: Indenter) =
            fileHeader(indenter) +
            classAnnotations(indenter) +
            classHeader(indenter) +
            classBody(indenter) +
            LINE_SEPARATOR

    private fun fileHeader(indenter: Indenter): String
            = indenter.indented("/*${LINE_SEPARATOR}") +
                    indenter.indented(" * Disassembled from $unitName (originally $originalSourceName) by JASM${LINE_SEPARATOR}") +
                    indenter.indented(" *") + LINE_SEPARATOR +
                    indenter.indented(" * Original class version: $originalVersion") + LINE_SEPARATOR +
                    indenter.indented(" * Signature: $signature") + LINE_SEPARATOR +
                    indenter.indented(" */${LINE_SEPARATOR}")

    private fun classAnnotations(indenter: Indenter): String {
        return if (annotations.isNotEmpty()) {
            annotations.joinToString(separator = "\n") { it.output(indenter) } + "\n"
        } else {
            ""
        }
    }

    private fun classHeader(indenter: Indenter) = indenter.indented("${formattedModifiers(this.access)}class ${
        LiteralNames.escape(this.name)
    }${extends()}${implements()}")

    private fun extends(): String {
        if (this.supername.isEmpty() || this.supername.isBlank() || this.supername == "java/lang/Object") {
            return ""
        } else {
            return " extends ${LiteralNames.escape(this.supername)}"
        }
    }

    private fun implements(): String {
        if (this.interfaces.isEmpty()) {
            return ""
        } else {
            return " implements ${this.interfaces.joinToString(", ") { LiteralNames.escape(it) } }"
        }
    }

    private fun classBody(indenter: Indenter): String {
        return if (methods.isEmpty() && fields.isEmpty()) {
            ""
        } else {
            " {\n${classMembers(indenter.indent())}$LINE_SEPARATOR}"
        }
    }

    private fun classMembers(indenter: Indenter): String {
        val methods = methods.joinToString(LINE_SEPARATOR + LINE_SEPARATOR) { it.output(indenter) }
        val fields = fields.joinToString(LINE_SEPARATOR + LINE_SEPARATOR) { it.output(indenter) }

        var out = fields
        if (out.isNotEmpty() && out.isNotBlank()) {
            out += LINE_SEPARATOR + LINE_SEPARATOR
        }
        return out + methods
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.originalVersion = version
        this.name = name!!
        this.access = access
        this.signature = signature ?: "<no signature>"
        this.supername = superName ?: ""
        this.interfaces.addAll(interfaces ?: emptyArray())
    }

    override fun visitSource(source: String?, debug: String?) {
        this.originalSourceName = source ?: "<unknown>"
        this.debug = debug ?: ""
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        return if (visible && descriptor != null) {
            val visitor = JasmDisassemblingAnnotationVisitor(descriptor)
            annotations.add(visitor)
            visitor
        } else {
            null
        }
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        val visitor = JasmDisassemblingFieldVisitor(access, name, descriptor, signature, value)
        fields.add(visitor)
        return visitor
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

    // Manually fix up bare types that may be ref arrays
    private fun handleBareType(bareType: String): String {
        return LiteralNames.escape(if (bareType.contains("[")) {
            val lastLSquare = bareType.lastIndexOf("[")
            bareType.substring(0, lastLSquare + 1) + bareType.substring(lastLSquare + 1, bareType.length - 1)
        } else {
            bareType
        })
    }

    private fun disassembleConstArg(arg: Any?, indenter: Indenter): String = when (arg) {
        null -> "null" // This should probably never happen!
        is String -> "\"${escapeString(arg)}\""
        is Number -> disassembleNumber(arg)
        is Type -> if (arg.sort == Type.METHOD) disassembleMethodDescriptor(arg.descriptor) else handleBareType(arg.internalName)
        is Handle -> disassembleMethodHandle(arg)
        is ConstantDynamic -> disassembleConstDynamic(arg, indenter)
        else -> {
            errorCollector.addError(DisassemblyError(unitName, DisassemblyContext.ConstArg, "Unexpected const arg type ${arg.javaClass}"))
            "null"  // TODO this isn't useful...
        }
    }

    private fun disassembleNumber(arg: Number): String {
        return if (arg is java.lang.Integer) {
            arg.toString()
        } else if (arg is java.lang.Long) {
            "${arg}L"
        } else if (arg is java.lang.Float) {
            arg.toString()
        } else if (arg is java.lang.Double) {
            "${arg}d"
        } else {
            errorCollector.addError(DisassemblyError(unitName, DisassemblyContext.ConstArg,
                "Unsupported number type ${arg.javaClass} [$arg]"))
            "0"
        }
    }

    private fun disassembleConstDynamic(arg: ConstantDynamic, indenter: Indenter): String {
        val block1 = indenter.indent()
        val bootstrapArguments = (0 until arg.bootstrapMethodArgumentCount).map { arg.getBootstrapMethodArgument(it) }.toTypedArray()

        return indenter.indented("constdynamic ${LiteralNames.escape(arg.name)} ${disassembleTypeDescriptor(arg.descriptor)} {$LINE_SEPARATOR") +
               block1.indented(disassembleMethodHandle(arg.bootstrapMethod)) + LINE_SEPARATOR +
               block1.indented(disassembleBootstrapArguments(block1, bootstrapArguments)) +
               indenter.indented("}$LINE_SEPARATOR")
    }

    private fun disassembleMethodHandle(handle: Handle): String {
        return when (handle.tag) {
            Opcodes.H_GETFIELD,
            Opcodes.H_GETSTATIC,
            Opcodes.H_PUTFIELD,
            Opcodes.H_PUTSTATIC -> "${HANDLE_TAGS[handle.tag]} ${handleBareType(handle.owner)}.${handle.name} ${disassembleTypeDescriptor(handle.desc)}"
            else -> "${HANDLE_TAGS[handle.tag]} ${handleBareType(handle.owner)}.${handle.name}${disassembleMethodDescriptor(handle.desc)}"
        }
    }

    private fun disassembleBootstrapArguments(indenter: Indenter, arguments: Array<out Any?>): String {
        return if (arguments.isNotEmpty()) {
            indenter.indented("[${
                arguments.joinToString(", ") { disassembleConstArg(it, indenter) }
            }]$LINE_SEPARATOR")
        } else {
            ""
        }
    }

    private fun disassembleMethodDescriptor(descriptor: String, annotations: List<List<JasmDisassemblingAnnotationVisitor>?>): String {
        val ctx = JvmTypesParser(CommonTokenStream(JvmTypesLexer(CharStreams.fromString(descriptor)))).method_descriptor()

        val params = ctx.param().mapIndexed { i, paramCtx ->
            "${annotations.getOrNull(i)?.joinToString(" ") { it.outputNonIndented() } ?: ""} ${disassembleSingleType(paramCtx.type())}".trim()
        }

        return "(${params.joinToString(", ")})${if (ctx.return_() != null) disassembleSingleType(ctx.return_().type()) else ""}"
    }

    private fun disassembleMethodDescriptor(descriptor: String) = disassembleMethodDescriptor(descriptor, emptyList())

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
            "$ary${LiteralNames.escape(name.substring(1, name.length - 1))}"
        } else if (ctx.VOID() != null) {
            "V"
        } else {
            errorCollector.addError(DisassemblyError(unitName, DisassemblyContext.Descriptor,
                "Probable bug: Unparseable type in descriptor ${ctx.text}; Replaced with void"))
            "V"
        }
    }

    private fun escapeString(value: String) = value.replace("\"", "\"\"")

    private interface CodeElement {
        fun generate(indenter: Indenter): String
    }

    private inner class Line(val line: String) : CodeElement {
        override fun generate(indenter: Indenter): String {
            return indenter.indented(line)
        }
    }

    private inner class LookupSwitch(val default: String, val keys: IntArray, val labels: List<String>) : CodeElement {
        override fun generate(indenter: Indenter): String {
            val blockIndent = indenter.indent()

            return indenter.indented("${OPCODE_NAMES[Opcodes.LOOKUPSWITCH]!!} $default {$LINE_SEPARATOR") +
                    keys.zip(labels).joinToString(",$LINE_SEPARATOR") { (key, label) -> blockIndent.indented("$key: $label") } +
                    LINE_SEPARATOR +
                    indenter.indented("}")
        }
    }

    private inner class TableSwitch(val default: String, val min: Int, val max: Int, val labels: List<String>) : CodeElement {
        override fun generate(indenter: Indenter): String {
            val blockIndent = indenter.indent()

            return indenter.indented("${OPCODE_NAMES[Opcodes.TABLESWITCH]!!} $default {$LINE_SEPARATOR") +
                    (min..max).zip(labels).joinToString(",$LINE_SEPARATOR") { (key, label) -> blockIndent.indented("$key: $label") } +
                    LINE_SEPARATOR +
                    indenter.indented("}")
        }
    }

    private inner class InvokeDynamic(
        val name: String,
        val descriptor: String,
        val bootstrapMethodHandle: Handle,
        val bootstrapMethodArguments: Array<out Any?>
    ) : CodeElement {
        override fun generate(indenter: Indenter): String {
            val blockIndent = indenter.indent()

            return indenter.indented("${OPCODE_NAMES[Opcodes.INVOKEDYNAMIC]!!} ${LiteralNames.escapeMethodName(name)}${disassembleMethodDescriptor(descriptor)} {$LINE_SEPARATOR") +
                    blockIndent.indented("${disassembleMethodHandle(bootstrapMethodHandle)}$LINE_SEPARATOR") +
                    disassembleBootstrapArguments(blockIndent, bootstrapMethodArguments) +
                    indenter.indented("}$LINE_SEPARATOR")
        }
    }

    private inner class Ldc(val value: Any): CodeElement {
        override fun generate(indenter: Indenter): String
                = indenter.indented("${OPCODE_NAMES[Opcodes.LDC]!!} ${disassembleConstArg(value, indenter)}")
    }

    private open inner class JasmDisassemblingAnnotationVisitor(
        private val descriptor: String
    ) : AnnotationVisitor(Opcodes.ASM9) {
        val params = mutableListOf<String>()

        fun output(indenter: Indenter) = indenter.indented(outputNonIndented())

        fun outputNonIndented() = "@${disassembleTypeDescriptor(descriptor)}${outputParams()}"

        private fun outputParams(): String {
            return if (params.isNotEmpty()) {
                "(${params.joinToString()})"
            } else {
                ""
            }
        }

        override fun visit(name: String?, value: Any?) {
            params.add("${name?.plus(" = ") ?: ""}${mapSingleElement(value)}")
        }

        override fun visitEnum(name: String, descriptor: String, value: String) {
            params.add("$name = [${LiteralNames.escape(disassembleTypeDescriptor(descriptor))}.$value]")
        }

        override fun visitAnnotation(name: String, descriptor: String)
                = JasmDisassemblingAnnotationParamVisitor(this, name, descriptor)

        override fun visitArray(name: String) = JasmDisassemblingArrayParamVisitor(this, name, descriptor)

        protected fun mapSingleElement(element: Any?) = when (element) {
            null            -> "null" // This should probably never happen!
            is String       -> "\"${element}\""
            is Number       -> disassembleNumber(element)
            is Type         -> handleBareType(element.internalName)
            else -> {
                errorCollector.addError(DisassemblyError(unitName, DisassemblyContext.ConstArg, "Unexpected array literal element type ${element.javaClass} (${element}"))
                ""
            }
        }
    }

    private inner class JasmDisassemblingAnnotationParamVisitor(
        val owner: JasmDisassemblingAnnotationVisitor,
        private val name: String,
        descriptor: String
    ) : JasmDisassemblingAnnotationVisitor(descriptor) {
        override fun visitEnd() {
            owner.params.add("$name = ${outputNonIndented()}")
            super.visitEnd()
        }
    }

    private inner class JasmDisassemblingArrayParamVisitor(
        val owner: JasmDisassemblingAnnotationVisitor,
        private val name: String,
        descriptor: String
    ) : JasmDisassemblingAnnotationVisitor(descriptor) {
        private val elements = mutableListOf<Any?>()

        override fun visit(name: String?, value: Any?) {
            elements.add(value)
        }

        override fun visitEnd() {
            owner.params.add("$name = { ${elements.map { mapSingleElement(it) }.joinToString() } }")
            super.visitEnd()
        }
    }

    private inner class JasmDisassemblingFieldVisitor(
        val access: Int,
        val name: String,
        val descriptor: String,
        val signature: String?,
        val value: Any?
    ) : FieldVisitor(Opcodes.ASM9) {
        private val annotations = mutableListOf<JasmDisassemblingAnnotationVisitor>()

        fun output(indenter: Indenter) = fieldComment(indenter) + fieldAnnotations(indenter) + fieldBody(indenter)

        private fun fieldComment(indenter: Indenter)
                = indenter.indented("// ${signature ?: "<no signature>"}$LINE_SEPARATOR")

        private fun fieldAnnotations(indenter: Indenter): String {
            return if (annotations.isNotEmpty()) {
                annotations.joinToString(separator = "\n") { it.output(indenter) } + "\n"
            } else {
                ""
            }
        }

        private fun fieldBody(indenter: Indenter)
                = indenter.indented("${modifiers.disassembleFieldModifiers(access)} ${LiteralNames.escape(name)} ${disassembleTypeDescriptor(descriptor)}") +
                        fieldInitializer()

        private fun fieldInitializer(): String {
            return if (value == null) {
                ""
            } else {
                return " = " + disassembleFieldInitializerValue(value)
            }
        }

        private fun disassembleFieldInitializerValue(value: Any) = when (value) {
            is String -> "\"${escapeString(value)}\""
            is Number -> disassembleNumber(value)
            else -> TODO("Unsupported field initializer ${value.javaClass}")
        }

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
            return if (visible && descriptor != null) {
                val visitor = JasmDisassemblingAnnotationVisitor(descriptor)
                annotations.add(visitor)
                visitor
            } else {
                null
            }
        }
    }

    private inner class JasmDisassemblingMethodVisitor(
        val access: Int,
        val name: String,
        val descriptor: String,
        val signature: String?,
        val exceptions: Array<out String>?
    ) : MethodVisitor(Opcodes.ASM9) {
        private val annotations = mutableListOf<JasmDisassemblingAnnotationVisitor>()
        private val parameterAnnotations = mutableListOf<MutableList<JasmDisassemblingAnnotationVisitor>?>()
        private val labels = mutableMapOf<Label, String>()
        private var nextLabelNum = 0
        private val blocks = mutableListOf<CodeElement>()

        fun output(indenter: Indenter) =
                methodComment(indenter) +
                methodAnnotations(indenter) +
                methodHeader(indenter) +
                methodBody(indenter.indent()) +
                indenter.indented("}")

        fun methodComment(indenter: Indenter): String
                = indenter.indented("// ${signature ?: "<no signature>"}$LINE_SEPARATOR") +
                    indenter.indented("// ${exceptions?.joinToString(", ") ?: "<no exceptions>"}$LINE_SEPARATOR")

        private fun methodAnnotations(indenter: Indenter): String {
            return if (annotations.isNotEmpty()) {
                annotations.joinToString(separator = "\n") { it.output(indenter) } + "\n"
            } else {
                ""
            }
        }

        fun methodHeader(indenter: Indenter): String
                = indenter.indented("${formattedModifiers(access)}${LiteralNames.escapeMethodName(name)}${
                    disassembleMethodDescriptor(descriptor, parameterAnnotations)
                } {\n")

        fun methodBody(indenter: Indenter): String {
            return if (blocks.isNotEmpty())
                blocks.joinToString(LINE_SEPARATOR) { it.generate(indenter) } + LINE_SEPARATOR
            else
                ""
        }

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
            return if (visible && descriptor != null) {
                val visitor = JasmDisassemblingAnnotationVisitor(descriptor)
                annotations.add(visitor)
                visitor
            } else {
                null
            }
        }

        override fun visitParameterAnnotation(
            parameter: Int,
            descriptor: String,
            visible: Boolean
        ): AnnotationVisitor? {
            return if (visible) {
                val visitor = JasmDisassemblingAnnotationVisitor(descriptor)
                val annotations = parameterAnnotations.getOrElse(parameter) { mutableListOf() }!!
                annotations.add(visitor)
                parameterAnnotations.add(parameter, annotations)
                visitor
            } else {
                null
            }
        }

        override fun visitTryCatchBlock(start: Label, end: Label, handler: Label, type: String?) {
            blocks.add(Line("exception ${getLabelName(start)}, ${getLabelName(end)}, ${getLabelName(handler)}${
                if (type != null) ", ${handleBareType(type)}" else ""
            }"))
        }

        override fun visitLineNumber(line: Int, start: Label?) {
            if (lineNumbers) {
                blocks.add(Line("// Line $line"))
            }
        }

        override fun visitInsn(opcode: Int) {
            blocks.add(Line(OPCODE_NAMES[opcode]!!))
        }

        override fun visitIntInsn(opcode: Int, operand: Int) {
            if (opcode == Opcodes.NEWARRAY) {
                // Handle this specially
                blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${NEWARRAY_TYPES[operand]}"))
            } else {
                blocks.add(Line("${OPCODE_NAMES[opcode]!!} $operand"))
            }
        }

        override fun visitTypeInsn(opcode: Int, type: String) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${handleBareType(type)}"))
        }

        override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.${LiteralNames.escape(name)} ${disassembleTypeDescriptor(descriptor)}"))
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String,
            name: String,
            descriptor: String,
            isInterface: Boolean
        ) {
            if (opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKESTATIC) {
                blocks.add(
                    Line(
                        "${OPCODE_NAMES[opcode]!!}${if (isInterface) "*" else ""} ${handleBareType(owner)}.${
                            LiteralNames.escapeMethodName(name)
                        }${
                            disassembleMethodDescriptor(
                                descriptor
                            )
                        }"
                    )
                )
            } else {
                blocks.add(
                    Line(
                        "${OPCODE_NAMES[opcode]!!} ${handleBareType(owner)}.${LiteralNames.escapeMethodName(name)}${
                            disassembleMethodDescriptor(
                                descriptor
                            )
                        }"
                    )
                )
            }
        }

        override fun visitInvokeDynamicInsn(
            name: String,
            descriptor: String,
            bootstrapMethodHandle: Handle,
            vararg bootstrapMethodArguments: Any?
        ) {
            blocks.add(InvokeDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments))
        }

        override fun visitJumpInsn(opcode: Int, label: Label) {
            blocks.add(Line("${OPCODE_NAMES[opcode]!!} ${getLabelName(label)}"))
        }

        override fun visitLabel(label: Label) {
            blocks.add(Line(""))
            blocks.add(Line("${getLabelName(label)}:"))
        }

        override fun visitLdcInsn(value: Any) {
            blocks.add(Ldc(value))
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

        private fun formattedModifiers(modifierBitmap: Int): String {
            val modsStr = modifiers.disassembleMethodModifiers(modifierBitmap)

            return if (modsStr.isNotEmpty())
                "$modsStr "
            else
                ""
        }

        private fun getLabelName(label: Label) = labels.getOrPut(label) { "label${nextLabelNum++}" }
    }
}
