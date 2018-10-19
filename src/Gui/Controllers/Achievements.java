package Gui.Controllers;

import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Achievements implements Initializable {


    @FXML
    private GridPane gridPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addBox();

    }

    private void addBox() {
        gridPane.add(getBorderPane("Play"), 0, 0);
        gridPane.add(getBorderPane("Recording"), 1, 0);
        gridPane.add(getBorderPane("Minute"), 0, 1);
        gridPane.add(getBorderPane("Deletion"), 1, 1);
        gridPane.add(getBorderPane("Report"), 0, 2);
        gridPane.add(getBorderPane("Open"), 1, 2);
    }

    @FXML
    public BorderPane getBorderPane(String name) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AchievementBox.fxml"));
        try {
            Parent root = loader.load();
            AchievementBox controller = loader.getController();
            controller.initData(name);

            return controller.retriveBorderPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BorderPane();
    }

    @FXML
    private void back() {
        SceneManager.getInstance().removeScene();
    }
}
