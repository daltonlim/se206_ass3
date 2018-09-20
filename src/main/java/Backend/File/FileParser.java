package Backend.File;

import java.io.File;

public class FileParser {
    File file;

    public FileParser(File file) {
        this.file = file;
    }

    public String getDate() {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Return Date Part
        return parts[1] + "_" + parts[2];
    }

    public String getUserName() {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Get user name with extension
        String[] nameExt = parts[3].split("\\.");

        //Get isolated userName without extension in sentence case
        return Character.toUpperCase(nameExt[0].charAt(0))
                + nameExt[0].substring(1).toLowerCase();
    }

    public File getFile() {
        return file;
    }
}
