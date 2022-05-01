package com.roscopeco.jasm.model;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public interface LdcAconstAreturn {
    Object testAconstNull();

    String testLdcString();

    int testLdcInt();

    float testLdcFloat();

    boolean testLdcBool();

    Class<?> testLdcClass();

    MethodType testLdcMethodType();

    MethodHandle testLdcMethodHandle();

    String testLdcDynamicConst();
}
