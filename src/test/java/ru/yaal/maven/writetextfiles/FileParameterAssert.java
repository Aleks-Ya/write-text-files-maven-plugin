package ru.yaal.maven.writetextfiles;

import org.assertj.core.api.AbstractAssert;

import java.nio.file.Path;

public class FileParameterAssert extends AbstractAssert<FileParameterAssert, FileParameter> {

    private FileParameterAssert(FileParameter actual) {
        super(actual, FileParameterAssert.class);
    }

    public static FileParameterAssert assertThat(FileParameter actual) {
        return new FileParameterAssert(actual);
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileParameterAssert hasPath(Path path) {
        var value = getFieldValue("path");
        objects.assertEqual(info, value, path != null ? path.toFile() : null);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileParameterAssert hasLines(String... lines) {
        var value = getFieldValue("lines");
        objects.assertEqual(info, value, lines);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileParameterAssert hasLineSeparator(LineSeparator lineSeparator) {
        var value = getFieldValue("lineSeparator");
        objects.assertEqual(info, value, lineSeparator);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileParameterAssert hasCharset(String charset) {
        var value = getFieldValue("charset");
        objects.assertEqual(info, value, charset);
        return this;
    }

    private Object getFieldValue(String fieldName) {
        try {
            var field = FileParameter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(actual);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
