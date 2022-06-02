/*
 * Copyright (c)2022 Ross Bamford & Contributors
 *
 * Licensed under the MIT license. See LICENSE.md for details.
 */
module com.roscopeco.jasm {
    requires kotlin.stdlib;
    requires org.antlr.antlr4.runtime;
    requires transitive org.objectweb.asm;
    requires com.beust.jcommander;

    exports com.roscopeco.jasm;
    exports com.roscopeco.jasm.antlr;
    exports com.roscopeco.jasm.tool;
    exports com.roscopeco.jasm.errors;

    opens com.roscopeco.jasm.tool to com.beust.jcommander;
}