package com.roscopeco.jasm.tool;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ToolArgsTest {
    @Test
    void testShowHelpIsFalseByDefault() {
        final var args = doTest();
        assertThat(args.getShowHelp()).isFalse();
    }

    @Test
    void testShowHelpShortFlagWorks() {
        final var args = doTest("-h");
        assertThat(args.getShowHelp()).isTrue();
    }

    @Test
    void testShowHelpLongFlagWorks() {
        final var args = doTest("--help");
        assertThat(args.getShowHelp()).isTrue();
    }

    @Test
    void testInputDirectoryDefaultWorks() {
        final var args = doTest();
        assertThat(args.getInputDirectory()).isEqualTo(".");
    }

    @Test
    void testInputDirectoryExplicitShortWorks() {
        final var args = doTest("-i", "some/directory");
        assertThat(args.getInputDirectory()).isEqualTo("some/directory");
    }

    @Test
    void testInputDirectoryExplicitLongWorks() {
        final var args = doTest("--input", "some/directory");
        assertThat(args.getInputDirectory()).isEqualTo("some/directory");
    }

    @Test
    void testOutputDirectoryDefaultWorks() {
        final var args = doTest();
        assertThat(args.getOutputDirectory()).isEqualTo(".");
    }

    @Test
    void testOutputDirectoryExplicitShortWorks() {
        final var args = doTest("-o", "some/directory");
        assertThat(args.getOutputDirectory()).isEqualTo("some/directory");
    }

    @Test
    void testOutputDirectoryExplicitLongWorks() {
        final var args = doTest("--output", "some/directory");
        assertThat(args.getOutputDirectory()).isEqualTo("some/directory");
    }

    @Test
    void testTargetDefaultWorks() {
        final var args = doTest();
        assertThat(args.getTarget()).isEqualTo(Opcodes.V11);
    }

    @Test
    void testTargetExplicitShortWorks() {
        final var args = doTest("-target", "50");
        assertThat(args.getTarget()).isEqualTo(50);
    }

    @Test
    void testTargetExplicitLongWorks() {
        final var args = doTest("--target", "50");
        assertThat(args.getTarget()).isEqualTo(50);
    }

    @Test
    void testTargetNonIntegerWorks() {
        assertThatThrownBy(() -> doTest("-target", "NaN"))
            .isInstanceOf(ParameterException.class);
    }

    @Test
    void testNonOptionArgsAreGatheredAsFilenames() {
        final var args = doTest("file/one", "file/two");
        assertThat(args.getInputFiles())
            .hasSize(2)
            .containsExactly("file/one", "file/two");
    }

    private ToolArgs doTest(String... argv) {
        final var toolArgs = new ToolArgs();

        JCommander.newBuilder()
            .addObject(toolArgs)
            .build()
            .parse(argv);

        return toolArgs;
    }
}
