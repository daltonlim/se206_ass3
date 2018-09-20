/**
 * This class takes care of the creation, merging and recording of audio
 */
package Backend.File;


import Backend.NameManagement.NameManager;
import Gui.SelectionMenu.TestController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

/**
 * This class takes care of the creation and merging of videos via various BashWorker commands
 */
public class FileCreator {
    private String _fileName;
    private String _date; // storing date when it is generated
    
    public FileCreator(String fileName) {
        _fileName = fileName;
        generateAudio();
    }

    /**
     * This generates audio
     */
    public void generateAudio() {
        String date = getDate();
        _date = date;
        new BashWorker("ffmpeg -f pulse -i default -t 5 " + NameManager.directory +
                "/se206" + date + _fileName + ".wav");
        
    }

    private String getDate() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_d-M-yyyy_H:m:s_");
        String text = date.format(formatter);
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        return text;
    }

    /**
     * return the current date stored in function
     * @return
     */
	public String getTime() {
		return _date;		
	}
	
	 /**
     * return the current name stored in function
     * @return
     */
	public String getName() {
		return _fileName;
	}
    
	public void removeFile() {
		 new BashWorker("rm "+ NameManager.directory + "/se206" + getTime() + getName() + ".wav");
	}
}
