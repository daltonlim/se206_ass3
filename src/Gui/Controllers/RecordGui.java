package Gui.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

import Backend.File.BashWorker;
import Backend.File.FileCreator;
import Backend.File.FileNameParser;
import Backend.NameManagement.NameManager;
import Backend.achievements.AchievementManager;
import Gui.SceneManager;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecordGui implements Initializable {
	@FXML
	private Label States;
	@FXML
	private Button PlayOldButton;
	@FXML
	private Button recordButton;
	@FXML
	private Button ExitButton;
	@FXML
	private Button PlayYoursButton;
	@FXML
	private ProgressBar progressbar;
	@FXML
	private VBox vbox;
	@FXML
	private Button microphoneButton;
	@FXML
	private ProgressBar PB;
	@FXML
	private Button loopButton;

	private BashWorker worker;
	private NameManager fileManager;
	private Task<?> _Recording;
	private FileCreator fileCreator;
	private FileNameParser fileParser;
	private BashWorker bashWorker;
	private Boolean _RecordingIsFinished = false;
	private Boolean isSingleWord;
	private String[] nameArray;
	private String _name;
	private Task<?> playCreatedName;
	private Thread thread;
	Task<?> recording;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		disableButtons(true);
		fileManager = NameManager.getInstance();
		States.setText("Hold to record");
		PB.setVisible(false);
	}

	private void disableButtons(boolean b) {
		PlayYoursButton.setDisable(b);
	}

	/**
	 * A task shows how is recording
	 */
	public Task<?> createWorker() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 100; i++) {
					Thread.sleep(100);
					updateMessage("Recording Completed : " + (i + 1) + "%");
					updateProgress(i + 1, 100);
				}
				RecordingIsFinished();
				return true;
			}
		};
	}

	/**
	 * record audio when record button is pressed
	 */
	@FXML
	public void record() {
		stop();
		try {
			States.setVisible(true);
			progressbar.setProgress(0.0);
			PlayOldButton.setDisable(true);
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

	/**
	 * reset button when recording finished
	 */
	private void RecordingIsFinished() {
		disableButtons(false);
		_RecordingIsFinished = true;
	}

	/**
	 * play your own version
	 */
	private void play(File audioFile) {

		AchievementManager.getInstance().incrementAchievement("Play");
		stop();
		String location = audioFile.toURI().toString();
		location = location.replace("%20", " ");
		bashWorker = new BashWorker("ffplay -af \"volume=10dB\" -nodisp -autoexit '" + location + "'");
	}

	/**
	 * play your own version
	 */
	@FXML
	public void playNew() throws InterruptedException {
		try {
			File audioFile = new File(fileCreator.fileString());
			play(audioFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * play old version
	 */
	@FXML
	public void playOld() throws InterruptedException {
		stop();
		if (isSingleWord) {
			try {
				States.setVisible(false);
				File audioFile = fileParser.getFile();
				play(audioFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			playCreatedName = play();
			new Thread(playCreatedName).start();
		}
	}

	public Task<?> play() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < nameArray.length; i++) {
					try {
						File file = fileManager.getRandomGoodFile(nameArray[i]);
						String location = file.toURI().toString();
						worker = new BashWorker("ffplay -af \"volume=10dB\" -nodisp -autoexit " + location);
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
						AudioFormat format = audioInputStream.getFormat();
						long frames = audioInputStream.getFrameLength();
						double durationInSeconds = (frames + 0.0) / format.getFrameRate();
						int time = (int) (durationInSeconds * 1000);
						Thread.sleep(time);
					} catch (Exception e) {
						break;
					}
				}
				return true;
			}
		};
	}

	@FXML
	public void playLoop() {
		stop();
		if (isSingleWord) {
			try {
				List<String> loop = fileManager.getFileDatesForName(PlayOldButton.getText());
				for (int i = 0; i < loop.size(); i++) {
					String date = loop.get(i);
					File file = fileManager.getFile(PlayOldButton.getText(), date);
					String location = file.toURI().toString();					
					worker = new BashWorker("ffplay -nodisp -autoexit '" + location + "'");
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					AudioFormat format = audioInputStream.getFormat();
					long frames = audioInputStream.getFrameLength();
					double durationInSeconds = (frames + 0.0) / format.getFrameRate();
					int time = (int) (durationInSeconds * 1000);
					Thread.sleep(time);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

		}
	}

	/**
	 * initialize data when name is single
	 */
	public void initData(File file) {
		fileParser = new FileNameParser(file);
		String name = fileParser.getUserName();
		fileCreator = new FileCreator(name);
		isSingleWord = true;
		PlayOldButton.setText(name);
	}

	/**
	 * refresh page
	 */
	public void reload() throws IOException {
		stop();
		fileCreator.removeFile();
		Stage primaryStage = (Stage) recordButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordGui.fxml"));
		Parent root = loader.load();

		RecordGui controller = loader.<RecordGui>getController();
		if (isSingleWord) {
			controller.initData(fileParser.getFile());
		} else {
			controller.initDataX(_name);
		}

		primaryStage.setScene(new Scene(root, 600, 600));
		primaryStage.show();
	}

	/**
	 * back to previous scene and save/delete your recording
	 */
	@FXML
	public void exit() throws IOException {
		stop();
		if (_RecordingIsFinished) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("RecordGui");
			alert.setHeaderText(null);
			alert.setContentText("Do you want save this recording?");
			alert.getDialogPane().getStylesheets()
					.add(getClass().getResource("/resources/FlatBee.css").toExternalForm());
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				NameManager.getInstance().addFile(fileCreator.getFile());
				AchievementManager.getInstance().incrementAchievement("Recording");
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
			alert.setTitle("RecordGui");
			alert.setHeaderText(null);
			alert.setContentText("Do you want leave this page?");
			alert.getDialogPane().getStylesheets()
					.add(getClass().getResource("/resources/FlatBee.css").toExternalForm());
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				stop();
				SceneManager.getInstance().removeScene();
			} else {
				reload();
			}
		}
	}

	/**
	 * recording is finished when you realese the record button
	 */
	@FXML
	public void stp() {
		PlayOldButton.setDisable(false);
		fileCreator.kill();
		RecordingIsFinished();
		_Recording.cancel(true);
		States.setText("Stop the recording");
		progressbar.progressProperty().unbind();
		progressbar.progressProperty().setValue(100);
	}

	/**
	 * kill process
	 */
	private void stop() {
		if (bashWorker != null) {
			bashWorker.kill();
		}
		if (playCreatedName != null) {
			playCreatedName.cancel(true);
		}
	}

	/**
	 * initialize data when its combinational name
	 */
	public void initDataX(String name) {
		fileCreator = new FileCreator(name);
		_name = name;
		isSingleWord = false;
		nameArray = name.split("[ -]");
		PlayOldButton.setText(">>" + name + "<<");
	}

	@SuppressWarnings("deprecation")
	@FXML
	private void microphoneTest() throws IOException {
		if (PB.isVisible()) {
			PB.setVisible(false);
			thread.stop();
		} else {
			PB.setVisible(true);
			try {
				PB.setProgress(0.0);
				recording = MicroWorker();
				PB.progressProperty().unbind();
				PB.progressProperty().bind(recording.progressProperty());
				recording.messageProperty().addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						States.setText(newValue);
					}
				});
				thread = new Thread(recording);
				thread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setProgressbar(){

	}
	public Task<?> MicroWorker() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					while (true) {
						// Code taken from
						// https://stackoverflow.com/questions/26574326/how-to-calculate-the-level-amplitude-db-of-audio-signal-in-java
						AudioFormat fmt = new AudioFormat(44100f, 16, 1, true, false);
						final int bufferByteSize = 2048;

						TargetDataLine line;
						line = AudioSystem.getTargetDataLine(fmt);
						line.open(fmt, bufferByteSize);

						byte[] buf = new byte[bufferByteSize];
						float[] samples = new float[bufferByteSize / 2];

						float lastPeak = 0f;

						line.start();
						for (int b; (b = line.read(buf, 0, buf.length)) > -1;) {

							// convert bytes to samples here
							for (int i = 0, s = 0; i < b;) {
								int sample = 0;

								sample |= buf[i++] & 0xFF; // (reverse these two lines
								sample |= buf[i++] << 8; // if the format is big endian)

								// normalize to range of +/-1.0f
								samples[s++] = sample / 32768f;
							}

							float rms = 0f;
							float peak = 0f;
							for (float sample : samples) {

								float abs = Math.abs(sample);
								if (abs > peak) {
									peak = abs;
								}

								rms += sample * sample;
							}

							rms = (float) Math.sqrt(rms / samples.length);

							if (lastPeak > peak) {
								peak = lastPeak * 0.875f;
							}

							lastPeak = peak;
							updateProgress(rms * 10000, 100);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		};
	}
}