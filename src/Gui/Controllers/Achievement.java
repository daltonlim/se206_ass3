package Gui.Controllers;

import Backend.achievements.AchievementManager;
import Backend.achievements.AchievementType;
import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Achievement implements Initializable {


    @FXML
    private CheckBox cb1;

    @FXML
    private CheckBox cb2;

    @FXML
    private CheckBox cb3;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int stars = AchievementManager.getInstance().getStar("Play");
        if(stars >= 3){
            cb3.setSelected(true);
        }
        if(stars >= 2){
            cb2.setSelected(true);
        }
        if (stars>=1){
            cb1.setSelected(true);
        }
    }

    @FXML
    private void back(){
        SceneManager.getInstance().removeScene();
    }
}
