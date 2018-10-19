package Gui.Controllers;

import Backend.achievements.Achievement;
import Backend.achievements.AchievementManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AchievementBox implements Initializable {
    @FXML
    private Label t0;

    @FXML
    private Label l00;

    @FXML
    private Label l01;

    @FXML
    private Label l02;

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
        t0.setText(achievement.getStarName());
        l02.setText(achievement.getOneStar() + " " + name.toLowerCase() + "s");
        l01.setText(achievement.getTwoStar() + " " + name.toLowerCase() + "s");
        l00.setText(achievement.getThreeStar() + " " + name.toLowerCase() + "s");
        stars = achievement.getStar();
        setStar();
        //Set checkbox to normal colour
        cb0.setStyle("-fx-opacity: 1");
        cb1.setStyle("-fx-opacity: 1");
        cb2.setStyle("-fx-opacity: 1");
    }

    public void initData(String name) {
        Image image =
                new Image(getClass().getResource("../../resources/icons/" + name.toLowerCase() + ".png").toString());
        imageView.setImage(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

/*        File file = new File("/resources/icons/" + name.toLowerCase());
        System.out.println(file.toURI().toString());*/
        /*Image image = new Image("../../resources/icons/"+name.toLowerCase() + ".png");
       image.getHeight();*/

        Achievement achievement = AchievementManager.getInstance().retrieveAchievement(name);
        t0.setText(achievement.getStarName());
        l02.setText(achievement.getOneStar() + " " + name.toLowerCase() + "s");
        l01.setText(achievement.getTwoStar() + " " + name.toLowerCase() + "s");
        l00.setText(achievement.getThreeStar() + " " + name.toLowerCase() + "s");
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