package com.roscopeco.jasm.e2e;

import com.roscopeco.jasm.model.Interface1;
import com.roscopeco.jasm.model.Interface2;
import com.roscopeco.jasm.model.Superclass;
import com.roscopeco.jasm.model.annotations.TestAnno2;
import com.roscopeco.jasm.model.annotations.TestAnnotation;
import com.roscopeco.jasm.model.annotations.TestEnum;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.roscopeco.jasm.TestUtil.assembleString;
import static com.roscopeco.jasm.TestUtil.defineClass;
import static com.roscopeco.jasm.TestUtil.disassemble;
import static com.roscopeco.jasm.TestUtil.doParseString;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertClass;
import static com.roscopeco.jasm.asserts.LexerParserAssertions.assertMember;
import static org.assertj.core.api.Assertions.assertThat;

public class DisassemblerE2ETests {
    @Test
    void shouldDisassembleEmptyClass() {
        final var source = disassemble("EmptyClass");
        final var test = doParseString(source);

        assertClass(test)
            .isPublic()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyClass");

        assertThat(test.classbody().member()).hasSize(1);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("<init>")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .label("label0:")
                .aload(0)
                .invokeSpecial("java/lang/Object", "<init>", "()V")
                .vreturn()
                .label("label1:")
                .noMoreCode();

        checkAssembleAndDefineClass(source, "EmptyClass");
    }

    @Test
    void shouldDisassembleEmptyClassWithDefaultAccess() {
        final var source = disassemble("EmptyDefaultClass");
        final var test = doParseString(source);

        assertClass(test)
            .isNotPublic()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyDefaultClass");

        assertThat(test.classbody().member()).hasSize(1);

        assertMember(test.classbody().member(0))
            .isMethod()
            .hasName("<init>")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .label("label0:")
                .aload(0)
                .invokeSpecial("java/lang/Object", "<init>", "()V")
                .vreturn()
                .label("label1:")
                .noMoreCode();

        checkAssembleAndDefineClass(source, "EmptyDefaultClass");
    }

    @Test
    void shouldDisassembleEmptyInterface() {
        final var source = disassemble("EmptyInterface");
        final var test = doParseString(source);

        assertClass(test)
            .isPublic()
            .isAbstract()
            .isInterface()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyInterface");

        assertThat(test.classbody()).isNull();

        checkAssembleAndDefineClass(source, "EmptyInterface");
    }

    @Test
    void shouldDisassembleEmptyEnum() {
        final var source = disassemble("EmptyEnum");
        final var test = doParseString(source);

        assertClass(test)
            .isPublic()
            .isEnum()
            .hasName("com/roscopeco/jasm/model/disasm/EmptyEnum");

        // Not testing the body here, as it differs between Javac versions (11 and 17 do things differently for example)
        // Also not testing define due to tightened up rules around enums in modern Java (trivial rename doesn't work)
    }

    @Test
    void shouldDisassembleClassWithTryCatch() {
        final var source = disassemble("ExceptionTest");
        final var test = doParseString(source);
        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("()V")
            .hasCodeSequence()
                .exception("label0", "label1", "label1", "java/lang/InterruptedException")
                .label("label0:")
                    .anew("java/lang/InterruptedException")
                    .dup()
                    .ldcStr("BANG")
                    .invokeSpecial("java/lang/InterruptedException", "<init>", "(Ljava/lang/String;)V")
                    .athrow()
                .label("label1:")
                    .astore(1)
                .label("label2:")
                    .invokeStatic("java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false)
                    .invokeVirtual("java/lang/Thread", "interrupt", "()V", false)
                .label("label3:")
                    .vreturn()
                .label("label4:")
                    .noMoreCode();

        checkAssembleAndDefineClass(source, "ExceptionTest");
    }

    @Test
    void shouldDisassembleClassWithLookupSwitch() {
        final var source = disassemble("LookupSwitchTest");
        final var test = doParseString(source);

        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("(I)V")
            .hasCodeSequence()
                .label("label0:")
                .iload(1)
                .lookupswitch()
                    .withDefault("label1")
                    .withCase(1, "label2")
                    .withCase(10000, "label3")
                    .withCase(2000000, "label4")
                // Not interested in the rest
            ;

        checkAssembleAndDefineClass(source, "LookupSwitchTest");
    }

    @Test
    void shouldDisassembleClassWithTableSwitch() {
        final var source = disassemble("TableSwitchTest");
        final var test = doParseString(source);

        assertMember(test.classbody().member(1))
            .isMethod()
            .hasName("test")
            .hasDescriptor("(I)V")
            .hasCodeSequence()
                .label("label0:")
                .iload(1)
                .tableswitch()
                    .withDefault("label1")
                    .withCase(1, "label2")
                    .withCase(2, "label3")
                    .withCase(3, "label1")
                    .withCase(4, "label1")
                    .withCase(5, "label4")
                // Not interested in the rest
        ;

        checkAssembleAndDefineClass(source, "TableSwitchTest");
    }

    @Test
    void shouldDisassembleClassWithFields() {
        final var source = disassemble("ClassWithFields");
        final var test = doParseString(source);

        assertThat(test.classbody().member()).hasSize(10);

        checkAssembleAndDefineClass(source, "ClassWithFields");
    }

    @Test
    void shouldDisassembleInvokeDynamic() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final var source = disassemble("InvokeDynamicTest");
        final var test = doParseString(source);

        assertThat(test.classbody().member()).hasSize(2);

        final var clz = checkAssembleAndDefineClass(source, "InvokeDynamicTest");
        final var result = clz.getMethod("test").invoke(null);

        assertThat(result).isEqualTo("Hello World");
    }

