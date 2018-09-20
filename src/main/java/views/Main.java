package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = this.getClass().getResource("controller/selectionMenu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Name Sayer");
        Scene scene = new Scene(root, 600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneManager.getInstance().setMainStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
