package com.roscopeco.jasm.tool

import java.io.File

abstract class FileTransformTask<R>(val src: File, val dest: File) : Task<R>