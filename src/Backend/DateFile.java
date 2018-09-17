package Backend;

import java.io.File;

public class DateFile {
    private String date;
    private File file;

    public DateFile(String date, File file) {
        this.date = date;
        this.file = file;
    }

    public String getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }
}
