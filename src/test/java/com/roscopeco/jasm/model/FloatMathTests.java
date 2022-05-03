package com.roscopeco.jasm.model;

public interface FloatMathTests {
    double testF2d(final float in);
    int testF2i(final float in);
    long testF2l(final float in);

    float testFadd(final float d1, final float d2);
    float testFconst0();
    float testFconst1();
    int testFcmpg(final float a, final float b);
    int testFcmpl(final float a, final float b);

    float testFdiv(final float a, final float b);
    float testFmul(final float a, final float b);
    float testFneg(final float a);
    float testFrem(final float a, final float b);
    float testFsub(final float a, final float b);
}
