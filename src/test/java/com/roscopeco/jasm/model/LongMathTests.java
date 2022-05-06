package com.roscopeco.jasm.model;

public interface LongMathTests {
    double testL2d(final long in);
    float testL2f(final long in);
    int testL2i(final long in);

    long testLadd(final long d1, final long d2);
    long testLand(final long a, final long b);
    int testLcmp(final long a, final long b);
    long testLdiv(final long a, final long b);
    long testLmul(final long a, final long b);
    long testLneg(final long a);
    long testLor(final long a, final long b);
    long testLrem(final long a, final long b);
    long testLshl(final long a, final int b);
    long testLshr(final long a, final int b);
    long testLsub(final long a, final long b);
    long testLushr(final long a, final int b);
    long testLxor(final long a, final long b);
}
