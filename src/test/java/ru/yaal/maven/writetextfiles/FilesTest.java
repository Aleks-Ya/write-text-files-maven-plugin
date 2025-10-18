package ru.yaal.maven.writetextfiles;

import com.google.common.io.Resources;
import org.apache.maven.plugin.MojoExecutionException;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.Test;

import java.nio.file.Files;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.yaal.maven.writetextfiles.FileParameterAssert.assertThat;
import static ru.yaal.maven.writetextfiles.LineSeparator.LF;
import static ru.yaal.maven.writetextfiles.MojoAssert.assertMojo;

public class FilesTest extends BaseTest {

    @Test
    public void writeNewFile() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        var mojo = createMojo("FilesTest_WriteNewFile.xml");
        assertMojo(mojo).hasCharset(null).hasLineSeparator(null).hasFileParameters(
                file -> assertThat(file)
                        .hasCharset("UTF-8").hasPath(outputFile).hasLineSeparator(LF).hasLines(
                                "=== SIMPLE STRING ===",
                                "How to use write-text-files-maven-plugin",
                                "=== PROJECT PROPERTIES ===",
                                "Name: Example for write-text-files-maven-plugin",
                                "ArtifactId: run-plugin",
                                "Version: 1.0.0-SNAPSHOT",
                                "=== POM.XML PROPERTY ===",
                                "Message: Machines should work; people should think.",
                                "=== JAVA PROPERTIES ===",
                                "Java vendor: ${java.vendor}",
                                "Java version: ${java.version}",
                                "Java home: ${java.home}",
                                "=== OS ENVIRONMENT VARIABLES ===",
                                "User dir: ${env.HOME}",
                                "=== MAVEN SETTINGS.XML ===",
                                "Offline: false",
                                "=== NULL VALUES ===",
                                "Description: ${project.description}"
                        ));
        var actStdOut = tapSystemOut(mojo::execute);
        var expUrl = requireNonNull(getClass().getResource("FilesTest_WriteNewFile.txt"));
        var expContent = Resources.toString(expUrl, UTF_8);
        assertThat(outputFile).hasContent(expContent);
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Line separator: \\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 520 bytes");
    }

    @Test
    public void overwriteFile() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        Files.createDirectories(outputFile.getParent());
        var oldContent = "Old Content";
        Files.writeString(outputFile, oldContent);
        assertThat(outputFile).hasContent(oldContent);
        var mojo = createMojo("FilesTest_OverwriteFile.xml");
        assertMojo(mojo).hasCharset("UTF-8").hasLineSeparator(null).hasFileParameters(
                file -> assertThat(file)
                        .hasCharset(null).hasPath(outputFile).hasLineSeparator(LF).hasLines("New Content"));
        var actStdOut = tapSystemOut(mojo::execute);
        assertThat(outputFile).hasContent("New Content\n");
        assertThat(actStdOut)
                .contains("Overwrite file: " + outputFile)
                .contains("Line separator: \\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 11 bytes");
    }

    @Test
    public void emptyPath() throws Exception {
        var mojo = createMojo("FilesTest_EmptyPath.xml");
        assertMojo(mojo).hasCharset(null).hasLineSeparator(null).hasFileParameters(
                file -> assertThat(file)
                        .hasCharset(null).hasPath(null).hasLineSeparator(null).hasLines());
        assertThatThrownBy(mojo::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("Path is empty");
    }

    @Test
    public void noFiles() throws Exception {
        var mojo = createMojo("FilesTest_NoFiles.xml");
        assertMojo(mojo).hasCharset(null).hasLineSeparator(null)
                .hasFileParameters((ThrowingConsumer<? super FileParameter>) null);
        var actStdOut = tapSystemOut(mojo::execute);
        assertThat(actStdOut).contains("No files specified in configuration.");
    }

}