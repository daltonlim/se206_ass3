package Gui.Controllers;

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
    public void startAcheivements() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Achievements.fxml"));

        Parent root = loader.load();

        Achievements controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
    @FXML
    private void quit(){
      Stage stage =   (Stage) startButton.getScene().getWindow();
      stage.close();
    }
}