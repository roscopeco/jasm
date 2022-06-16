package com.roscopeco.jasm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class JasmDisassemblingVisitor : ClassVisitor(Opcodes.ASM9) {
    private var access = 0
    private var name = ""

    fun output(): String = "${modifers(this.access)} class ${this.name}"

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

    private fun modifers(modifiers: Int): String {
        val modStrs = mutableListOf<String>()

        if (modifiers and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC) {
            modStrs.add("public")
        }

        return modStrs.joinToString(separator = " ")
    }
}