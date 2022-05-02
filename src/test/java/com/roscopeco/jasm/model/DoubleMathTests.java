package com.roscopeco.jasm.model;

public interface DoubleMathTests {
    float testD2f(final double in);
    int testD2i(final double in);
    long testD2l(final double in);

    double testDadd(final double d1, final double d2);
    double testDconst0();
    double testDconst1();
    int testDcmpg(final double a, final double b);
    int testDcmpl(final double a, final double b);
}
