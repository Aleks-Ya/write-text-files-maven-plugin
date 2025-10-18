package ru.yaal.maven.writetextfiles;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import static java.lang.String.format;
import static ru.yaal.maven.writetextfiles.LineSeparator.SYSTEM;

/**
 * @author Aleksey Yablokov.
 */
@Mojo(name = WriteTextFilesMojo.MOJO_NAME)
@SuppressWarnings("unused")
public class WriteTextFilesMojo extends AbstractMojo {
    public static final String MOJO_NAME = "write-text-files";

    @Parameter
    private FileParameter[] files;

    @Parameter
    private String charset;

    @Override
    public void execute() throws MojoExecutionException {
        if (files != null) {
            try {
                for (var fileParameter : files) {
                    var file = getFile(fileParameter);
                    var separator = getLineSeparator(fileParameter);
                    var content = String.join(separator, fileParameter.getLines());
                    var fileCharset = getCharset();
                    Files.writeString(file.toPath(), content, fileCharset);
                    getLog().info(format("Output file length: %s bytes", file.length()));
                }
            } catch (MojoExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } else {
            getLog().warn("No files specified in configuration.");
        }
    }

    private File getFile(FileParameter fileParameter) throws MojoExecutionException, IOException {
        var file = fileParameter.getPath();
        if (file == null) {
            throw new MojoExecutionException("Path is empty");
        }
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        if (!file.createNewFile()) {
            getLog().info("Overwrite file: " + file.getAbsolutePath());
        } else {
            getLog().info("Write to new file: " + file.getAbsolutePath());
        }
        return file;
    }

    private String getLineSeparator(FileParameter fileParameter) {
        var lineSeparator = fileParameter.getLineSeparator().orElse(SYSTEM);
        String separator;
        switch (lineSeparator) {
            case LF:
                separator = "\n";
                break;
            case CRLF:
                separator = "\r\n";
                break;
            default:
                separator = System.lineSeparator();
        }
        getLog().info("Line separator: " +
                separator.replace("\n", "\\n").replace("\r", "\\r"));
        return separator;
    }

    private Charset getCharset() {
        var fileCharset = Charset.forName(charset);
        getLog().info("Charset: " + fileCharset);
        return fileCharset;
    }

}

