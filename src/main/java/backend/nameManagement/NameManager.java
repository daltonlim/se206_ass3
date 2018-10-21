package backend.nameManagement;

import backend.file.FileNameParser;

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
    public static final File directory = new File("Database");
    public static final File userDirectory = new File("UserDatabase");
    private HashMap<String, Name> nameList;
    private List<String> singleNames;

    private NameManager() {
        nameList = new HashMap<>();
        singleNames = new ArrayList<>();
        getFiles(directory);
        getFiles(userDirectory);

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
        } else {
            file.mkdir();
        }
        availableNames = new ArrayList<>(nameList.keySet());
        Collections.sort(singleNames);
    }

    public List<String> getSingleNames() {
        return singleNames;
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
        if (!name.contains(" ") && !name.contains("-")) {
            if (!singleNames.contains(name)) {
                singleNames.add(name);
            }
        }
        nameList.put(name, nameToAdd);
    }

    /**
     * Returns the list of dates for a specified name.
     *
     * @param name name to which dates will be found for
     * @return A list of dates related to that name
     */
    public List<String> getFileDatesForName(String name) {
        List<String> toReturn = new ArrayList<>();
        if (nameList.get(name) != null) {
            toReturn = nameList.get(name).returnDates();
            Collections.sort(toReturn);
        }
        return toReturn;
    }

    /**
     * Retrieves a random file that for the provided name
     * @param name the name you want
     * @return  A good file for the provided name
     */
    public File getRandomGoodFile(String name) {
        Name available = nameList.get(name);
        return available.randomBestName();
    }

    /**
     * This function retrieves all the names which contain a name
     * prefix
     * @param nam the name to act as a prefix
     * @return  the list of strings which have the last part of the name as a prefix
     */
    public List<String> retrievePrefix(String nam) {
        String[] nameArray = nam.trim().split("[ -]");
        int offset = 0;
        if (nam.length() > 0) {
            int lastInd = nam.length() - 1;
            while (nam.charAt(lastInd) == '-' || nam.charAt(lastInd) == ' ') {
                offset++;
                lastInd--;
            }
        }
        String prefix = nam.substring(0, nam.length() - nameArray[nameArray.length - 1].length() - offset);
        prefix = prefix.trim();
        if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) != '-') {
            prefix += ' ';
        }
        String name = nameArray[nameArray.length - 1];
        List<String> possibilities = new ArrayList<>();
        for (String string : singleNames) {

            if (string.startsWith(name)) {
                possibilities.add(prefix + string);
            }
        }
        return possibilities;
    }

    public List<String> retrieveSinglePrefix(String nam) {
        List<String> possibilities = new ArrayList<>();
        for (String string : singleNames) {

            if (string.startsWith(nam)) {
                possibilities.add(string);
            }
        }
        return possibilities;
    }

    /**
     * Retrieves all names which contain a user recording
     */
    public List<String> getUserNames() {
        List<String> userList = new ArrayList<>();
        for (String name : nameList.keySet()) {
            if (nameList.get(name).hasUser()) {
                userList.add(name);
            }
        }
        return userList;
    }

    /**
     * Returns whether or not a given name has user recordings
     * @param name
     * @return
     */
    public boolean hasUser(String name) {
        return nameList.get(name).hasUser();
    }
}
