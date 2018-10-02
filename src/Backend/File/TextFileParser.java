package Backend.File;

import Backend.NameManagement.NameManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFileParser {

    private List<String> availableNames;
    private List<String> namesToAdd;
    private List<String> notPossibleNames;
    private List<String> partialNames;
    private File file;

    public TextFileParser(File file) {
        availableNames = NameManager.getInstance().getAvailableNames();
        this.file = file;
        namesToAdd = new ArrayList<>();
        partialNames = new ArrayList<>();
        notPossibleNames = new ArrayList<>();
        parseNames();
    }

    private void parseNames() {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String name = scanner.nextLine();
                name = sentenceCase(name);
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
                            notPossibleNames.add(name);
                            partialNames.addAll(partialList);
                        }
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String sentenceCase(String name) {
        char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 || chars[i - 1] == ' ' || chars[i - 1] == '-') {
                chars[i] = Character.toUpperCase(chars[i]);
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }


    public List<String> getNamesToAdd() {
        return namesToAdd;
    }

    public List<String> getNotPossibleNames() {
        return notPossibleNames;
    }

    public List<String> getPartialNames() {
        return partialNames;
    }
}
