package backend.file;

import java.io.File;

/**
 * A class which takes care of the parsing of files, retrieving the date, name,
 * and file associated with a file.
 */
public class FileNameParser {
    File file;

    public FileNameParser(File file) {
        this.file = file;
    }

    /**
     * Returns the date in the file name.
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
     *
     * @return
     */
    public String getUserName() {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Get user name with extension
        String[] nameExt = parts[3].split("\\.");

        //Get isolated userName without extension in sentence case
        return sentenceCase(nameExt[0]);
    }

    public static String sentenceCase(String name) {
        char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 || chars[i - 1] == ' ' || chars[i - 1] == '-') {
                chars[i] = Character.toUpperCase(chars[i]);
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }
    /**
     * Returns the file.
     */
    public File getFile() {
        return file;
    }
}
