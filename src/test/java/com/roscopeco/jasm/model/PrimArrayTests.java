package com.roscopeco.jasm.model;

public interface PrimArrayTests {
    void testBipushBastore(final byte[] in);
    byte testBaload(final byte[] in);
    void testCastore(final char[] in);
    char testCaload(final char[] in);
    void testDastore(final double[] in, final double testval);
    double testDaload(final double[] in);
    void testFastore(final float[] in, final float testval);
    float testFaload(final float[] in);
    void testIastore(final int[] in, final int testval);
    int testIaload(final int[] in);
    void testLastore(final long[] in, final long testval);
    long testLaload(final long[] in);

    int[] testNewarray(int size);
}
