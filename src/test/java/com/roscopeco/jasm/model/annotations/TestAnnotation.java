package com.roscopeco.jasm.model.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
    String stringArg() default  "Hello";
    Class<?> classArg() default Object.class;

    String[] arrayArg() default {};

    TestEnum enumArg() default TestEnum.ONE;
}
