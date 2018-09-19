/**
 * This class takes care of the creation, merging and recording of audio
 */
package Backend.File;


import Backend.NameManagement.NameManager;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class takes care of the creation and merging of videos via various BashWorker commands
 */
public class FileCreator {
    String _fileName;


    public FileCreator(String fileName) {
        _fileName = fileName;
        generateAudio();
    }

    /**
     * This generates audio
     */
    public void generateAudio() {
        String date = getDate();
        new BashWorker("ffmpeg -y -f alsa -i hw:0 -ac 1 -t 00:00:05 " + NameManager.directory +
                "/se206" + date + _fileName + ".wav");
    }

    private String getDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_d-M-yyyy_H:m:s_");
        String text = date.format(formatter);
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        return text;
    }

}

