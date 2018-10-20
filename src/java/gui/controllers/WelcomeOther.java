package gui.controllers;

import java.io.IOException;

import gui.controllers.achievements.Achievements;
import gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class WelcomeOther {

    @FXML
    private Button startButton;

    @FXML
    private ImageView practice;
    @FXML
    private ImageView help;
    @FXML
    private ImageView exit;
    @FXML
    private ImageView achievement;

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
    public void help() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Help.fxml"));

        Parent root = loader.load();

        Help controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    @FXML
    public void startAcheivements() throws IOException {
        Scene scene = startButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("achievements/Achievements.fxml"));

        Parent root = loader.load();

        Achievements controller = loader.getController();


        SceneManager.getInstance().addScene(scene, controller);
        Stage primaryStage = (Stage) startButton.getScene().getWindow();

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    @FXML
    private void quit() {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void setDarkAchievements() {
        setImage(achievement, "award", true);
    }

    private void setImage(ImageView button, String name, boolean isDark) {
        String suffix = "";
        if (isDark) {
            suffix = "-dark";
        }
        button.setImage(new Image(getClass().getClassLoader().getResource("icons/" + name +
                suffix + ".png").toString()));
    }

    @FXML
    private void setLightAchievements() {
        setImage(achievement, "award", false);
    }

    @FXML
    private void setLightExit() {
        setImage(exit, "exit", false);
    }

    @FXML
    private void setDarkExit() {
        setImage(exit, "exit", true);
    }

    @FXML
    private void setLightHelp() {
        setImage(help, "help", false);
    }

    @FXML
    private void setDarkHelp() {
        setImage(help, "help", true);
    }

    @FXML
    private void setDarkPractice() {
        setImage(practice, "chat", true);
    }

    @FXML
    private void setLightPractice() {
        setImage(practice, "chat", false);
    }
}