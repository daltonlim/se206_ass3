package Backend.NameManagement;

import Backend.File.FileNameParser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

/**
 * This class will provide a list of unique file names, as well as and allow the retrieval
 * of all files with the same name. Singleton as only one name manager needs to exist
 * to account for all the database entries
 */

public class NameManager {
    private List<String> availableNames;
    private static NameManager instance;
    public static final File directory = new File("audioFiles");
    private HashMap<String, Name> nameList;

    private NameManager() {
        nameList = new HashMap<>();
        getFiles(directory);

    }

    /**
     * Retrieve singleton instance
     */
    public static NameManager getInstance() {
        if (instance == null) {
            instance = new NameManager();
        }
        return instance;
    }

    /**
     * Removes a specified file from the name manager
     *
     * @param file the file to remove from the database
     */
    public void removeFile(File file) {
        FileNameParser fileParser = new FileNameParser(file);
        if (fileParser.getDate().contains("ser")) {
            nameList.get(fileParser.getUserName()).remove(fileParser);
        }
    }

    /**
     * A method to find all names a person has
     *
     * @return A list of all available names for which there are files.
     */
    public List<String> getAvailableNames() {
        return availableNames;
    }

    /**
     * A way to parse only the wav files in the wav directory
     */
    public void getFiles(File file) {
        if (file.exists()) {
            File[] wavFiles = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File directory, String name) {
                    return name.endsWith(".wav");
                }
            });
            for (File wav : wavFiles) {
                addFile(wav);
            }
        }else{
            file.mkdir();
        }
        availableNames = new ArrayList<>(nameList.keySet());
    }

    /**
     * Returns the file associated with a name and date
     *
     * @param name the name within the file
     * @param date the date within the file
     * @return the file associated with the above
     */
    public File getFile(String name, String date) {
        return nameList.get(name).getFile(date);
    }

    /**
     * This adds a file to the database, usually when a new recording has been generated
     *
     * @param file file to add
     */
    public void addFile(File file) {
        FileNameParser fileParser = new FileNameParser(file);
        String name = fileParser.getUserName();
        Name nameToAdd = new Name(name);

        if (nameList.containsKey(name)) {
            nameToAdd = nameList.get(name);
        }

        nameToAdd.addDate(fileParser);

        nameList.put(name, nameToAdd);
    }

    /**
     * Returns the list of dates for a specified name.
     *
     * @param name name to which dates will be found for
     * @return A list of dates related to that name
     */
    public List<String> getFileDatesForName(String name) {
        List<String> toReturn = nameList.get(name).returnDates();
        Collections.sort(toReturn);
        return toReturn;
    }
}
