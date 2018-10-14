package Gui.Controllers;

import Backend.achievements.Achievement;
import Backend.achievements.AchievementManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AchievementPopup {

    @FXML
    Label text;
    @FXML
    Label name;

    public void initData(String achievement) {
       Achievement achievement1 = AchievementManager.getInstance().retrieveAchievement(achievement);
       name.setText(achievement1.getStarName() + " Unlocked");
       text.setText("Congratulations on achieving " + achievement1.getCount() + " " + achievement.toLowerCase() + "s");
    }
}
