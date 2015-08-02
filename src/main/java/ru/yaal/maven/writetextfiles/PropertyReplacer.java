package ru.yaal.maven.writetextfiles;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.introspection.ReflectionValueExtractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replace maven properties ("${project.version}", etc} with them values.
 *
 * @author Yablokov Aleksey
 */
class PropertyReplacer {
    private static final Pattern pattern = Pattern.compile("(\\$\\{([\\w\\.]*)\\})");
    private final String defaultNullValue;
    private final Boolean nullValueException;
    private final MavenProject project;

    public PropertyReplacer(MavenProject project, String defaultNullValue, Boolean nullValueException) {
        this.defaultNullValue = defaultNullValue;
        this.nullValueException = nullValueException;
        this.project = project;
    }

    public void replace(FileParameter file) throws Exception {
        String[] lines = file.getLines();
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if (s != null) {
                Matcher matcher = pattern.matcher(s);
                while (matcher.find()) {
                    String withBraces = matcher.group(1);
                    String withoutBraces = matcher.group(2);

                    if (withoutBraces.startsWith("project.")) {
                        withoutBraces = withoutBraces.replaceFirst("project.", "");
                    }
                    Object replaced = ReflectionValueExtractor.evaluate(withoutBraces, project);
                    if (replaced != null) {
                        s = s.replace(withBraces, replaced.toString());
                    } else {
                        if (!nullValueException) {
                            String value = file.getNullValue() != null ? file.getNullValue() : defaultNullValue;
                            s = s.replace(withBraces, value);
                        } else {
                            throw new MojoExecutionException("Value of property was not found: " + withBraces);
                        }
                    }
                }
            } else {
                s = "";
            }
            lines[i] = s;
        }
    }
}
