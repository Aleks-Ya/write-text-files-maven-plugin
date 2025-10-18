package ru.yaal.maven.writetextfiles;

import java.io.File;
import java.util.Optional;

/**
 * @author Yablokov Aleksey
 */
public class FileParameter {
    @SuppressWarnings("unused")
    private File path;

    @SuppressWarnings("unused")
    private String[] lines;

    @SuppressWarnings("unused")
    private LineSeparator lineSeparator;

    @SuppressWarnings("unused")
    private String charset;

    public File getPath() {
        return path;
    }

    public String[] getLines() {
        return lines;
    }

    public Optional<LineSeparator> getLineSeparator() {
        return Optional.ofNullable(lineSeparator);
    }

    public Optional<String> getCharset() {
        return Optional.ofNullable(charset);
    }
}
