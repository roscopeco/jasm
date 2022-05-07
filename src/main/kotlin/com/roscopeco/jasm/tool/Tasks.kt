package com.roscopeco.jasm.tool

import java.io.File
import java.nio.file.Paths

object Tasks {
    @JvmStatic
    fun createTasks(args: ToolArgs) = createTasks(args, File::exists)

    @JvmStatic
    fun createTasks(args: ToolArgs, exists: (File) -> Boolean) = args.inputFiles
            .map { Pair(it, Paths.get(args.inputDirectory, it)) }
            .map { Pair(it.first, it.second.toFile()) }
            .onEach { if (!exists(it.second)) println("\u001B[1;33mWARN:\u001B[0m Input file ${it.second.name} not found!")}
            .filter { exists(it.second) }
            .map { AssembleTask(it.second, Paths.get(args.outputDirectory, fixClassExtension(it.first)).toFile(), args.target) }

    private fun fixClassExtension(input: String) = with (File(input)) {
        Paths.get(parent ?: "", "$nameWithoutExtension.class").toString()
    }
}