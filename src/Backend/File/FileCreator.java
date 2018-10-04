/**
 * This class takes care of the creation, merging and recording of audio
 */
package Backend.File;


import Backend.NameManagement.NameManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class takes care of the creation and merging of videos via various BashWorker commands
 */
public class FileCreator {
    private String fileName;
    private String date; // storing date when it is generated
	private BashWorker bashworker;

    public FileCreator(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This generates audio
     */
    public void generateAudio() {
        String date = getDate();
        this.date = date;
        new BashWorker("ffmpeg -f pulse -i default -t 5 " + NameManager.userDirectory+
                "/se206" + date + fileName + ".wav");

    }


    public void kill() {
    	bashworker.kill();
    }
    /**
     * Gets current date
     */
    private String getDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy_H-m-s_");
        String text = date.format(formatter);
        return "_User-" + text;
    }

    /**
     * return the current name stored in function
     *
     * @return
     */
    public String getName() {
        return fileName;
    }

    /**
     * Returns the current file
     *
     * @return
     */
    public File getFile() {
        return new File(fileString());
    }

    /**
     * Deletes the file
     */
    public void removeFile() {
        new File(fileString()).delete();
    }

    /**
     * Returns the filestring for use in a bashworker elsewhere
     *
     * @return
     */
    public String fileString() {
        return NameManager.userDirectory+ "/se206" + date + fileName + ".wav";
    }

}
