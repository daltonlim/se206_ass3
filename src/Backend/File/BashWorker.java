package Backend.File;

import javax.swing.SwingWorker;

/**
 * Allows for the easy running of bash commands on a thread different to the EDT
 */
public class BashWorker extends SwingWorker<Void, Void> {
    String _command = null;
    
    public BashWorker(String command) {
        _command = command;
        this.execute();
    }

    @Override
    protected Void doInBackground() {
        try {
            ProcessBuilder builder = new ProcessBuilder("bash", "-c", _command);
            Process process = builder.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
