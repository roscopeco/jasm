package com.roscopeco.jasm.tool;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TasksTest {
    @Test
    void testCreateTasksFromArgsWhenFilesDontExist() {
        final var tasks = Tasks.createTasks(createTestArgs(), f -> false);

        assertThat(tasks).isEmpty();
        assertThat(tasks).isEmpty();
    }

    @Test
    void testCreateTasksFromArgsWhenFilesExist() {
        final var tasks = Tasks.createTasks(createTestArgs(), f -> true);

        assertThat(tasks)
            .extracting(FileTransformTask::getSrc)
            .extracting(File::getPath)
            .containsExactly(
                Path.of("input", "dir", "one.jasm").toString(),
                Path.of("input", "dir", "two").toString(),
                Path.of("input", "dir", "has/path/three.whatever").toString()
            );

        assertThat(tasks)
            .extracting(FileTransformTask::getDest)
            .extracting(File::getPath)
            .containsExactly(
                Path.of("output", "dir", "one.class").toString(),
                Path.of("output", "dir", "two.class").toString(),
                Path.of("output", "dir", "has/path/three.class").toString()
            );

        assertThat(tasks.stream().filter(t -> t instanceof AssembleTask).map(AssembleTask.class::cast))
            .extracting(AssembleTask::getTargetVersion)
            .allMatch(ver -> ver == 50);
    }

    private ToolArgs createTestArgs() {
        final var args = new ToolArgs();

        args.setInputDirectory(Path.of("input", "dir").toString());
        args.setOutputDirectory(Path.of("output", "dir").toString());
        args.setInputFiles(List.of("one.jasm", "two", "has/path/three.whatever"));
        args.setTarget(50);

        return args;
    }
}
