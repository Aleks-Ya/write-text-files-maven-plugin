package ru.yaal.maven.writetextfiles;

import com.google.common.io.Resources;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WriteTextFilesMojoTest {
    private final Path BASE_DIR = createBaseDir();
    @Rule
    public MojoRule rule = new MojoRule();

    @Test
    public void writeFile() throws Exception {
        var outputFile = BASE_DIR.resolve("target/version.txt");
        var actStdOut = tapSystemOut(() -> createMojo("WriteFile.xml").execute());
        var expUrl = requireNonNull(getClass().getResource("WriteFile.txt"));
        var expContent = Resources.toString(expUrl, UTF_8);
        assertThat(outputFile).hasContent(expContent);
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Line separator: \\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 511 bytes");
    }

    @Test
    public void overwriteFile() throws Exception {
        var outputFile = BASE_DIR.resolve("target/version.txt");
        Files.createDirectories(outputFile.getParent());
        var oldContent = "Old Content";
        Files.writeString(outputFile, oldContent);
        assertThat(outputFile).hasContent(oldContent);
        var actStdOut = tapSystemOut(() -> createMojo("OverwriteFile.xml").execute());
        assertThat(outputFile).hasContent("New Content\n");
        assertThat(actStdOut)
                .contains("Overwrite file: " + outputFile)
                .contains("Line separator: \\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 11 bytes");
    }

    @Test
    public void windowsLineSeparator() throws Exception {
        var outputFile = BASE_DIR.resolve("target/version.txt");
        var actStdOut = tapSystemOut(() -> createMojo("WindowsLineSeparator.xml").execute());
        var actContent = Files.readString(outputFile, UTF_8);
        assertThat(actContent).isEqualTo("Line1\r\nLine2");
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Line separator: \\r\\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 12 bytes");
    }

    @Test
    public void systemLineSeparator() throws Exception {
        var outputFile = BASE_DIR.resolve("target/version.txt");
        var actStdOut = tapSystemOut(() -> createMojo("SystemLineSeparator.xml").execute());
        var actContent = Files.readString(outputFile, UTF_8);
        assertThat(actContent).isEqualTo("Line1" + lineSeparator() + "Line2");
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Charset: UTF-8");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            assertThat(actStdOut)
                    .contains("Line separator: \\r\\n")
                    .contains("Output file length: 12 bytes");
        } else {
            assertThat(actStdOut)
                    .contains("Line separator: \\n")
                    .contains("Output file length: 11 bytes");
        }
    }

    @Test
    public void emptyPath() {
        assertThatThrownBy(() -> createMojo("EmptyPath.xml").execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasMessageContaining("Path is empty");
    }

    @Test
    public void noFilesSpecified() throws Exception {
        var actStdOut = tapSystemOut(() -> createMojo("NoFilesSpecified.xml").execute());
        assertThat(actStdOut).contains("No files specified in configuration.");
    }

    @Test
    public void executionException() throws Exception {
        var outputFile = BASE_DIR.resolve("target/version.txt");
        Files.createDirectories(outputFile);
        assertThatThrownBy(() -> createMojo("ExecutionException.xml").execute())
                .isInstanceOf(MojoExecutionException.class)
                .hasCauseInstanceOf(FileSystemException.class);
    }

    private WriteTextFilesMojo createMojo(String pomFilename) throws Exception {
        var pomXml = BASE_DIR.resolve("pom.xml").toFile();
        var pomUrl = requireNonNull(getClass().getResource(pomFilename));
        Resources.copy(pomUrl, new FileOutputStream(pomXml));
        var project = rule.readMavenProject(BASE_DIR.toFile());
        rule.newMavenSession(project);
        return (WriteTextFilesMojo) rule.lookupConfiguredMojo(project, "write-text-files");
    }

    private static Path createBaseDir() {
        try {
            return Files.createTempDirectory(WriteTextFilesMojoTest.class.getSimpleName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}