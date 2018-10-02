package Gui.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import Backend.File.BashWorker;

import Backend.File.FileCreator;
import Backend.File.FileNameParser;
import Backend.NameManagement.NameManager;
import Gui.SceneManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class dummy implements Initializable {
    @FXML
    private Label States;
    @FXML
    private Label nameLabel;
    @FXML
    private Button PlayOldButton;
    @FXML
    private Button RestartButton;
    @FXML
    private Button recordButton;
    @FXML
    private Button ExitButton;
    @FXML
    private Button PlayYoursButton;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private Button stopButton;
    
    private BashWorker worker;
    private NameManager fileManager;
    private Task<?> _Recording;
    private FileCreator fileCreator;
    private FileNameParser fileParser;
    private BashWorker bashWorker;
    private Boolean _RecordingIsFinished =false;
    private Boolean isSingleWord;
    private String[] nameArray;
    private String _name;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableButtons(true);
        fileManager = NameManager.getInstance();
        States.setVisible(false);
    }

	private void disableButtons(boolean b) {
        //PlayOldButton.setDisable(b);
        PlayYoursButton.setDisable(b);
        RestartButton.setDisable(b);
    }

    public Task<?> createWorker() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                    updateMessage("Recording Completed : " + ((i * 20) + 20) + "%");
                    updateProgress(i + 1, 5);
                }
                RecordingIsFinished();
                return true;
            }
        };
    }

    @FXML
    public void record() {
        try {
            recordButton.setDisable(true);
            States.setVisible(true);
            PlayOldButton.setDisable(true);
            progressbar.setProgress(0.0);

            _Recording = createWorker();

            progressbar.progressProperty().unbind();

            progressbar.progressProperty().bind(_Recording.progressProperty());

            _Recording.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    States.setText(newValue);
                }
            });
            new Thread(_Recording).start();
            fileCreator.generateAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void RecordingIsFinished() {
        disableButtons(false);
        _RecordingIsFinished =true;
    }

    private void play(File audioFile) {
        stop();
        String location = audioFile.toURI().toString();
        bashWorker = new BashWorker("ffplay -nodisp -autoexit " + location);
    }

    @FXML
    public void playNew() throws InterruptedException {
        try {
            File audioFile = new File(fileCreator.fileString());
            play(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void playOld() throws InterruptedException {
        if (isSingleWord) {
        	try {
                States.setVisible(false);
                File audioFile = fileParser.getFile();
                play(audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
        	for (int i = 0; i < nameArray.length; i++) {
        		try {
        			List<String> Dates = fileManager.getFileDatesForName(nameArray[i]);
        			int index = new Random().nextInt(Dates.size());
            		String oldestDate = Dates.get(index);
            		File file = fileManager.getFile(nameArray[i], oldestDate);
            		String location = file.toURI().toString();
                    worker = new BashWorker("ffplay -nodisp -autoexit " + location);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    AudioFormat format = audioInputStream.getFormat();
                    long frames = audioInputStream.getFrameLength();
                    double durationInSeconds = (frames+0.0) / format.getFrameRate();
                    int time = (int) (durationInSeconds*1000);
                    Thread.sleep(time);
                } catch (Exception e) {
                	e.printStackTrace();
                }	
        	}
        }
    }

    public void initData(File file) {
        fileParser = new FileNameParser(file);
        String name = fileParser.getUserName();
        fileCreator = new FileCreator(name);
        isSingleWord=true;
        nameLabel.setText(name);
    }

    @FXML
    public void restart() throws IOException {
    	reload();
    }
    
    public void reload() throws IOException {
        stop();
        fileCreator.removeFile();
        Stage primaryStage = (Stage) recordButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dummy.fxml"));
        Parent root = loader.load();

        dummy controller = loader.<dummy>getController();
        if (isSingleWord) {
        	controller.initData(fileParser.getFile());
        } else {
        	controller.initDataX(_name);
        }

        primaryStage.setScene(new Scene(root,600,600));
        primaryStage.show();
    }
    

    @FXML
    public void exit() throws IOException {
    	if (_RecordingIsFinished) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("dummy");
    		alert.setHeaderText(null);
    		alert.setContentText("Do you want save this recording?");
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == ButtonType.OK) {
    			NameManager.getInstance().addFile(fileCreator.getFile());
    			stop();
    	        SceneManager.getInstance().removeScene();
    		} else {
    			fileCreator.removeFile();
    	        NameManager.getInstance().removeFile(fileCreator.getFile());
    	        stop();
    	        SceneManager.getInstance().removeScene();
    		}
    	} else {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("dummy");
    		alert.setHeaderText(null);
    		alert.setContentText("Do you want leave this page?");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == ButtonType.OK) {
    			stop();
    	        SceneManager.getInstance().removeScene();
    		} else {
    			reload();
    		}
    	}
    }

    @FXML
    public void stopRecording() {
    	 
    }
    
    private void stop(){
        if(bashWorker!=null){
            bashWorker.kill();
        }
    }
    
	public void initDataX(String name) {
		fileCreator = new FileCreator("name");
		_name=name;
		isSingleWord=false;
		nameArray = name.split("[ -]");
        nameLabel.setText(name);	
	}
}