package com.roscopeco.jasm.model;

public interface IfNullNonNullTest {
    boolean testIfNullWhenNullPasses();
    boolean testIfNullWhenNonNullPasses();
    boolean testIfNonNullWhenNullPasses();
    boolean testIfNonNullWhenNonNullPasses();
}
