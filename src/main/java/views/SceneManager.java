package views;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager {
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
    }

    public void addScene(Scene scene) {
        sceneStack.push(scene);
    }

    public void removeScene() {
        setScene();
        sceneStack.pop();
    }

    public Scene getScene(){
        return (Scene) sceneStack.peek();
    }

    private void setScene() {
        mainStage.setScene((Scene) sceneStack.peek());
        mainStage.show();
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
