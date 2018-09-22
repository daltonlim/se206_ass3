package Gui.SelectionMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Backend.File.BashWorker;

import Backend.File.FileCreator;
import Backend.NameManagement.NameManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javax.swing.text.html.HTML.Tag.HEAD;

public class controlController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label States;

    @FXML
    private Button PlayOldButton;

    @FXML
    private Button RecordButton;

    @FXML
    private Button PlayYoursButton;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private ListView dateList;

    @FXML
    private Button SaveButton;

    @FXML
    private Button NoSaveButton;

    @FXML
    private Label name;

    @FXML
    private Button ExitButton;

    @FXML
    private Button RestartButton;

    private Task<?> _Recording;

    private FileCreator _FileCreator;

    private String _name;

    NameManager fileManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons(true);
        progressbar.setVisible(false);
        States.setVisible(false);
        fileManager = NameManager.getInstance();
    }

    private void disableButtons(boolean b) {
        PlayYoursButton.setDisable(b);
        SaveButton.setDisable(b);
        NoSaveButton.setDisable(b);
    }

    public Task<?> createWorker() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                    updateMessage("Recording Completed : " + ((i * 20) + 20) + "%");
                    updateProgress(i + 1, 5);
                }
                RecordingIsFinished();
                return true;
            }
        };
    }

    @FXML
    public void record() throws InterruptedException {
        RecordButton.setDisable(true);
        progressbar.setVisible(true);
        States.setVisible(true);
        PlayOldButton.setDisable(true);
        progressbar.setProgress(0.0);

        _Recording = createWorker();

        progressbar.progressProperty().unbind();

        progressbar.progressProperty().bind(_Recording.progressProperty());

        _Recording.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                States.setText(newValue);
            }
        });
        new Thread(_Recording).start();
        _FileCreator = new FileCreator(_name);
    }


    private void RecordingIsFinished() {
        PlayYoursButton.setDisable(false);
        SaveButton.setDisable(false);
        NoSaveButton.setDisable(false);
    }

    @FXML
    public void playback() throws InterruptedException {
        try {
            String audioFile = _FileCreator.fileString();
            Media media = new Media(new File(audioFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void playold() throws InterruptedException {
        try {
            progressbar.setVisible(false);
            States.setVisible(false);
            String date = dateList.getSelectionModel().getSelectedItems().get(0).toString();
            File audioFile = fileManager.getFile(_name, date);
            String location = audioFile.toURI().toString();
            new BashWorker("chmod +x " + location);
            new BashWorker("ffplay -nodisp -autoexit " + location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectedName(String name) {
        _name = name;
    }

    public void initData(String name) {
        selectedName(name);
        updatelist();
    }

    public void updatelist() {

        dateList.getItems().remove(0, dateList.getItems().size());
        dateList.getItems().addAll(fileManager.getFileDatesForName(_name));
        dateList.getSelectionModel().select(0);
        name.setText(_name);
    }

    @FXML
    public void choose() {
        PlayOldButton.setDisable(false);
    }

    @FXML
    public void restart() throws IOException {
        Stage primaryStage = (Stage) RestartButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordGui.fxml"));
        Parent root = loader.load();

        RecordGuiController controller = loader.<RecordGuiController>getController();


        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    public void save() {
        fileManager.addFile(_FileCreator.getFile());

        updatelist();
        SaveButton.setDisable(true);
        NoSaveButton.setDisable(true);
    }

    @FXML
    public void nosave() {
        _FileCreator.removeFile();
        NameManager.getInstance().removeFile(_FileCreator.getFile());
        SaveButton.setDisable(true);
        NoSaveButton.setDisable(true);

    }

    @FXML
    public void exit() throws IOException {
        SceneManager.getInstance().removeScene();
    }
}