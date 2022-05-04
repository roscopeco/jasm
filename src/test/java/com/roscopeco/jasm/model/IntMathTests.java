package com.roscopeco.jasm.model;

public interface IntMathTests {
    byte testI2b(final int in);
    char testI2c(final int in);
    double testI2d(final int in);
    float testI2f(final int in);
    long testI2l(final int in);
    short testI2s(final int in);

    int testIadd(final int d1, final int d2);
    int testIand(final int a, final int b);
    int testIdiv(final int a, final int b);
    int testIinc(final int a);
    int testImul(final int a, final int b);
    int testIneg(final int a);
    int testIor(final int a, final int b);
    int testIrem(final int a, final int b);
    int testIshl(final int a, final int b);
    int testIshr(final int a, final int b);
    int testIsub(final int a, final int b);
    int testIushr(final int a, final int b);
    int testIxor(final int a, final int b);
}
