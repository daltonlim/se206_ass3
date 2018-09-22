package Gui.SelectionMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Backend.File.BashWorker;
import Backend.File.FileCreator;
import Backend.NameManagement.NameManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class    RecordGuiController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label States;

    @FXML
    private Button PlayOldButton;

    @FXML
    private Button RecordButton;

    @FXML
    private Button PlayYoursButton;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Label name;

    @FXML
    private Button SaveButton;

    @FXML
    private Button NoSaveButton;
    
	private Task<?> _Recording;

	private FileCreator _FileCreator;
	
	private String _name;

	NameManager fileManager;

	private String _date;
	
	private BashWorker _worker;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 disableButtons(true);
		 progressbar.setVisible(false);
		 States.setVisible(false);
		 name.setText(_name);
		 fileManager = NameManager.getInstance();
	}
	
	 private void disableButtons(boolean b) {
		 PlayYoursButton.setDisable(b);
		 PlayOldButton.setDisable(b);
		 SaveButton.setDisable(b);
		 NoSaveButton.setDisable(b);
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
	
	public Task<?> oldversion() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 2; i++) {
                    Thread.sleep(1000);
                }
                RecordingIsFinished();
                return true;
            }
        };  	    
 }

	public Task<?> ownversion() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                }
                RecordingIsFinished();
                return true;
            }
        };  	    
 }
	
    @FXML
    public void record() throws InterruptedException, IOException {
    	RecordButton.setDisable(true);
    	disableButtons(true);
    	progressbar.setVisible(true);
    	States.setVisible(true);
    	

        _Recording = createWorker();

        progressbar.progressProperty().unbind();

        progressbar.progressProperty().bind(_Recording.progressProperty());

        _Recording.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                States.setText(newValue);
            }
        });
        new Thread(_Recording).start();
        _FileCreator = new FileCreator(_name);
    }

    private void RecordingIsFinished()  {
		 PlayYoursButton.setDisable(false);
		 PlayOldButton.setDisable(false);
		 SaveButton.setDisable(false);
		 NoSaveButton.setDisable(false);
		 RecordButton.setDisable(false);
    }

    @FXML
    public void playback() throws InterruptedException {
    	 RecordButton.setDisable(true);
    	 disableButtons(true);
    	 _Recording = ownversion();
    	 new Thread(_Recording).start();
         String audioFile = _FileCreator.fileString();
         _worker =  new BashWorker("ffplay -nodisp -autoexit "+ audioFile);
    }

    @FXML
    public void playold() throws InterruptedException { 
    	 RecordButton.setDisable(true);
    	 disableButtons(true);
   	     _Recording = oldversion();
   	     new Thread(_Recording).start();
         File file = fileManager.getFile(_name, _date);
   		 String location = file.toURI().toString();
   		 _worker =  new BashWorker("ffplay -nodisp -autoexit "+ location);
    }

	public void selected(String name,String date) {
		_name = name;	
		_date = date;
	}

	public void initData(String name, String date) {
		selected(name,date);
		this.name.setVisible(true);
		this.name.setText(_name);
	}
    
    @FXML
    public void save() {
        fileManager.addFile(_FileCreator.getFile());
        SceneManager.getInstance().removeScene();
    }

    @FXML
    public void nosave() {
    	_FileCreator.removeFile();
    	NameManager.getInstance().removeFile(_FileCreator.getFile());
    	SceneManager.getInstance().removeScene();
    }
}