package Gui.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Backend.File.BashWorker;

import Backend.File.FileCreator;
import Backend.File.FileParser;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
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
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * A class controls the Record window 
 */
public class RecordGuiController implements Initializable {
    @FXML
    private Label States;
    @FXML
    private Label nameLabel;
    @FXML
    private Button PlayOldButton;
    @FXML
    private Button RestartButton;
    @FXML
    private Button recordButton;
    @FXML
    private Button ExitButton;
    @FXML
    private Button PlayYoursButton;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button SaveButton;
    @FXML
    private Button NoSaveButton;

    private Task<?> _Recording;
    private FileCreator fileCreator;
    private FileParser fileParser;
    private BashWorker bashWorker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons(true);
        States.setVisible(false);
    }

    private void disableButtons(boolean b) {
        PlayOldButton.setDisable(b);
        PlayYoursButton.setDisable(b);
        SaveButton.setDisable(b);
        NoSaveButton.setDisable(b);
        RestartButton.setDisable(b);
    }
    
    /**
     * a task connects with progress bar
     */
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

    /**
     * record the own version
     */
    @FXML
    public void record() {
        try {
            recordButton.setDisable(true);
            ExitButton.setVisible(false);
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
            fileCreator.generateAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * users can do more thing after recording
     */
    private void RecordingIsFinished() {
        disableButtons(false);
    }

    /**
     * Play the file selected
     */
    private void play(File audioFile) {
        stop();
        String location = audioFile.toURI().toString();
        bashWorker = new BashWorker("ffplay -nodisp -autoexit " + location);
    }

    /**
     * Play the own version
     */
    @FXML
    public void playNew() throws InterruptedException {
        try {
            File audioFile = new File(fileCreator.fileString());
            play(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the original version
     */
    @FXML
    public void playOld() throws InterruptedException {

        try {
            States.setVisible(false);
            File audioFile = fileParser.getFile();
            play(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * setup variables needed
     */
    public void initData(File file) {
        fileParser = new FileParser(file);
        String name = fileParser.getUserName();
        fileCreator = new FileCreator(name);
        nameLabel.setText(name);
    }

    /**
     * refresh the record window, can record again
     */
    @FXML
    public void restart() throws IOException {
        stop();
        fileCreator.removeFile();
        Stage primaryStage = (Stage) recordButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordGui.fxml"));
        Parent root = loader.load();

        RecordGuiController controller = loader.<RecordGuiController>getController();

        controller.initData(fileParser.getFile());

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    /**
     * save the own version and exit
     */
    @FXML
    public void save() {
        NameManager.getInstance().addFile(fileCreator.getFile());
        exit();
    }

    /**
     * delete the own version and exit
     */
    @FXML
    public void nosave() {
        fileCreator.removeFile();
        NameManager.getInstance().removeFile(fileCreator.getFile());
        exit();
    }

    /**
     * back to previous window
     */
    @FXML
    public void exit() {
        stop();
        SceneManager.getInstance().removeScene();
    }

    /**
     * kill all process
     */
    private void stop(){
        if(bashWorker!=null){
            bashWorker.kill();
        }
    }
}