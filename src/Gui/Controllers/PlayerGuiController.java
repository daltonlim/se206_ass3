package Gui.Controllers;

import Backend.File.BashWorker;
import Backend.File.FileLogger;
import Backend.NameManagement.NameManager;
import Backend.achievements.AchievementManager;
import Backend.achievements.AchievementType;
import Gui.SceneManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

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
    private Button recordButton;
    @FXML
    private Button microphoneButton;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private ToggleButton offButton;
    @FXML
    private ToggleGroup toggleGroup;
    
    private BashWorker worker;

    private List<String> chosenNames;
    private String[] nameArray;
    private boolean setVolume =false;

    private Task<?> playCreatedName;

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
    }

    /**
     * Updates the dateslist when a button is clicked
     */
    @FXML
    public void updateDates() {
        nameLabel.setText(name);
        dateList.getItems().remove(0, dateList.getItems().size());
        if (!isSingleWord()) {
            dateList.getItems().add("Automatically generated file");
        }

        if (fileManager.getFileDatesForName(name).size() != 0) {
            //Cause removeALl command is buggy https://stackoverflow
            // .com/questions/12132896/listview-removeall-doesnt-work

            dateList.getItems().addAll(fileManager.getFileDatesForName(name));

            //dateList.getSelectionModel().select(0);
            //Select first element is list by default
            //isBadFile();
        }
        dateList.getSelectionModel().select(0);
        isBadFile();
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
    
    /**
     * set volume as 10dB
     */
    @FXML
    public void setVolume() {
    	setVolume=true;
    }
    
    /**
     * set volume as normal
     */
    @FXML
    public void off() {
    	setVolume=false;
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
        AchievementManager.getInstance().incrementAchievement(AchievementType.PLAY);
        stop();
        if (!isSingleWord() && dateList.getSelectionModel().getSelectedIndex() == 0) {
            playCreatedName = createWorker();
            new Thread(playCreatedName).start();
        } else {
            File file = retrieveFile();
            String location = file.toURI().toString();
            //Replace spaces succesfully
            location = location.replace("%20", " ");
            if(setVolume) {
            	worker = new BashWorker("ffplay -af \"volume=10dB\" -nodisp -autoexit '" + location + "'");
            } else {
                worker = new BashWorker("ffplay -nodisp -autoexit '" + location + "'");
            }
        }
    }
    
    /**
     * A task to play created name
     */
    public Task<?> createWorker() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < nameArray.length; i++) {
                    try {
                        File file = fileManager.getRandomGoodFile(nameArray[i]);
                        String location = file.toURI().toString();
                        if(setVolume) {
                        	worker = new BashWorker("ffplay -af \"volume=10dB\" -nodisp -autoexit '" + location + "'");
                        } else {
                            worker = new BashWorker("ffplay -nodisp -autoexit '" + location + "'");
                        }
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                        AudioFormat format = audioInputStream.getFormat();
                        long frames = audioInputStream.getFrameLength();
                        double durationInSeconds = (frames + 0.0) / format.getFrameRate();
                        int time = (int) (durationInSeconds * 1000);
                        Thread.sleep(time);
                    } catch (Exception e) {
                        break;
                    }
                }
                return true;
            }
        };
    }

    private void stop() {
        if (worker != null) {
            worker.kill();
        }

       if (playCreatedName != null) {
            playCreatedName.cancel(true);
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
        retrieveFile().delete();
        fileManager.removeFile(retrieveFile());
        updateDates();
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
        if (!isSingleWord() && dateList.getSelectionModel().getSelectedIndex() == 0) {
            reportButton.setDisable(true);
            deleteButton.setDisable(true);
            return false;
        } else {
            deleteButton.setDisable(false);
            reportButton.setDisable(false);
        }
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
        primaryStage.setScene(new Scene(root, 1200, 800));
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

        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();

    }
}
