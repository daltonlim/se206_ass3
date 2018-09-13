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
    private static final File directory = new File("./audioFiles");
    private HashMap<String,List<File>> fileList;

    public FileManager(){
        fileList = new HashMap<>();
        getFiles();
    }

    private void getFiles(){
        File [] wavFiles = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File directory, String name) {
                return name.endsWith(".wav");
            }
        });

        for (File wav : wavFiles) {
            addFile(wav);
        }
    }

    private void addFile(File file){
        String key = getUserName(file);
        if(fileList.get(key) == null){
            List<File> toAdd = new ArrayList<>();
            toAdd.add(file);
            fileList.put(key, toAdd );
        }else   {
            fileList.get(key).add(file);
        }
    }

    private String getUserName(File file){
        //Split filename
        String entireName = file.getName();
        String[] parts = entireName.split("_");

        //Get user name with extension
        String nameExt = parts[3];

        //Get isolated userName
        return nameExt.substring(0,nameExt.length()-4);
    }
}
