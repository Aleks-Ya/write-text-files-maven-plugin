package ru.yaal.maven.writetextfiles;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowingConsumer;

public class MojoAssert extends AbstractAssert<MojoAssert, WriteTextFilesMojo> {

    private MojoAssert(WriteTextFilesMojo actual) {
        super(actual, MojoAssert.class);
    }

    public static MojoAssert assertMojo(WriteTextFilesMojo actual) {
        return new MojoAssert(actual);
    }

    @SuppressWarnings("UnusedReturnValue")
    public MojoAssert hasCharset(String charset) {
        var value = getFieldValue("charset");
        objects.assertEqual(info, value, charset);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public MojoAssert hasLineSeparator(LineSeparator lineSeparator) {
        var value = getFieldValue("lineSeparator");
        objects.assertEqual(info, value, lineSeparator);
        return this;
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final MojoAssert hasFileParameters(ThrowingConsumer<? super FileParameter>... requirements) {
        var files = (FileParameter[]) getFieldValue("files");
        if (files != null) {
            Assertions.assertThat(files).satisfiesExactly(requirements);
        } else {
            Assertions.assertThat((FileParameter[]) null).isNull();
        }
        return this;
    }

    private Object getFieldValue(String fieldName) {
        try {
            var field = WriteTextFilesMojo.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(actual);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
