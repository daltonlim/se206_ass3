package Backend.File;

import javax.swing.*;

/**
 * Allows for the easy running of bash commands on a thread different to the EDT
 */
class BashWorker extends SwingWorker<Void, Void> {
    String _command;

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
        }
        return null;

    }
}
