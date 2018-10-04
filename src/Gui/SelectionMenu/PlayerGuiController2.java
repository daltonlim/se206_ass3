package Gui.SelectionMenu;

import Backend.File.BashWorker;
import Backend.File.FileLogger;
import Backend.NameManagement.NameManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerGuiController2 implements Initializable {
    NameManager fileManager;
    FileLogger fileLogger;
    String _name;
    private int index = 0;

    @FXML
    private Label badWarningLabel;
    @FXML
    private Button nextName;
    @FXML
    private Button lastName;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView dateList;
    @FXML
    private Button recordButton;
    @FXML
    private Button microphoneButton;

    private BashWorker worker;

    private List<String> _chosenNames;
	private String _date;


    //todo fix list type not being remembered
    @FXML
    private void goBack() throws IOException {
        SceneManager.getInstance().removeScene();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileLogger = FileLogger.getInstance();
        fileManager = NameManager.getInstance();
    }

    /**
     * Updates the dateslist when a name is clicked
     */
    @FXML
    public void updateDates() {
        nameLabel.setText(_name);
        //Cause removeALl command is buggy https://stackoverflow.com/questions/12132896/listview-removeall-doesnt-work
        dateList.getItems().remove(0, dateList.getItems().size());
        dateList.getItems().addAll(fileManager.getFileDatesForName(_name));


        //Select first element is list by default
        dateList.getSelectionModel().select(0);
        defaultDate(dateList.getSelectionModel().getSelectedItems().get(0).toString());
        isBadFile();
    }
    
    private void defaultDate(String string) {
    	_date= string;		
	}

    @FXML
    public void getNext() {
        if (_chosenNames.get(index++) != null) {
            _name = _chosenNames.get(index);
            nameLabel.setText(_name);
        } else {
            index--;
        }
        updateDates();
        checkButtons();
    }

    @FXML
    public void getLast() {
        if (_chosenNames.get(index--) != null) {
            _name = _chosenNames.get(index);
        } else {
            index++;
        }
        checkButtons();
        updateDates();

    }

    @FXML
    private void play() {
        if(worker!=null)
        worker.kill();
        try {
            File file = retrieveFile();
            String location = file.toURI().toString();
            worker = new BashWorker("ffplay -nodisp -autoexit " + location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initData(List<String> names, Boolean ordered) {
        if (!ordered) {
            Collections.shuffle(names);
        }
        _chosenNames = names;
        index = 0;
        _name = names.get(0);

        nameLabel.setText(_name);
        checkButtons();
        updateDates();
    }

    private void checkButtons() {
        nextName.setDisable(false);
        lastName.setDisable(false);

        if (_chosenNames.indexOf(_name) == _chosenNames.size() - 1) {
            nextName.setDisable(true);
        }
        if (_chosenNames.indexOf(_name) == 0) {
            lastName.setDisable(true);
        }
    }

    @FXML
    private void report() {
        File file = retrieveFile();
        fileLogger.report(file);
        setBadWarningLabel();
    }

    private File retrieveFile() {
        String date = dateList.getSelectionModel().getSelectedItems().get(0).toString();
        return fileManager.getFile(_name, date);
    }

    private void setBadWarningLabel() {
        badWarningLabel.setText("Warning: The selected recording had been reported as bad.");
    }

    @FXML
    private void isBadFile() {
        if (fileLogger.isBad(retrieveFile())) {
            setBadWarningLabel();
        } else {
            badWarningLabel.setText("");
        }
    }

    @FXML
    private void MicrophoneTest() throws IOException {
        Stage primaryStage = (Stage) microphoneButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Test.fxml"));
        Parent root = loader.load();

        SceneManager.getInstance().addScene(recordButton.getScene(), loader.getController());


        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    @FXML
    private void recordAudio() throws IOException {
        Stage primaryStage = (Stage) recordButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordGui.fxml"));
        Parent root = loader.load();

        RecordGuiController controller = loader.<RecordGuiController>getController();

        controller.initData(_name,_date);


        SceneManager.getInstance().addScene(recordButton.getScene(), controller);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    Scene scene;

}
