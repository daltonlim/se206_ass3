package Backend.NameManagement;

import Backend.File.FileParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Name {

    private String name;
    private HashMap<String, File> dateFileList;

    public Name(String name) {
        this.name = name;
        dateFileList = new HashMap<>();
    }

    public void addDate(FileParser fileParser) {
        dateFileList.put(fileParser.getDate(), fileParser.getFile());
    }

    public void remove(FileParser fileParser) {
        dateFileList.remove(fileParser.getDate());
    }

    public File getFile(String date) {
        return dateFileList.get(date);
    }


    public List<String> returnDates() {
        return new ArrayList(dateFileList.keySet());
    }
}
