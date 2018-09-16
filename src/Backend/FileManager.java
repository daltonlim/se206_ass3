package Backend;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class will provide a list of unique file names, as well as
 * and allow the retrieval of all files with the same name.
 */
public class FileManager {
    public static final File directory = new File("audioFiles");
    private HashMap<String, HashMap<String, File>> fileList;

    public FileManager() {
        fileList = new HashMap<>();
        getFiles();
    }

    public List<String> getUniqueNames() {
        return new ArrayList<>(fileList.keySet());
    }

    public boolean hasMultiple(String name) {
        return fileList.get(name).size() != 1;
    }

    public File getFile(String name, String date) {
        return fileList.get(name).get(date);
    }

    public List<String> getFileDatesForName(String name) {
        List<String> stringList = new ArrayList<>();
        stringList.addAll(fileList.get(name).keySet());
        return stringList;
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

    private void addFile(File file) {
        String key = getUserName(file);
        HashMap<String, File> toAdd = new HashMap<>();

        if (fileList.get(key) != null) {
            toAdd = fileList.get(key);
        }

        toAdd.put(getDate(file), file);
        fileList.put(key, toAdd);
    }

    private String getDate(File file) {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Return Date Part
        return parts[1] + "_" + parts[2];
    }

    private String getUserName(File file) {
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Get user name with extension
        String[] nameExt = parts[3].split("\\.");

        //Get isolated userName without extension in sentence case
        return java.lang.Character.toUpperCase(nameExt[0].charAt(0))
                + nameExt[0].substring(1).toLowerCase();
    }

}