    @Test
    void shouldDisassembleLdcTests() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final var source = disassemble("LdcTests");
        final var test = doParseString(source);

        assertThat(test.classbody().member()).hasSize(6);

        final var clz = checkAssembleAndDefineClass(source, "LdcTests");

        var result = clz.getMethod("returnString").invoke(null);
        assertThat(result).isEqualTo("Testing");

        result = clz.getMethod("returnInt").invoke(null);
        assertThat(result).isEqualTo(100000);

        result = clz.getMethod("returnLong").invoke(null);
        assertThat(result).isEqualTo(100L);

        result = clz.getMethod("returnFloat").invoke(null);
        assertThat(result).isEqualTo(9.5f);

        result = clz.getMethod("returnDouble").invoke(null);
        assertThat(result).isEqualTo(10.2d);
    }

    @Test
    void shouldDisassembleExtendsImplements() {
        final var source = disassemble("ExtendsImplementsTest");
        final var test = doParseString(source);

        assertThat(test.classbody().member()).hasSize(1);

        final var clz = checkAssembleAndDefineClass(source, "ExtendsImplementsTest");

        assertThat(clz.getSuperclass()).isEqualTo(Superclass.class);
        assertThat(clz.getInterfaces())
            .containsExactlyInAnyOrder(Interface1.class, Interface2.class);
    }

    @Test
    void shouldDisassembleAnnotatedClass() throws NoSuchFieldException, NoSuchMethodException {
        final var source = disassemble("AnnotatedClass");

        final var clz = checkAssembleAndDefineClass(source, "AnnotatedClass");

        final var otherField = clz.getDeclaredField("otherField");
        assertThat(otherField).isNotNull();

        final var fieldAnnotation = otherField.getAnnotation(Deprecated.class);
        assertThat(fieldAnnotation).isNotNull();
        assertThat(fieldAnnotation.since()).isEqualTo("2002");

        final var method = clz.getDeclaredMethod("test", int.class, String.class);
        assertThat(method).isNotNull();
        assertThat(method.getAnnotations()).hasSize(1);

        final var methodAnnotation = method.getAnnotation(TestAnnotation.class);
        assertThat(methodAnnotation).isNotNull();
        assertThat(methodAnnotation.stringArg()).isEqualTo("Changed"); /* Explicitly specified arguments */
        assertThat(methodAnnotation.classArg()).isEqualTo(List.class);
        assertThat(methodAnnotation.arrayArg()).containsExactly("one", "two");
        assertThat(methodAnnotation.enumArg()).isEqualTo(TestEnum.THREE);

        final var anno2 = (TestAnno2)methodAnnotation.annotationParameter();
        assertThat(anno2.value()).isEqualTo("Test Value 2");

        final var annotations = method.getParameterAnnotations();

        assertThat(annotations[0]).hasSize(1);
        var annotation = annotations[0][0];
        assertThat(annotation.annotationType()).isEqualTo(TestAnnotation.class);
        var testAnnotation = (TestAnnotation)annotation;
        assertThat(testAnnotation.classArg()).isEqualTo(Object.class); /* default */

        assertThat(annotations[1]).hasSize(2);
        annotation = annotations[1][0];
        assertThat(annotation.annotationType()).isEqualTo(Deprecated.class);
        final var deprecatedAnnotation = (Deprecated)annotation;
        assertThat(deprecatedAnnotation.since()).isEqualTo("3003");

        annotation = annotations[1][1];
        assertThat(annotation.annotationType()).isEqualTo(TestAnnotation.class);
        testAnnotation = (TestAnnotation)annotation;
        assertThat(testAnnotation.classArg()).isEqualTo(Object.class); /* default */
    }

    // https://github.com/roscopeco/jasm/issues/40
    @Test
    void shouldRoundTripNullTypeFinallyTryCatchCorrectly() throws Throwable {
        final var source = disassemble("FinallyClass");

        assertThat(source).contains("exception label0, label1, label2" + System.lineSeparator());

        final var clz = checkAssembleAndDefineClass(source, "FinallyClass");
        final var method = MethodHandles.lookup().findVirtual(clz, "theMethod", MethodType.methodType(String.class));
        final var obj = clz.getDeclaredConstructor().newInstance();

        final var result = method.invoke(obj);

        assertThat(result).isEqualTo("Finally");
    }

    /*
     * TODO leaving this here for when we migrate to Java 17
     *
     * Relies on a RecordClass in the model.disasm package:
     *
     * public record RecordClass<T>(T thing, String other) { }

    @Test
    void shouldRoundTripRecordsCorrectly() throws Throwable {
        final var source = disassemble("RecordClass");

        System.out.println(source);

        final var clz = checkAssembleAndDefineClass(source, "RecordClass");

        final var obj1 = clz.getDeclaredConstructor(Object.class, String.class).newInstance("Hello", "World");
        final var obj2 = clz.getDeclaredConstructor(Object.class, String.class).newInstance("Yolo", "World");
        final var obj3 = clz.getDeclaredConstructor(Object.class, String.class).newInstance("Hello", "World");

        assertThat(obj1.equals(obj2)).isFalse();
        assertThat(obj1.equals(obj3)).isTrue();
        assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
        assertThat(obj1.hashCode()).isEqualTo(obj3.hashCode());
        assertThat(obj1.toString()).isEqualTo("RecordClassTest0000[thing=Hello, other=World]");
        assertThat(obj2.toString()).isEqualTo("RecordClassTest0000[thing=Yolo, other=World]");
        assertThat(obj3.toString()).isEqualTo("RecordClassTest0000[thing=Hello, other=World]");
    }

    */

    @Test
    void shouldRoundTripInterfaceCorrectly() {
        final var source = disassemble("Interface");

        assertThat(source).contains("public abstract interface class");
        assertThat(source).contains("public abstract test()java/lang/String" + System.lineSeparator());

        final var clz = checkAssembleAndDefineClass(source, "Interface");

        assertThat(clz).isInterface();
        assertThat(clz).hasPublicMethods("test");
    }

    @Test
    void shouldRoundTripInterfaceWithDefaultCorrectly() throws NoSuchMethodException {
        final var source = disassemble("InterfaceWithDefault");

        System.out.println(source);

        assertThat(source).contains("public abstract interface class");
        assertThat(source).contains("public abstract getString()java/lang/String" + System.lineSeparator());
        assertThat(source).contains("public doStuff()V {" + System.lineSeparator());

        final var clz = checkAssembleAndDefineClass(source, "InterfaceWithDefault");

        assertThat(clz).isInterface();
        assertThat(clz).hasPublicMethods("getString", "doStuff");
    }

    @Test
    void shouldRoundTripEnumCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final var source = disassemble("AnEnum");

        assertThat(source).contains("public enum class");

        final var clz = checkAssembleAndDefineClass(source, "AnEnum");

        assertThat(clz.isEnum()).isTrue();
        assertThat(clz).hasPublicMethods("values", "getI");

        final var values = clz.getMethod("values").invoke(null);
        final var one = Array.get(values, 0);
        final var two = Array.get(values, 1);

        assertThat(clz.getMethod("getI").invoke(one)).isEqualTo(1);
        assertThat(clz.getMethod("getI").invoke(two)).isEqualTo(2);
    }

    private Class<?> checkAssembleAndDefineClass(final String source, final String name) {
        final var newSource = source.replace("com/roscopeco/jasm/model/disasm/" + name, "com/roscopeco/jasm/" + name + "Test0000");
        final var clz = defineClass(assembleString(newSource, Opcodes.V11));

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm." + name + "Test0000");

        return clz;
    }
}
