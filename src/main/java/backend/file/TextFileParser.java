package backend.file;

import backend.nameManagement.NameManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFileParser {

    private List<String> availableNames;
    private List<String> namesToAdd;
    private List<String> notPossibleNames;
    private String notPossibleNameString;
    private List<String> partialNames;
    private File file;

    public TextFileParser(File file) {
        this.file = file;
        init();
        parseFile();
    }

    private void init() {
        notPossibleNameString = "";
        availableNames = NameManager.getInstance().getAvailableNames();
        namesToAdd = new ArrayList<>();
        partialNames = new ArrayList<>();
        notPossibleNames = new ArrayList<>();
    }

    public TextFileParser(String name) {
        init();
        parseName(name);
    }

    private void parseFile() {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                parseName(name);
            }
            scanner.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void parseName(String name) {
        name = FileNameParser.sentenceCase(name);
        int length = name.length();
        int offset = 1;
        while (name.charAt(length - offset) == ' ' || name.charAt(length - offset) == '-') {
            offset++;
        }
        name = name.substring(0, length - offset + 1);
        String[] nameArray = name.split("[ -]");
        boolean present = true;
        List<String> partialList = new ArrayList<>();

        for (int i = 0; i < nameArray.length; i++) {
            if (availableNames.contains(nameArray[i])) {
                partialList.add(nameArray[i]);
            } else {
                present = false;
            }
            if (i == nameArray.length - 1) {
                if (present) {
                    namesToAdd.add(name);
                } else {
                    String toChange = name + "-->" + partialList.toString() + "\n";
                    toChange = toChange.replace("[", "");
                    toChange = toChange.replace("]", "");

                    notPossibleNameString += toChange;
                    notPossibleNames.add(toChange);
                    partialNames.addAll(partialList);
                }
            }
        }
    }


    public List<String> getNamesToAdd() {
        return namesToAdd;
    }

    public List<String> getNotPossibleNames() {
        return notPossibleNames;
    }

    public String getNotPossibleNameString() {
        return notPossibleNameString;
    }


    public List<String> getPartialNames() {
        return partialNames;
    }
}
