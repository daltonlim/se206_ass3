package Gui.Controllers;

import java.io.File;
import java.io.IOException;

import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Welcome {

    @FXML
    private Button startButton;

    @FXML
    public void start() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectionMenu.fxml"));

        Parent root = loader.load();

        SelectionMenuController controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}