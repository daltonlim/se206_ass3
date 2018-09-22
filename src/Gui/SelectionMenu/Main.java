package Gui.SelectionMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main program to start the application
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = this.getClass().getResource("selectionMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Name Sayer");
        Scene scene = new Scene(root, 600,600);
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.getInstance().setMainStage(primaryStage);
    }

}
