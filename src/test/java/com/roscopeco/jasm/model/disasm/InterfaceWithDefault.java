package com.roscopeco.jasm.model.disasm;

public interface InterfaceWithDefault {
    String getString();

    default void doStuff() {
    }
}
