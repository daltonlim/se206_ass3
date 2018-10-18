package Gui;

import Backend.File.FileLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main program to start the application
 */
public class MergeController extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
          Thread.setDefaultUncaughtExceptionHandler(Main::showErrror);
        URL location = this.getClass().getResource("Controllers/SelectionMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Name Sayer");
        Scene scene = new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.getInstance().setMainStage(primaryStage);
    }
 private static void showError(Thread t, Throwable e) {
 }
 @Override
    public void stop() throws Exception {
        FileLogger.getInstance().writeToFile();
        super.stop();
    }
}
