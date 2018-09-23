package Backend.NameManagement;

import Backend.File.FileParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class to help deal with the multiple files associated with any name
 */
public class Name {

    private String name;
    private HashMap<String, File> dateFileList;

    /**
     * Creates a name object from a string
     */
    public Name(String name) {
        this.name = name;
        dateFileList = new HashMap<>();
    }

    /**
     * Add date to a name
     */
    public void addDate(FileParser fileParser) {
        dateFileList.put(fileParser.getDate(), fileParser.getFile());
    }

    /**
     * Remove a date from a name, usually when the file doesnt exist anymore.
     */
    public void remove(FileParser fileParser) {
        dateFileList.remove(fileParser.getDate());
    }

    /**
     * Retrieve the file associated with a date
     *
     * @param date a date string
     * @return the file associated with the date string
     */
    public File getFile(String date) {
        return dateFileList.get(date);
    }

    /**
     * A method to return the list of dates associated with a name.
     *
     * @return
     */
    public List<String> returnDates() {
        return new ArrayList(dateFileList.keySet());
    }
}
