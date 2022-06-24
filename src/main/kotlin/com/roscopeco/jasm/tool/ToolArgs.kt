package com.roscopeco.jasm.tool

import com.beust.jcommander.Parameter
import org.objectweb.asm.Opcodes

class ToolArgs {
    @Parameter(names = ["-h", "--help"], description =  "Display usage and quit")
    var showHelp: Boolean = false

    @Parameter(names = ["-i", "--input"], description = "Input base directory for source files")
    var inputDirectory: String = "."

    @Parameter(names = ["-o", "--output"], description = "Output base directory for class files")
    var outputDirectory: String = "."

    @Parameter(names = ["-d", "--disassemble"], description = "Disassemble Java class files")
    var disassmbly: Boolean = false

    @Parameter(names = ["-l", "--line-numbers"], description = "Emit line number comments in disassembly")
    var emitLineNumbers: Boolean = false

    @Parameter(names = ["-target", "--target"], description = "Generate class files suitable for the specified class format versionx")
    var target: Int = Opcodes.V11;

    @Parameter(description = "List of files to assemble (names relative to input directory)")
    var inputFiles: List<String> = mutableListOf()
}