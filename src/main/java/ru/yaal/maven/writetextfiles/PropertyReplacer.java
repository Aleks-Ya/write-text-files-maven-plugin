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
    private static final Pattern pattern = Pattern.compile("(\\${1,2}\\{([\\w\\.]*)\\})");
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
            String line = lines[i];
            if (line != null) {
                StringBuffer sb = new StringBuffer(line.length());
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String withBraces = matcher.group(1);
                    if (!withBraces.startsWith("$$")) {
                        String withoutBraces = matcher.group(2);
                        if (withoutBraces.startsWith("project.")) {
                            withoutBraces = withoutBraces.replaceFirst("project\\.", "");
                        }
                        Object replaced = ReflectionValueExtractor.evaluate(withoutBraces, project);
                        if (replaced != null) {
                            matcher.appendReplacement(sb, Matcher.quoteReplacement(replaced.toString()));
                        } else {
                            if (!nullValueException) {
                                String value = file.getNullValue() != null ? file.getNullValue() : defaultNullValue;
                                matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
                            } else {
                                throw new MojoExecutionException("Value of property was not found: " + withBraces);
                            }
                        }
                    } else {
                        String value = withBraces.replaceFirst("\\${2}", "\\$");
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
                    }
                }
                matcher.appendTail(sb);
                lines[i] = sb.toString();
            } else {
                lines[i] = "";
            }
        }
    }
}
