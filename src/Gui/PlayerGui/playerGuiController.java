package Gui.PlayerGui;

import Backend.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class playerGuiController implements Initializable {
    FileManager fileManager;

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
        //TODO fix following block
        fileManager = new FileManager();
        nameList.getItems().addAll(fileManager.getUniqueNames());

        disableButtons(true);
    }

    /**
     * Updates the dateslist when a name is clicked
     */
    @FXML
    public void updateDates() {
        String name = nameList.getSelectionModel().getSelectedItems().get(0).toString();
        //Cause removeALl command is buggy https://stackoverflow.com/questions/12132896/listview-removeall-doesnt-work
        dateList.getItems().remove(0, dateList.getItems().size());
        dateList.getItems().addAll(fileManager.getFileDatesForName(name));

        disableButtons(false);

        //Select first element is list by default
        dateList.getSelectionModel().select(0);
        play();
    }

    private void disableButtons(boolean bool) {
        playButton.setDisable(bool);
        stopButton.setDisable(bool);
        reportButton.setDisable(bool);
        recordButton.setDisable(bool);
    }

    @FXML
    public void play() {
        String name = nameList.getSelectionModel().getSelectedItems().get(0).toString();
        String date = dateList.getSelectionModel().getSelectedItems().get(0).toString();

//        Media media = new Media(fileManager.getFile(name, date).toURI().toString());

        File file = fileManager.getFile(name, date);
        Media media = new Media(file.toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }
}
