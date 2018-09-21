package Gui.SelectionMenu;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class SceneManager {
    private Stack sceneStack;
    private Stack controllerStack;
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
        controllerStack = new Stack();
    }

    public void addScene(Scene scene, Initializable controller) {
        sceneStack.push(scene);
        controllerStack.push(controller);
    }

    public void removeScene() {
        setScene();
        sceneStack.pop();
        controllerStack.pop();

        //Ensure dates update accordingly
        if(controllerStack.peek() instanceof PlayerGuiController){
            ((PlayerGuiController) controllerStack.peek()).updateDates();
        }

    }

    public Scene getScene(){
        return (Scene) sceneStack.peek();
    }
    public Initializable getController(){
        return (Initializable) controllerStack.peek();
    }

    private void setScene() {
        mainStage.setScene((Scene) sceneStack.peek());
        mainStage.show();
    }

    public void setMainStage(Stage stage){
        this.mainStage = stage;
    }
}
