package gui;

import gui.controllers.achievements.AchievementPopup;
import gui.controllers.PlayerGuiController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;

/**
 * A class to help easily retrieve the various scenes and controllers so that
 * the user may be able to quickly and easily move back to the previous scene
 * A singleton as only one is needed.
 */
public class SceneManager {
    private Stack sceneStack;
    private Stack controllerStack;
    private Stage mainStage;

    private static SceneManager instance;

    /**
     * Retireve instance
     */
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

    /**
     * Push a scene and controller onto their respective stacks
     */
    public void addScene(Scene scene, Initializable controller) {
        sceneStack.push(scene);
        controllerStack.push(controller);
    }

    /**
     * Take care of setting up previous scenes
     */
    public void removeScene() {
        setScene();

        sceneStack.pop();
        controllerStack.pop();
        //Ensure dates update accordingly
        if (controllerStack.size() != 0 && controllerStack.peek() instanceof PlayerGuiController) {
            ((PlayerGuiController) controllerStack.peek()).updateDates();
        }


    }

    /**
     * Returns the current scene at the top of the stack
     */
    public Scene getScene() {
        return (Scene) sceneStack.peek();
    }

    /**
     * Returns the current controlle rat the top of the stack
     */
    public Initializable getController() {
        return (Initializable) controllerStack.peek();
    }

    /**
     * Set the scene to the one at the top of the stack
     */
    private void setScene() {
        mainStage.setScene((Scene) sceneStack.peek());
        mainStage.show();
    }

    /**
     * A method to set a reference to the main stage so scenes are easily drawn.
     *
     * @param stage
     */
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    /**
     * https://blog.csdn.net/teamlet/article/details/52914301
     */
    public void showDialog(String achievement) {

        URL location = this.getClass().getResource("controllers/achievements/AchievementPopup.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Platform.runLater(() -> {
            try {
                Parent root = fxmlLoader.load();
                Stage primaryStage = new Stage();
                primaryStage.setTitle("achievements Unlocked");
                primaryStage.setX(mainStage.getX());
                primaryStage.setY(mainStage.getY() - 150);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root, 600, 120);
                AchievementPopup controller = fxmlLoader.getController();
                controller.initData(achievement);
                primaryStage.setScene(scene);
                primaryStage.show();

                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                        if (primaryStage.isShowing()) {
                            Platform.runLater(() -> primaryStage.close());
                        }
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
