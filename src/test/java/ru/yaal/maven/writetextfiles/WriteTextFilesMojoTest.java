package ru.yaal.maven.writetextfiles;

import com.google.common.io.Files;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Yablokov Aleksey
 */
@Ignore("MavenProject injection doesn't work in test")
public class WriteTextFilesMojoTest {
    @Rule
    public final MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void test() throws Exception {
        File pom = new File(WriteTextFilesMojoTest.class.getResource("correct_pom.xml").toURI());

        WriteTextFilesMojo mojo = (WriteTextFilesMojo) rule.lookupMojo(WriteTextFilesMojo.MOJO_NAME, pom);
        assertNotNull(mojo);
        mojo.execute();

        List<String> hiLines = Files.readLines(new File("target/hi.txt"), Charset.forName("UTF-8"));
        List<String> versionLines = Files.readLines(new File("target/version.txt"), Charset.forName("UTF-8"));

        assertThat(hiLines, contains(
                "Hello, world!"
        ));

        assertThat(versionLines, contains(
                "Description: not-found",
                "Version: 1.0-SNAPSHOT"
        ));

    }


}