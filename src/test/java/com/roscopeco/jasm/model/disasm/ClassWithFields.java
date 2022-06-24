package com.roscopeco.jasm.model.disasm;

public class ClassWithFields {
    public String publicInstanceField;
    private int privateInstanceField;
    Object defaultField;
    public static long staticLongField;
    private static Object[] staticObjectArrayField;
    protected String protectedField;

    public static final String staticStringFieldWithInit = "INIT";
    public static final int staticIntFieldWithInit = 42;

    // TODO not yet supported by JASM
    //  public static final long staticLongFieldWithInit = 42;

    public static final float staticFloatFieldWithInit = 9001.0f;

    // TODO not yet supported by JASM
    //  public static final double staticDoubleFieldWithInit = 900.2;
}
