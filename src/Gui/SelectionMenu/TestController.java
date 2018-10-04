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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public class TestController implements Initializable {


    @FXML
    private Label States;

    @FXML
    private ProgressBar PB;


    Task<?> _Recording;
    private Thread thread;


    /**
     * when pressed Start button, a demo.wav generate automatically.
     * Using a progress bar to count 5 seconds, which will make the user know
     * how the recording is going on.
     *
     * @throws InterruptedException
     */
    @FXML
    public void buttonAction()  {
        try {
            PB.setProgress(0.0);
            _Recording = createWorker();
            PB.progressProperty().unbind();
            PB.progressProperty().bind(_Recording.progressProperty());
            _Recording.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    States.setText(newValue);
                }
            });
            thread = new Thread(_Recording);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void ExitAction() throws IOException {
        thread.stop();
        SceneManager.getInstance().removeScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonAction();
    }

    public Task<?> createWorker() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                try {
                    while (true) {
                        AudioFormat fmt = new AudioFormat(44100f, 16, 1, true, false);
                        final int bufferByteSize = 2048;

                        TargetDataLine line;
                        line = AudioSystem.getTargetDataLine(fmt);
                        line.open(fmt, bufferByteSize);

                        byte[] buf = new byte[bufferByteSize];
                        float[] samples = new float[bufferByteSize / 2];

                        float lastPeak = 0f;

                        line.start();
                        for (int b; (b = line.read(buf, 0, buf.length)) > -1; ) {

                            // convert bytes to samples here
                            for (int i = 0, s = 0; i < b; ) {
                                int sample = 0;

                                sample |= buf[i++] & 0xFF; // (reverse these two lines
                                sample |= buf[i++] << 8;   //  if the format is big endian)

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
                            updateProgress(rms * 10000, 1000);
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