package backend.file;

import backend.achievements.AchievementManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class which takes care of reporting and retrieving bad recordings. A singleton is used
 * to ensure that only one instance is present as that is all that is required.
 */
public class FileLogger {

    private List<String> loggedList;
    private static final FileLogger instance = new FileLogger();
    private String logFile;

    private FileLogger() {
        logFile = "Logs/badRecordings.txt";
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
     */
    public static FileLogger getInstance() {
        return instance;
    }

    /**
     * Adds file to the list
     */
    public void report(File file) {
        String toLog = file.getName();
        AchievementManager.getInstance().incrementAchievement("Report");
        if (!loggedList.contains(toLog)) {
            loggedList.add(toLog);
        }
    }

    /**
     * Removes file to the list
     */
    public void unReport(File file) {
        String toLog = file.getName();

        if (loggedList.contains(toLog)) {
            loggedList.remove(toLog);
        }
    }

    public void writeToFile() {
        writeToFile(logFile, loggedList);
    }

    public void writeToFile(File logFile, List<String> strings) {
        try {
            FileWriter fw = new FileWriter(logFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            for (String file : strings) {
                out.println(file);
            }
            out.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes log to file
     */
    public void writeToFile(String logFile, List<String> strings) {
        writeToFile(new File(logFile), strings);
    }

    /**
     * Way to check if a file has previously been reported as bad.
     */
    public boolean isBad(File file) {
        return loggedList.contains(file.getName());
    }

    /**
     * Generates a file when an uncaught exception is caught. This allows the user to email if
     * required.
     */
    public static void uncaughtException(Thread t, Throwable e) {
        File file = new File("/Log/errors.txt");
        try {
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(e.toString());
            out.close();
            bw.close();
        } catch (IOException ex) {
        }
    }
}
