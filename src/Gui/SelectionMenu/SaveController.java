package Gui.SelectionMenu;

import Backend.File.BashWorker;
import Backend.File.FileCreator;
import Backend.NameManagement.NameManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SaveController implements Initializable {
    NameManager fileManager;
    FileCreator fileCreator;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileManager = NameManager.getInstance();
    }

    public void initData(FileCreator creator) {
        creator = fileCreator;
    }

    @FXML
    public void save() {
        fileManager.addFile(fileCreator.getFile());
        exit();
    }

    @FXML
    public void nosave() {
        fileCreator.removeFile();
        NameManager.getInstance().removeFile(fileCreator.getFile());
        exit();
    }

    private void exit() {
        try {
            SceneManager.getInstance().removeScene();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}