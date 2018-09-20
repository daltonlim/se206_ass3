package Backend.NameManagement;

import Backend.File.FileLogger;
import Backend.File.FileParser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This class will provide a list of unique file names, as well as
 * and allow the retrieval of all files with the same name.
 * SIngle ton as only one name manager needs to exist to account for all the database entries
 */

public class NameManager {
    private boolean ordered;
    private boolean _maxOne;
    private List<String> selectedNames;
    private List<String> availableNames;
    private List<String> allNames;
    private static NameManager instance;

    public static NameManager getInstance() {
        if (instance == null) {
            instance = new NameManager();
        }
        return instance;
    }

    public void setSelectedNames(List<String> selectedNames) {
        this.selectedNames = selectedNames;
        availableNames.removeAll(selectedNames);
    }

    public List<String> getAvailableNames() {
        return availableNames;
    }

    public List<String> getSelectedNames() {
        if (!ordered) {
            Collections.shuffle(selectedNames);
        }
        return selectedNames;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public static final File directory = new File("audioFiles");
    private HashMap<String, Name> nameList;


    public NameManager() {
        nameList = new HashMap<>();
        getFiles();
        availableNames = new ArrayList<>(nameList.keySet());
        selectedNames = new ArrayList<>();
        ordered = true;
        _maxOne = true;
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
