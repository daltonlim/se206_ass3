package Scratch;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.*;
import javafx.stage.Stage;

import java.io.File;

public class VideoTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        Media pick = new Media("audioFiles/se206_27-4-2018_16-18-3_Francis.wav"); // replace this with your own audio
        // file
        MediaPlayer player = new MediaPlayer(pick);

        // Add a mediaView, to display the media. Its necessary !
        // This mediaView is added to a Pane
        MediaView mediaView = new MediaView(player);

        // Add to scene
        Group root = new Group(mediaView);
        Scene scene = new Scene(root, 500, 200);

        // Show the stage
        primaryStage.setTitle("Media Player");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Play the media once the stage is shown
        player.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}