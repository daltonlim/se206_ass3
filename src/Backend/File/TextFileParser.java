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


                String[] nameArray = name.split("[ -]");
                boolean present = true;

                for (int i = 0; i < nameArray.length; i++) {
                    nameArray[i] =  Character.toUpperCase(nameArray[i].charAt(0))
                            + nameArray[i].substring(1).toLowerCase();
                    List<String> partialList = new ArrayList<>();
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
