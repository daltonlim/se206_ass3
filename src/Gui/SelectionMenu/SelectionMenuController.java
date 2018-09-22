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
    private ListView availibleNamesList;
    @FXML
    private ListView selectedNames;
    @FXML
    private Button selectNamesButton;

    /**
     * Initialiser method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _maxOne = true;
        fileManager = NameManager.getInstance();
        selectNamesButton.setDisable(true);

        //Add availableNames to the list
        List<String> sortedList = fileManager.getAvailableNames();
        Collections.sort(sortedList);
        availibleNamesList.getItems();
        availibleNamesList.getItems().addAll(sortedList);
        checkAll();
        ordered = true;
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
        if (!_maxOne || selectedNames.getItems().size() == 0) {
            selectedNames.getItems().add(name);
            availibleNamesList.getItems().remove(name);
        }
        // Resort
        Collections.sort(selectedNames.getItems());
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
        availibleNamesList.getItems().add(name);
        selectedNames.getItems().remove(name);
        //Resort
        Collections.sort(availibleNamesList.getItems());
        checkAll();
    }

    /**
     * Set the single flag to true when the corresponding button is pressed
     */
    @FXML
    public void setSingle() {
        _maxOne = true;
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
        _maxOne = false;
        checkAll();
        ordered = false;
    }

    /**
     * Set correct fields when an ordered list is requested.
     */
    @FXML
    public void setOrdered() {
        _maxOne = false;
        checkAll();
        ordered = true;
    }

    /**
     * Starts the player gui scene
     */
    @FXML
    public void getPlayerGuiScene() throws Exception {
        Scene scene = selectNamesButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerGui.fxml"));

        Parent root = loader.load();

        PlayerGuiController controller = loader.getController();
        controller.initData(selectedNames.getItems(), ordered);


        SceneManager.getInstance().addScene(scene,controller);
        Stage primaryStage = (Stage) selectNamesButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


    }

    


}
