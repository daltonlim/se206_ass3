package Gui.SelectionMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectionMenu.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneManager.getInstance().setMainStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
