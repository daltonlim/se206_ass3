package Gui.SelectionMenu;

import Backend.File.FileLogger;
import Backend.NameManagement.NameManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class playerGuiController implements Initializable {
    NameManager fileManager;
    FileLogger fileLogger;
    @FXML
    private Label badWarningLabel;
    @FXML
    private MediaView mediaView;
    @FXML
    private ListView nameList;
    @FXML
    private ListView dateList;
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button recordButton;
    @FXML
    private Button reportButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons(true);
        fileLogger = FileLogger.getInstance();
    }

    /**
     * Updates the dateslist when a name is clicked
     */
    @FXML
    private void updateDates() {
        String name = nameList.getSelectionModel().getSelectedItems().get(0).toString();
        //Cause removeALl command is buggy https://stackoverflow.com/questions/12132896/listview-removeall-doesnt-work
        dateList.getItems().remove(0, dateList.getItems().size());
        dateList.getItems().addAll(fileManager.getFileDatesForName(name));

        disableButtons(false);

        //Select first element is list by default
        dateList.getSelectionModel().select(0);

        isBadFile();
    }

    private void disableButtons(boolean bool) {
        playButton.setDisable(bool);
        stopButton.setDisable(bool);
        reportButton.setDisable(bool);
        recordButton.setDisable(bool);
    }

    @FXML
    private void play() {
        File file = retrieveFile();
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    public void initData(List<String> names, NameManager fileManager, Boolean ordered) {
        this.fileManager = fileManager;
        if (!ordered) {
            Collections.shuffle(names);
        }
        nameList.getItems().addAll(names);
    }

    @FXML
    private void report() {
        File file = retrieveFile();
        fileLogger.report(file);
        badWarningLabel.setText("Warning: The selected file has been marked as bad");
    }

    private File retrieveFile() {
        String name = nameList.getSelectionModel().getSelectedItems().get(0).toString();
        String date = dateList.getSelectionModel().getSelectedItems().get(0).toString();
        return fileManager.getFile(name, date);
    }

    @FXML
    private void isBadFile() {
        if (fileLogger.isBad(retrieveFile())) {
            badWarningLabel.setText("Warning: The selected file has been marked as bad");
        } else {
            badWarningLabel.setText("");
        }
    }
}
