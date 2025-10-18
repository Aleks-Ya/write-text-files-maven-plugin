package ru.yaal.maven.writetextfiles;

import org.junit.Test;

import java.nio.file.Files;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.yaal.maven.writetextfiles.LineSeparator.CRLF;
import static ru.yaal.maven.writetextfiles.LineSeparator.LF;
import static ru.yaal.maven.writetextfiles.MojoAssert.assertMojo;

public class LineSeparatorTest extends BaseTest {

    @Test
    public void lineSeparatorWindows() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        var mojo = createMojo("LineSeparatorTest_Windows.xml");
        assertMojo(mojo).hasCharset("UTF-8").hasLineSeparator(null).hasFileParameters(
                file -> FileParameterAssert.assertThat(file)
                        .hasCharset(null).hasPath(outputFile).hasLineSeparator(CRLF).hasLines("Line1", "Line2"));
        var actStdOut = tapSystemOut(mojo::execute);
        var actContent = Files.readString(outputFile, UTF_8);
        assertThat(actContent).isEqualTo("Line1\r\nLine2");
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Line separator: \\r\\n")
                .contains("Charset: UTF-8")
                .contains("Output file length: 12 bytes");
    }

    @Test
    public void lineSeparatorSystem() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        var mojo = createMojo("LineSeparatorTest_System.xml");
        assertMojo(mojo).hasCharset("UTF-8").hasLineSeparator(null).hasFileParameters(
                file -> FileParameterAssert.assertThat(file)
                        .hasCharset(null).hasPath(outputFile).hasLineSeparator(null).hasLines("Line1", "Line2"));
        var actStdOut = tapSystemOut(mojo::execute);
        var actContent = Files.readString(outputFile, UTF_8);
        assertThat(actContent).isEqualTo("Line1" + lineSeparator() + "Line2");
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Charset: UTF-8");
        if (isWindows()) {
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
    public void lineSeparatorGlobalLinux() throws Exception {
        var outputFile = baseDir.resolve("target/info.txt");
        var mojo = createMojo("LineSeparatorTest_GlobalLinux.xml");
        assertMojo(mojo).hasCharset("UTF-8").hasLineSeparator(LF).hasFileParameters(
                file -> FileParameterAssert.assertThat(file)
                        .hasCharset(null).hasPath(outputFile).hasLineSeparator(null).hasLines("Line1", "Line2"));
        var actStdOut = tapSystemOut(mojo::execute);
        var actContent = Files.readString(outputFile, UTF_8);
        assertThat(actContent).isEqualTo("Line1\nLine2");
        assertThat(actStdOut)
                .contains("Write to new file: " + outputFile)
                .contains("Charset: UTF-8")
                .contains("Line separator: \\n")
                .contains("Output file length: 11 bytes");
    }

}