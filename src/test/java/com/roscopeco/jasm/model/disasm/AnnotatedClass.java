package com.roscopeco.jasm.model.disasm;

import com.roscopeco.jasm.model.annotations.TestAnno2;
import com.roscopeco.jasm.model.annotations.TestAnnotation;
import com.roscopeco.jasm.model.annotations.TestEnum;

import java.util.List;

@Deprecated(since = "1001")
public class AnnotatedClass {
    @Deprecated(since = "2002")
    public int otherField;

    @TestAnnotation(stringArg = "Changed", classArg = List.class, arrayArg = { "one" , "two" }, enumArg = TestEnum.THREE, annotationParameter = @TestAnno2("Test Value 2"))
    public void test(@TestAnnotation int arg1, @Deprecated(since = "3003") @TestAnnotation String arg2) {

    }
}
