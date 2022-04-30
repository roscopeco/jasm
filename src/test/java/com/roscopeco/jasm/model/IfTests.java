package com.roscopeco.jasm.model;

public interface IfTests {
    boolean testEqWhenEqualPasses();
    boolean testEqNotEqualPasses();
    boolean testGeWhenLessPasses();
    boolean testGeWhenEqualPasses();
    boolean testGeWhenGreaterPasses();
    boolean testGtWhenLessPasses();
    boolean testGtWhenGreaterPasses();
    boolean testLeWhenLessPasses();
    boolean testLeWhenEqualPasses();
    boolean testLeWhenGreaterPasses();
    boolean testLtWhenLessPasses();
    boolean testLtWhenGreaterPasses();
    boolean testNeWhenEqualPasses();
    boolean testNeNotEqualPasses();
}
