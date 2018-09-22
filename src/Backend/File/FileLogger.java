package Backend.File;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class which takes care of reporting and retrieving bad recordings. A singleton is used
 * to ensure that on ly once instance is present as that is all that is required.
 */
public class FileLogger {

    List<String> loggedList;
    private static final FileLogger instance = new FileLogger();
    private String logFile;

    private FileLogger() {
        logFile = "Logs/log.txt";
        checkDir();
        checkLogFile();
        loggedList = new ArrayList<>();

        readFileToList();
    }

    /**
     * Loads previously logged bad files to the list
     */
    private void readFileToList() {
        try {
            Scanner s = new Scanner(new File(logFile));
            while (s.hasNext()) {
                loggedList.add(s.next());
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensures that the log file exists
     */
    private void checkLogFile() {
        File log = new File(logFile);
        try {
            log.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure that the log folder exists
     */
    private void checkDir() {
        File dir = new File("Logs");

        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (Exception se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Returns singleton instance
     * @return
     */
    public static FileLogger getInstance() {
        return instance;
    }

    /**
     * Adds file to the list and appends it to the file
     */
    public void report(File file) {
        String toLog = file.getName();

        if (!loggedList.contains(toLog)) {
            loggedList.add(toLog);
            try (FileWriter fw = new FileWriter(logFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Way to check if a file has previously been reported as bad.
     */
    public boolean isBad(File file){
        return loggedList.contains(file.getName());
    }
}
