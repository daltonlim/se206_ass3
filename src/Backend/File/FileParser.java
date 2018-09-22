package Backend.File;

import java.io.File;

/**
 * A class which takes care of the parsing of files, retrieving the date, name,
 * and file associated with a file.
 */
public class FileParser {
    File file;

    public FileParser(File file) {
        this.file = file;
    }

    /**
     * Returns the date in the file name.
     * @return
     */
    public String getDate() {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Return Date Part
        return parts[1] + "_" + parts[2];
    }

    /**
     * Returns the name in the file name.
     * @return
     */
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

    /**
     * Returns the file.
     */
    public File getFile() {
        return file;
    }
}
