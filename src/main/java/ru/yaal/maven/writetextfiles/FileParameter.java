package ru.yaal.maven.writetextfiles;

import java.io.File;

/**
 * @author Yablokov Aleksey
 */
public class FileParameter {
    private File path;
    private String nullValue;
    private String[] lines;

    public File getPath() {
        return path;
    }

    public String[] getLines() {
        return lines;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }
}
