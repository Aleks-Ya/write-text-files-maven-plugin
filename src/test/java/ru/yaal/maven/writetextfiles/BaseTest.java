package ru.yaal.maven.writetextfiles;

import com.google.common.io.Resources;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public abstract class BaseTest {
    protected final Path baseDir = createBaseDir();
    @Rule
    public MojoRule rule = new MojoRule();

    protected WriteTextFilesMojo createMojo(String pomFilename) throws Exception {
        var pomXml = baseDir.resolve("pom.xml").toFile();
        var pomUrl = requireNonNull(getClass().getResource(pomFilename));
        Resources.copy(pomUrl, new FileOutputStream(pomXml));
        var project = rule.readMavenProject(baseDir.toFile());
        rule.newMavenSession(project);
        return (WriteTextFilesMojo) rule.lookupConfiguredMojo(project, "write-text-files");
    }

    protected static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static Path createBaseDir() {
        try {
            return Files.createTempDirectory(WriteTextFilesMojo.class.getSimpleName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}