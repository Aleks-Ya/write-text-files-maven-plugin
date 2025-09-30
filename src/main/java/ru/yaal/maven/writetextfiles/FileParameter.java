package ru.yaal.maven.writetextfiles;

import java.io.File;

import static ru.yaal.maven.writetextfiles.LineSeparator.SYSTEM;

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

    public File getPath() {
        return path;
    }

    public String[] getLines() {
        return lines;
    }

    public LineSeparator getLineSeparator() {
        return lineSeparator != null ? lineSeparator : SYSTEM;
    }
}

