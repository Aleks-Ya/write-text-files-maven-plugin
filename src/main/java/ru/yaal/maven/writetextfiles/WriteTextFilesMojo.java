package ru.yaal.maven.writetextfiles;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author Aleksey Yablokov.
 */
@Mojo(name = WriteTextFilesMojo.MOJO_NAME, requiresProject = true)
public class WriteTextFilesMojo extends AbstractMojo {
    public static final String MOJO_NAME = "write-text-files";

    @Parameter(required = true)
    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    private FileParameter[] files;

    @Parameter(defaultValue = "")
    @SuppressWarnings("unused")
    private String nullValue;

    @Parameter(defaultValue = "false")
    @SuppressWarnings("unused")
    private Boolean nullValueException;

    @Parameter(defaultValue = "UTF-8")
    @SuppressWarnings("unused")
    private String charset;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            PropertyReplacer replacer = new PropertyReplacer(project, nullValue, nullValueException);
            for (FileParameter file : files) {
                replacer.replace(file);
                File path = file.getPath();
                if (path == null) {
                    throw new MojoExecutionException("Path is empty");
                }
                path.getParentFile().mkdirs();
                if (!path.createNewFile()) {
                    getLog().info("Overwrite file: " + path.getAbsolutePath());
                } else {
                    getLog().info("Write to new file: " + path.getAbsolutePath());
                }
                Files.write(path.toPath(), Arrays.asList(file.getLines()), Charset.forName(charset));
            }
        } catch (MojoExecutionException | MojoFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}

