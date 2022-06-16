package com.roscopeco.jasm.e2e;

import com.roscopeco.jasm.JasmDisassemblingVisitor;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import static com.roscopeco.jasm.TestUtil.loadDisasmTestClass;
import static org.assertj.core.api.Assertions.assertThat;

public class DisassemblerE2ETests {
    @Test
    void shouldDisassembleEmptyClass() {
        assertThat(disassemblerTest("EmptyClass")).isEqualTo(
            "public class com/roscopeco/jasm/model/disasm/EmptyClass"
        );
    }

    private String disassemblerTest(final String testCase) {
        return disassemblerTest(new JasmDisassemblingVisitor(), testCase);
    }

    private String disassemblerTest(final JasmDisassemblingVisitor disassembler, final String testCase) {
        final var clz = loadDisasmTestClass(testCase);
        clz.accept(disassembler, ClassReader.SKIP_FRAMES);
        return disassembler.output();
    }
}
