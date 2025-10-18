package ru.yaal.maven.writetextfiles;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import java.nio.file.FileSystemException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.yaal.maven.writetextfiles.MojoAssert.assertMojo;

public class WriteTextFilesMojoTest extends BaseTest {

    @Test
    public void executionException() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        Files.createDirectories(outputFile);
        var mojo = createMojo("WriteTextFilesMojoTest_ExecutionException.xml");
        assertMojo(mojo).hasCharset("UTF-8").hasLineSeparator(null).hasFileParameters(
                file -> FileParameterAssert.assertThat(file)
                        .hasCharset(null).hasPath(outputFile).hasLineSeparator(null).hasLines("Line 1"));
        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasCauseInstanceOf(FileSystemException.class);
    }

}