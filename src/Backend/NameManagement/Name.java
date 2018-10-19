package Backend.NameManagement;

import Backend.File.FileLogger;
import Backend.File.FileNameParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    public void addDate(FileNameParser fileParser) {
        dateFileList.put(fileParser.getDate(), fileParser.getFile());
    }

    /**
     * Remove a date from a name, usually when the file doesnt exist anymore.
     */
    public void remove(FileNameParser fileParser) {
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

    public File randomBestName() {
        //Get non user generated files
        List<String> dateList = new ArrayList<>();
        for (String key : dateFileList.keySet()) {
            if (!key.contains("ser")) {
                dateList.add(key);
            }
        }
        FileLogger fileLogger = FileLogger.getInstance();

        List<String> goodFiles = new ArrayList<>();
        //Get all good files
        for (String s : dateList) {
            if (!fileLogger.isBad(dateFileList.get(s))) {
                goodFiles.add(s);
            }
        }

        //Ensure that a list is given if all are bad
        if (goodFiles.size() == 0) {
            goodFiles = dateList;
        }
        //Generate random key
        String key = goodFiles.get(new Random().nextInt(goodFiles.size()));
        return dateFileList.get(key);

    }

    Boolean hasUser(){
        for (String name : dateFileList.keySet()) {
            if(name.contains("ser")){
                return true;
            }
        }
        return false;
    }
}

