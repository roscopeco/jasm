package com.roscopeco.jasm.tool

import java.io.File
import java.nio.file.Paths

object Tasks {
    @JvmStatic
    fun createTasks(args: ToolArgs) = createTasks(args, File::exists)

    @JvmStatic
    fun createTasks(args: ToolArgs, exists: (File) -> Boolean) = args.inputFiles
        .asSequence()
            .map { Pair(it, Paths.get(args.inputDirectory, it)) }
            .map { Pair(it.first, it.second.toFile()) }
            .onEach { if (!exists(it.second)) println("\u001B[1;33mWARN:\u001B[0m Input file ${it.second.name} not found!")}
            .filter { exists(it.second) }
            .map {
                if (args.disassmbly) {
                    DisassembleTask(
                        it.second,
                        Paths.get(args.outputDirectory, fixJasmExtension(it.first)).toFile(),
                    )
                } else {
                    AssembleTask(
                        it.second,
                        Paths.get(args.outputDirectory, fixClassExtension(it.first)).toFile(),
                        args.target
                    )
                }
            }
            .toList()

    private fun fixClassExtension(input: String) = with (File(input)) {
        Paths.get(parent ?: "", "$nameWithoutExtension.class").toString()
    }

    private fun fixJasmExtension(input: String) = with (File(input)) {
        Paths.get(parent ?: "", "$nameWithoutExtension.jasm").toString()
    }
}