package Gui.Controllers;

import Backend.File.FileLogger;
import Backend.File.FileNameParser;
import Backend.File.TextFileParser;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class SelectionMenuController implements Initializable {

    private boolean single;
    NameManager fileManager;
    private boolean ordered;
    @FXML
    private ListView availibleNamesList;
    @FXML
    private ListView selectedNames;
    @FXML
    private Button selectNamesButton;
    @FXML
    private ComboBox cb;
    @FXML
    private ToggleButton singleButton;
    @FXML
    private ToggleButton orderedButton;
    @FXML
    private ToggleButton randomisedButton;

    @FXML
    private void comboBox() {
        String text = new String(cb.getEditor().getText());
        text = FileNameParser.sentenceCase(text);
        cb.show();
        cb.getItems().remove(0, cb.getItems().size());
        cb.getItems().addAll(fileManager.retrievePrefix(text));
        cb.setVisibleRowCount(10);
    }

    /**
     * FUnction to ensure the toggle button group cannot be disabled.
     *
     * @param button
     */
    private void setEnabledButton(ToggleButton button) {
        singleButton.setDisable(false);
        orderedButton.setDisable(false);
        randomisedButton.setDisable(false);
        button.setDisable(true);
    }

    /**
     * Initialiser method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        single = true;
        fileManager = NameManager.getInstance();
        selectNamesButton.setDisable(true);
        setupList();
        ordered = true;
    }

    private void setupList() {
        //Add availableNames to the list
        List<String> sortedList = fileManager.getAvailableNames();
        Collections.sort(sortedList);
        availibleNamesList.getItems().addAll(sortedList);
        checkAll();
    }

    /**
     * Add a name to the selected names list, and remove from the available names
     */
    @FXML
    private void addName() {
        String name = availibleNamesList.getSelectionModel().getSelectedItems().get(0).toString();
        /*Only allow addition, if max one is not enforced,
        or if it is, only allow if the list is empty
         */
        if (!single || selectedNames.getItems().size() == 0) {
            selectedNames.getItems().add(name);
            availibleNamesList.getItems().remove(name);
            Collections.sort(selectedNames.getItems());
        }
        if (single) {
            availibleNamesList.getItems().addAll(selectedNames.getItems());
            selectedNames.getItems().remove(0);
            Collections.sort(availibleNamesList.getItems());
            selectedNames.getItems().add(name);
            availibleNamesList.getItems().remove(name);
        }
        // Resort
        checkAll();
    }

    //Make button clickable if one or more names are in names to play
    private void checkAll() {
        if (selectedNames.getItems().size() != 0) {
            selectNamesButton.setDisable(false);
        } else {
            selectNamesButton.setDisable(true);
        }

    }

    /**
     * Removes a name from the selectedNames and adds it bach to available names
     */
    @FXML
    private void removeName() {
        if (selectedNames.getSelectionModel().getSelectedItems().get(0) != null) {
            String name = selectedNames.getSelectionModel().getSelectedItems().get(0).toString();

            availibleNamesList.getItems().add(name);
            selectedNames.getItems().remove(name);
            //Resort
            Collections.sort(availibleNamesList.getItems());
            checkAll();
        }
    }

    /**
     * Set the single flag to true when the corresponding button is pressed
     */
    @FXML
    public void setSingle() {
        setEnabledButton(singleButton);

        single = true;
        int runs = selectedNames.getItems().size();
        for (int i = 1; i < runs; i++) {
            availibleNamesList.getItems().add(selectedNames.getItems().get(1));
            selectedNames.getItems().remove(1);
        }

        Collections.sort(availibleNamesList.getItems());
        checkAll();

    }

    /**
     * Set correct flags when randomised list button pressed.
     */
    @FXML
    public void setRandomised() {
        single = false;
        setEnabledButton(randomisedButton);
        checkAll();
        ordered = false;
    }

    /**
     * Set correct fields when an ordered list is requested.
     */
    @FXML
    public void setOrdered() {
        setEnabledButton(orderedButton);
        single = false;
        checkAll();
        ordered = true;
    }

    /**
     * Open file chooser
     */
    @FXML
    private void fileChooser() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(selectNamesButton.getScene().getWindow());
        TextFileParser textFileParser = new TextFileParser(file);
        addNames(textFileParser);
    }

    /**
     * Alert box to let the user add partial names if required.
     *
     * @param textFileParser
     */
    @FXML
    private void alertBox(TextFileParser textFileParser) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Invalid Names");
        alert.setHeaderText(null);
        alert.setContentText("Invalid names detected. Would you like to add partial names? " + textFileParser.getNotPossibleNameString());
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/resources/FlatBee.css").toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //TODo remove this duplicate
            selectedNames.getItems().addAll(textFileParser.getPartialNames());
            availibleNamesList.getItems().removeAll(textFileParser.getPartialNames());
            removeDuplicates(selectedNames.getItems());
            Collections.sort(selectedNames.getItems());
        }

    }

    private void removeDuplicates(List<String> stringList) {
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(stringList);
        stringList.clear();
        stringList.addAll(hashSet);
    }

    /**
     * Starts the player gui scene
     */
    @FXML
    public void getPlayerGuiScene() throws Exception {
        Scene scene = selectNamesButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerGui.fxml"));

        Parent root = loader.load();

        PlayerGuiController controller = loader.getController();
        controller.initData(selectedNames.getItems(), ordered);


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) selectNamesButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


    }

    /**
     * Starts the Achievements scene
     */
    @FXML
    public void getAchievements() throws Exception {
        Scene scene = selectNamesButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Achievements.fxml"));

        Parent root = loader.load();

        Achievements controller = loader.getController();
        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) selectNamesButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


    }

    @FXML
    private void addDatabase() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(selectNamesButton.getScene().getWindow());

        if (selectedDirectory != null) {
            fileManager.getFiles(selectedDirectory);
        }
        availibleNamesList.getItems().remove(0, availibleNamesList.getItems().size());
        setupList();
    }

    @FXML
    private void searchNames() {
        if (!cb.getEditor().getText().isEmpty()) {
            TextFileParser textFileParser = new TextFileParser(cb.getEditor().getText());
            addNames(textFileParser);
            checkAll();
        }
    }

    private void addNames(TextFileParser textFileParser) {
        if (single) {
            availibleNamesList.getItems().addAll(selectedNames.getItems());
            if (selectedNames.getItems().size() == 1)
                selectedNames.getItems().remove(0);
            Collections.sort(availibleNamesList.getItems());
        }
        selectedNames.getItems().addAll(textFileParser.getNamesToAdd());
        availibleNamesList.getItems().removeAll(textFileParser.getNamesToAdd());
        Collections.sort(selectedNames.getItems());
        removeDuplicates(selectedNames.getItems());

        if (!textFileParser.getNotPossibleNames().isEmpty()) {
            alertBox(textFileParser);
        }

        Collections.sort(selectedNames.getItems());
    }

    @FXML
    public void exportNames() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("Logs"));
        fileChooser.setInitialFileName("exportNames" + java.time.LocalDateTime.now().toString() + ".txt");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.setSelectedExtensionFilter(extFilter);
        FileLogger.getInstance().writeToFile(fileChooser.showSaveDialog(selectNamesButton.getScene().getWindow()),
                selectedNames.getItems());
    }

    @FXML
    private void exit() {
        SceneManager.getInstance().removeScene();
    }

}
