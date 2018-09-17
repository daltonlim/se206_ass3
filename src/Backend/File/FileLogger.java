package Backend.File;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileLogger {

    List<String> loggedList;
    private static final FileLogger instance = new FileLogger();

    private String logFile;
    private PrintWriter writer;

    private FileLogger() {
        logFile = "Logs/log.txt";
        checkDir();
        checkLogFile();
        loggedList = new ArrayList<>();

        readFileToList();
    }

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

    private void checkLogFile() {
        File log = new File(logFile);
        try {
            log.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static FileLogger getInstance() {
        return instance;
    }

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

    public boolean isBad(File file){
        return loggedList.contains(file.getName());
    }
}
