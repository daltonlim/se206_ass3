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

public class WelcomeOther {

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
    @FXML
    public void startMerge() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Merge.fxml"));

        Parent root = loader.load();

        MergeController controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
    @FXML
    public void startAcheivements() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Achievement.fxml"));

        Parent root = loader.load();

        Achievement controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}