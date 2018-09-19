package Backend.NameManagement;

import Backend.File.FileParser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class will provide a list of unique file names, as well as
 * and allow the retrieval of all files with the same name.
 * SIngle ton as only one name manager needs to exist to account for all the database entries
 */

public class NameManager {
    public static final File directory = new File("audioFiles");
    private HashMap<String, Name> nameList;

    public NameManager() {
        nameList = new HashMap<>();
        getFiles();
    }

    public List<String> getUniqueNames() {
        return new ArrayList<>(nameList.keySet());
    }

    private void getFiles() {
        File[] wavFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String name) {
                return name.endsWith(".wav");
            }
        });

        for (File wav : wavFiles) {
            addFile(wav);
        }
    }

    public File getFile(String name, String date) {
        return nameList.get(name).getFile(date);
    }

    private void addFile(File file) {
        FileParser fileParser = new FileParser(file);
        String name = fileParser.getUserName();
        Name nameToAdd = new Name(name);

        if (nameList.containsKey(name)) {
            nameToAdd = nameList.get(name);
        }

        nameToAdd.addDate(fileParser);

        nameList.put(name, nameToAdd);
    }

    public List<String> getFileDatesForName(String name) {
        return nameList.get(name).returnDates();
    }


}
