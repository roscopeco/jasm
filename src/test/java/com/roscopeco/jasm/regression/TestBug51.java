package com.roscopeco.jasm.regression;

import com.roscopeco.jasm.JasmDisassemblingVisitor;
import com.roscopeco.jasm.errors.StandardErrorCollector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

import static com.roscopeco.jasm.TestUtil.assembleString;
import static com.roscopeco.jasm.TestUtil.disassembleFullName;
import static org.assertj.core.api.Assertions.fail;
import static org.objectweb.asm.Opcodes.V11;

public class TestBug51 {
    @Test
    void testBug51_roundTrip() {
        final var disasm = disassembleFullName(
            new JasmDisassemblingVisitor("RasterPrinterJob", new StandardErrorCollector()),
            "/classes/RasterPrinterJob.class"
        );

        try {
            assembleString(disasm, V11);
        } catch (Throwable t) {
            try (final var pw = new PrintWriter("dump.jasm")) {
                pw.write(disasm);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            fail("Failed: " + t);
        }
    }
}
