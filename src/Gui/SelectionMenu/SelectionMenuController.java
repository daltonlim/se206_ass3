package Gui.SelectionMenu;

import Backend.NameManagement.NameManager;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class SelectionMenuController implements Initializable {

    private boolean _maxOne;
    NameManager fileManager;
    private boolean ordered;

    @FXML
    private ListView AvailibleNamesList;
    @FXML
    private ListView ChosenNames;
    @FXML
    private Button selectNamesButton;
    @FXML
    private Button remove;
    @FXML
    private Button add;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _maxOne = true;
        fileManager = new NameManager();
        selectNamesButton.setDisable(true);

        //Add availableNames to the list
        List<String> sortedList = fileManager.getUniqueNames();
        java.util.Collections.sort(sortedList);
        AvailibleNamesList.getItems().addAll(sortedList);
        checkAll();
        ordered = true;
    }

    @FXML
    private void addName() {
        String name = AvailibleNamesList.getSelectionModel().getSelectedItems().get(0).toString();
        /*Only allow addition, if max one is not enforced,
        or if it is, only allow if the list is empty
         */
        if (!_maxOne || ChosenNames.getItems().size() == 0) {
            ChosenNames.getItems().add(name);
            AvailibleNamesList.getItems().remove(name);
        }
        // Resort
        Collections.sort(ChosenNames.getItems());
        checkAll();
    }

    //Make button clickable if one or more names are in mnames to play
    private void checkAll() {
        if (ChosenNames.getItems().size() != 0) {
            selectNamesButton.setDisable(false);
        } else {
            selectNamesButton.setDisable(true);
        }
        if (AvailibleNamesList.getItems().size() == 0 || ChosenNames.getItems().size() == 1 && _maxOne) {
            add.setDisable(true);
        } else {
            add.setDisable(false);
        }

        if (ChosenNames.getItems().size() == 0) {
            remove.setDisable(true);
        } else {
            remove.setDisable(false);
        }
    }

    @FXML
    private void removeName() {
        String name = ChosenNames.getSelectionModel().getSelectedItems().get(0).toString();

        AvailibleNamesList.getItems().add(name);
        ChosenNames.getItems().remove(name);
        //Resort
        Collections.sort(AvailibleNamesList.getItems());
        checkAll();
    }

    @FXML
    public void setSingle() {
        _maxOne = true;
        int runs = ChosenNames.getItems().size();
        for (int i = 1; i < runs; i++) {
            AvailibleNamesList.getItems().add(ChosenNames.getItems().get(1));
            ChosenNames.getItems().remove(1);
        }

        Collections.sort(AvailibleNamesList.getItems());
        checkAll();

    }

    @FXML
    public void setRandomised() {
        _maxOne = false;
        checkAll();
        ordered = false;
    }

    @FXML
    public void setOrdered() {
        _maxOne = false;
        checkAll();
        ordered = true;
    }

    @FXML
    public void getPlayerGuiScene() throws Exception{
        Stage primaryStage =(Stage) add.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(  "playerGui.fxml"));

        Parent root = loader.load();

        playerGuiController controller = loader.<playerGuiController>getController();
        controller.initData(ChosenNames.getItems(),fileManager,ordered);

        primaryStage.setScene(new Scene(root, 600,600));
        primaryStage.show();
    }


}
