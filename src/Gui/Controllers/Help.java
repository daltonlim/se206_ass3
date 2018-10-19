package Gui.Controllers;

import Gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Help implements Initializable {
    @FXML
    private WebView webView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void back() {
        SceneManager.getInstance().removeScene();
    }
}
