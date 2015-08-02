package ru.yaal.maven.writetextfiles;

import java.io.File;

/**
 * @author Yablokov Aleksey
 */
public class FileParameter {
    @SuppressWarnings("unused")
    private File path;

    @SuppressWarnings("unused")
    private String[] lines;

    public File getPath() {
        return path;
    }

    public String[] getLines() {
        return lines;
    }
}
