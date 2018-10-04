package Gui.Controllers;

import Backend.File.BashWorker;
import Backend.File.FileLogger;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class PlayerGuiController implements Initializable {
    private NameManager fileManager;
    private FileLogger fileLogger;
    private String name;
    private int index = 0;

    @FXML
    private Label badWarningLabel;
    @FXML
    private Button nextName;
    @FXML
    private Button lastName;
    @FXML
    private Button deleteButton;
    @FXML
    private Button reportButton;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView dateList;
    @FXML
    Button recordButton;
    @FXML
    private Button microphoneButton;
    @FXML
    private Slider slider;

    private BashWorker worker;

    private List<String> chosenNames;
    private String[] nameArray;
    private Clip clip;
    private FloatControl volume;


    //Return to previous window
    @FXML
    private void goBack() {
        stop();
        SceneManager.getInstance().removeScene();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileLogger = FileLogger.getInstance();
        fileManager = NameManager.getInstance();
        slider.setValue(100);
    }

    @FXML
    public void setVolume() throws LineUnavailableException {

    }

    public float getVolume() throws LineUnavailableException {
        float range = (volume.getMaximum() - volume.getMinimum()) / 100;
        float gain = (float) ((range * slider.getValue()) + volume.getMinimum());
        if (gain > volume.getMaximum()) {
            gain = volume.getMaximum();
        } else if (gain < volume.getMinimum()) {
            gain = volume.getMinimum();
        } else {

        }
        return gain;
    }

    /**
     * Updates the dateslist when a button is clicked
     */
    @FXML
    public void updateDates() {
        nameLabel.setText(name);
        if (isSingleWord()) {
            //Cause removeALl command is buggy https://stackoverflow
            // .com/questions/12132896/listview-removeall-doesnt-work
            dateList.getItems().remove(0, dateList.getItems().size());
            dateList.getItems().addAll(fileManager.getFileDatesForName(name));


            //Select first element is list by default
            dateList.getSelectionModel().select(0);

            isBadFile();
        } else {
            dateList.getItems().remove(0, dateList.getItems().size());
            dateList.getItems().add("no file");
        }

    }

    /**
     * Get the next name in the selected names list and display them.
     */
    @FXML
    public void getNext() {

        if (index != chosenNames.size() - 1) {
            index++;
            name = chosenNames.get(index);
            nameLabel.setText(name);
        }
        updateDates();
        checkButtons();
    }

    /**
     * Get the previous name in the selected names list
     */
    @FXML
    public void getLast() {
        if (index != 0) {
            index--;
            name = chosenNames.get(index);
            nameLabel.setText(name);
        }
        checkButtons();
        updateDates();

    }

    public boolean isSingleWord() {
        nameArray = name.split("[ -]");
        if (nameArray.length > 1) {
            return false;
        }
        return true;
    }

    /**
     * Play the file selected or play the combination
     */
    @FXML
    private void play() {
        stop();
        if (isSingleWord()) {
            try {
                File file = retrieveFile();
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(file));
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(getVolume());
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < nameArray.length; i++) {
                try {
                    File file = fileManager.getRandomGoodFile(nameArray[i]);
                    clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volume.setValue(getVolume());
                    clip.start();
                    //worker = new BashWorker("ffplay -nodisp -autoexit " + location);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    AudioFormat format = audioInputStream.getFormat();
                    long frames = audioInputStream.getFrameLength();
                    double durationInSeconds = (frames + 0.0) / format.getFrameRate();
                    int time = (int) (durationInSeconds * 1000);
                    Thread.sleep(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stop() {
        if (worker != null) {
            worker.kill();
        }
    }

    /**
     * Setup the needed variables
     */
    void initData(List<String> names, Boolean ordered) {
        chosenNames = new ArrayList<>(names);
        if (!ordered) {
            Collections.shuffle(chosenNames);
        }
        index = 0;
        name = chosenNames.get(0);

        nameLabel.setText(name);
        checkButtons();
        updateDates();
    }

    /**
     * Method to ensure the next and previous buttons are enabled when required
     */
    private void checkButtons() {
        nextName.setDisable(false);
        lastName.setDisable(false);
        deleteButton.setDisable(true);

        if (chosenNames.indexOf(name) == chosenNames.size() - 1) {
            nextName.setDisable(true);
        }
        if (chosenNames.indexOf(name) == 0) {
            lastName.setDisable(true);
        }
    }

    /**
     * Report the selected file as bad
     */
    @FXML
    private void report() {
        File file = retrieveFile();
        if (isBadFile()) {
            fileLogger.unReport(file);
            noReport();
        } else {
            fileLogger.report(file);
            setBadWarningLabel();
        }
    }

    /**
     * Retrieve the file currently selected in the list
     */
    private File retrieveFile() {
        String date = dateList.getSelectionModel().getSelectedItems().get(0).toString();
        return fileManager.getFile(name, date);
    }

    /**
     * Deletes the file if it contains a user recording
     */
    @FXML
    private void deleteFile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation ");
        alert.setContentText("Are you sure you wish to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK

            retrieveFile().delete();
            fileManager.removeFile(retrieveFile());
            updateDates();
        }
    }

    /**
     * Set warning label
     */
    private void setBadWarningLabel() {
        badWarningLabel.setText("Warning: The selected recording had been reported as bad.");
        reportButton.setText("Unreport");
    }

    /**
     * Check if a recording has been marked as bad and throw an error message if required
     */
    @FXML
    private boolean isBadFile() {
        if (retrieveFile().getName().contains("ser")) {
            deleteButton.setDisable(false);

        } else {
            deleteButton.setDisable(true);
        }


        if (fileLogger.isBad(retrieveFile())) {
            setBadWarningLabel();
            return true;
        } else {
            noReport();
            return false;
        }
    }

    /**
     * Remove warning label
     */
    private void noReport() {
        badWarningLabel.setText("");
        reportButton.setText("Report");
    }

    /**
     * Method to load the microphone test scene.
     */
    @FXML
    private void MicrophoneTest() throws IOException {
        stop();
        Stage primaryStage = (Stage) microphoneButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TestMic.fxml"));
        Parent root = loader.load();

        SceneManager.getInstance().addScene(recordButton.getScene(), loader.getController());
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    /**
     * Method to load the record audio scene
     */
    @FXML
    private void recordAudio() throws IOException {
        stop();
        Stage primaryStage = (Stage) recordButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordGui.fxml"));
        Parent root = loader.load();

        RecordGui controller = loader.getController();
        if (isSingleWord()) {
            controller.initData(retrieveFile());
        } else {
            controller.initDataX(name);
        }

        SceneManager.getInstance().addScene(recordButton.getScene(), controller);

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

    }
}
