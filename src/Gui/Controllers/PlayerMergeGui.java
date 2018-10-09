package Gui.Controllers;

import Backend.File.BashWorker;
import Backend.File.FileLogger;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerMergeGui implements Initializable {
    private NameManager fileManager;
    private FileLogger fileLogger;
    private String name;
    private int index = 0;
    private File[] files;
    private Boolean disableNext;
    private int[] selectionArray;

    @FXML
    private Label badWarningLabel;
    @FXML
    private Button nextName;
    @FXML
    private Button lastName;
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

    private BashWorker worker;

    private List<String> chosenNames;


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
        microphoneButton.setDisable(true);
        disableNext =true;
        selectionArray = new int[] {0};
        nextName.setVisible(false);
        lastName.setVisible(false);

    }

    /**
     * Updates the dateslist when a button is clicked
     */
    @FXML
    public void updateDates() {
        nameLabel.setText(name);
        //Cause removeALl command is buggy https://stackoverflow.com/questions/12132896/listview-removeall-doesnt-work
        dateList.getItems().remove(0, dateList.getItems().size());
        dateList.getItems().addAll(fileManager.getFileDatesForName(name));


        //Select first element is list by default
        dateList.getSelectionModel().select(selectionArray[index]);

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
        }
        checkButtons();
        updateDates();

    }

    /**
     * Play the file selected
     */
    @FXML
    private void play() {
        stop();
        try {
            File file = retrieveFile();
            String location = file.toURI().toString();
            worker = new BashWorker("ffplay -nodisp -autoexit " + location);
        } catch (Exception e) {
            e.printStackTrace();
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
    void initData(List<String> names) {
        chosenNames = new ArrayList<>(names);
        index = 0;
        name = chosenNames.get(0);

        nameLabel.setText(name);
        checkButtons();
        updateDates();
        files = new File[names.size()];
        selectionArray = new int[names.size()];
    }

    /**
     * Method to ensure the next and previous buttons are enabled when required
     */
    private void checkButtons() {
        nextName.setDisable(disableNext);
        lastName.setDisable(false);

        if (index == chosenNames.size() - 1) {
            nextName.setDisable(true);
        }
        if (index == 0) {
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
    private void combineNames() throws IOException {
        stop();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    /**
     * Method to load the record audio scene
     */
    @FXML
    private void select() throws IOException {
        files[index] = retrieveFile();
        selectionArray[index] = dateList.getSelectionModel().getSelectedIndex();
        if (index == chosenNames.size() - 1) {
            microphoneButton.setDisable(false);
            disableNext = false;
            nextName.setVisible(true);
            lastName.setVisible(true);
            checkButtons();
        }
        getNext();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
