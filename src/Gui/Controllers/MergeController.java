package Gui.Controllers;

import Backend.File.FileLogger;
import Backend.File.FileNameParser;
import Backend.File.TextFileParser;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
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

public class MergeController implements Initializable {

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
    private void comboBox() {
        String text = new String(cb.getEditor().getText());
        text = FileNameParser.sentenceCase(text);
        if (text.length() > 0) {
            cb.show();
            cb.getItems().remove(0, cb.getItems().size());
            cb.getItems().addAll(fileManager.retrieveSinglePrefix(text));
            cb.setVisibleRowCount(10);

        }
    }

    /**
     * Initialiser method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        single = false;
        fileManager = NameManager.getInstance();
        selectNamesButton.setDisable(true);
        setupList();
        ordered = true;
    }

    private void setupList() {
        //Add availableNames to the list
        List<String> sortedList = fileManager.getSingleNames();
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
        }
        if (single) {
            availibleNamesList.getItems().addAll(selectedNames.getItems());
            selectedNames.getItems().remove(0);
            Collections.sort(availibleNamesList.getItems());
            selectedNames.getItems().add(name);
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
        String name = selectedNames.getSelectionModel().getSelectedItems().get(0).toString();
        selectedNames.getItems().remove(name);
        //Resort
        checkAll();
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
        alert.setHeaderText("Invalid names detected. Would you like to add partial names?");
        alert.setContentText(textFileParser.getNotPossibleNameString());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            //TODo remove this duplicate
            selectedNames.getItems().addAll(textFileParser.getPartialNames());
            removeDuplicates(selectedNames.getItems());
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerMergeGui.fxml"));

        Parent root = loader.load();

        PlayerMergeGui controller = loader.getController();
        controller.initData(selectedNames.getItems() );


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

        String text = cb.getEditor().getText();
        TextFileParser textFileParser = new TextFileParser(text);
        String[] strings = text.split("[ -]");
        if(strings.length > 1){
            alertBox(textFileParser);
        }else {
            addNames(textFileParser);
        }
        checkAll();
    }

    private void addNames(TextFileParser textFileParser) {
        selectedNames.getItems().addAll(textFileParser.getNamesToAdd());

        if (!textFileParser.getNotPossibleNames().isEmpty()) {
            alertBox(textFileParser);
        }

    }

    @FXML
    public void exportNames() {
        FileLogger.getInstance().writeToFile("Logs/exportNames" + java.time.LocalDateTime.now().toString() + ".txt",
                selectedNames.getItems());
    }
    @FXML
    private void exit(){
        SceneManager.getInstance().removeScene();
    }
}
