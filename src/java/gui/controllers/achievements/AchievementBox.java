package gui.controllers.achievements;

import backend.achievements.Achievement;
import backend.achievements.AchievementManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AchievementBox implements Initializable {
    @FXML
    private Label title;

    @FXML
    private Label threeStar;

    @FXML
    private Label twoStar;

    @FXML
    private Label oneStar;

    @FXML
    private CheckBox cb0;
    @FXML
    private CheckBox cb1;
    @FXML
    private CheckBox cb2;
    @FXML
    private BorderPane borderPane;
    private int stars;
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public BorderPane retriveBorderPane() {
        return borderPane;
    }

    public void initData(String name, String imgLoc) {
        Image image = new Image(imgLoc);
        imageView.setImage(image);
        Achievement achievement = AchievementManager.getInstance().retrieveAchievement(name);
        title.setText(achievement.getStarName());
        oneStar.setText(achievement.getOneStar() + " " + name.toLowerCase() + "s");
        twoStar.setText(achievement.getTwoStar() + " " + name.toLowerCase() + "s");
        threeStar.setText(achievement.getThreeStar() + " " + name.toLowerCase() + "s");
        stars = achievement.getStar();
        setStar();
        //Set checkbox to normal colour
        cb0.setStyle("-fx-opacity: 1");
        cb1.setStyle("-fx-opacity: 1");
        cb2.setStyle("-fx-opacity: 1");
    }

    public void initData(String name) {
        Image image =
                new Image(getClass().getClassLoader().getResource("icons/" + name.toLowerCase() + ".png").toString());
        imageView.setImage(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        Achievement achievement = AchievementManager.getInstance().retrieveAchievement(name);
        title.setText(achievement.getStarName());
        oneStar.setText(achievement.getOneStar() + " " + name.toLowerCase() + "s");
        twoStar.setText(achievement.getTwoStar() + " " + name.toLowerCase() + "s");
        threeStar.setText(achievement.getThreeStar() + " " + name.toLowerCase() + "s");
        stars = achievement.getStar();
        setStar();
        //Set checkbox to normal colour
        cb0.setStyle("-fx-opacity: 1");
        cb1.setStyle("-fx-opacity: 1");
        cb2.setStyle("-fx-opacity: 1");
    }

    private void setStar() {
        if (stars >= 3) {
            cb2.setSelected(true);
        }
        if (stars >= 2) {
            cb1.setSelected(true);
        }
        if (stars >= 1) {
            cb0.setSelected(true);
        }
    }
}