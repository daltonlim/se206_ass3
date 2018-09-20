package Gui.SelectionMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager  {
    private Stack sceneStack;
    private Stage mainStage;

    private static SceneManager instance;

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    private SceneManager() {
        sceneStack = new Stack();
        mainStage = new Stage();
    }

    public void addScene(Scene scene) {
        sceneStack.push(scene);
    }

    public void removeScene() {
        setScene();
        sceneStack.pop();
    }


    private void setScene() {
        mainStage.setScene((Scene) sceneStack.peek());
        mainStage.show();
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
