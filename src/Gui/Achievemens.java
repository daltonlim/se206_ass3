package Gui;

import Backend.File.FileLogger;
import Backend.achievements.AchievementManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main program to start the application
 */
public class Achievemens extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        FileLogger.getInstance();
        AchievementManager.getInstance().incrementAchievement("Open");
        URL location = this.getClass().getResource("Controllers/Achievements.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Name Sayer");
        Scene scene = new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.getInstance().setMainStage(primaryStage);
    }
    public void uncaughtException(Thread t, Throwable e) {
    }
    @Override
    public void stop() throws Exception {
        FileLogger.getInstance().writeToFile();
        AchievementManager.getInstance().saveState();
        super.stop();
    }
}
