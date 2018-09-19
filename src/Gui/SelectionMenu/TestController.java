package Gui.SelectionMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class TestController implements Initializable{
   
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label States;

    @FXML
    private ProgressBar PB;

    @FXML
    private Label Messages;

    @FXML
    private Button ExitButton;

    @FXML
    private Button PlayButton;

    @FXML
    private Button OK;

    FileCreator _Demo;
    
    Task<?> _Recording;
    
    String _fileName;
    
    String _date; // storing date when it is generated
    
    

    /**
     * when pressed Start button, a demo.wav generate automatically.
     * Using a progress bar to count 5 seconds, which will make the user know 
     * how the recording is going on.
     * @throws InterruptedException
     */
    @FXML
    public void buttonAction() throws InterruptedException {
    	OK.setDisable(true);
        PB.setProgress(0.0);
        _Recording = createWorker();
        
        PB.progressProperty().unbind();
        
        PB.progressProperty().bind(_Recording.progressProperty());

        _Recording.messageProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        States.setText(newValue);
                    }
                });
        new Thread(_Recording).start();  	
        _Demo = new FileCreator("Demo");    
    }
    
    @FXML
    public void PlayAction() {
    	try {
    		String audioFile = NameManager.directory + "/se206" + _Demo.getTime() + _Demo.getName() + ".wav";
            Media media = new Media(new File(audioFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void ExitAction() throws IOException {
    	Stage primaryStage =(Stage) ExitButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("selectionMenu.fxml"));
    	 primaryStage.setTitle("Hello World");
    	 primaryStage.setScene(new Scene(root, 600,600));
    	 primaryStage.show();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 disableButtons(true);	
	}
    
	 private void disableButtons(boolean b) {
		 PlayButton.setDisable(b);
	}

	public Task<?> createWorker() {
	        return new Task<Object>() {
	            @Override
	            protected Object call() throws Exception {
	                for (int i = 0; i < 5; i++) {
	                    Thread.sleep(1000);
	                    updateMessage("Recording Completed : " + ((i*20)+20)  + "%");
	                    updateProgress(i + 1, 5);
	                }
	                RecordingIsFinished(); 
	                return true;
	            }
	        };  	    
	 }

	public void RecordingIsFinished() {
		PlayButton.setDisable(false);
	}
}