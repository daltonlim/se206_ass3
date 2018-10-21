package backend.file;

import javax.swing.SwingWorker;
import java.io.File;

/**
 * Allows for the easy running of bash commands on a thread different to the EDT
 */
public class BashWorker extends SwingWorker<Void, Void> {
    String command = null;
    Process process;

    public BashWorker(String command) {
        this.command = command;
        this.execute();
    }

	@Override
    protected Void doInBackground() {
        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
            process = builder.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public void kill() {
        process.destroy();
    }
}
