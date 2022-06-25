package com.roscopeco.jasm.e2e;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.InvocationTargetException;

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
        System.out.println(disassemble("LdcTests"));
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
    private Class<?> checkAssembleAndDefineClass(final String source, final String name) {
        final var clz = defineClass(assembleString(
            source.replace("com/roscopeco/jasm/model/disasm/" + name, "com/roscopeco/jasm/" + name + "Test0000"), Opcodes.V11));

        assertThat(clz.getName()).isEqualTo("com.roscopeco.jasm." + name + "Test0000");

        return clz;
    }
}
