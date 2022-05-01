package com.roscopeco.jasm.model;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TestBootstrap {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    /*
     * Used as constdynamic in invokedynamic and ldc tests.
     */
    public static final String DYNAMIC_CONST_FOR_TEST = "The expected result";

    private final String theResult;

    private TestBootstrap(final String theResult) {
        this.theResult = theResult;
    }

    /*
     * This is the method that ultimately implements the lambda for the basic
     * invokedynamic test.
     */
    public static String lambdaGetImpl() {
        return "The expected basic result";
    }

    /*
     * This is the static method handle used in the full invokedynamic test.
     * Also used in the LDC method handle test.
     */
    public static String staticForHandleTest() {
        return "Handle is good";
    }

    /*
     * This is the bootstrap method for the full invokedynamic test. It exercises all features
     * of invokedynamic, including dynamic constants.
     */
    public static CallSite testBootstrap(
            final MethodHandles.Lookup caller,
            final String name,
            final MethodType type,
            final int intArg,
            final float floatArg,
            final String stringArg,
            final Class<?> classArg,
            final MethodHandle methodHandleArg,
            final MethodType methodTypeArg,
            final String dynamicConstValue
    ) {
        assertThat(intArg).isEqualTo(42);
        assertThat(floatArg).isEqualTo(10.0f);
        assertThat(stringArg).isEqualTo("Bootstrap test");
        assertThat(classArg).isEqualTo(List.class);

        try {
            assertThat(methodHandleArg.invoke()).isEqualTo("Handle is good");
        } catch (Throwable t) {
            fail("Caught throwable when trying to invoke method handle: " + t);
        }

        assertThat(methodTypeArg.parameterList()).containsExactly(String.class);
        assertThat(methodTypeArg.returnType()).isEqualTo(int.class);

        try {

            return new ConstantCallSite(
                    lookup.findVirtual(TestBootstrap.class, "getImpl", MethodType.methodType(Object.class))
                            .bindTo(new TestBootstrap(dynamicConstValue)));

        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getImpl() {
        return this.theResult;
    }
}
