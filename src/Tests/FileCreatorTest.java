package Tests;

import Backend.File.FileCreator;
import org.junit.Test;

public class FileCreatorTest {
    @Test
    public void testFileCreator() throws InterruptedException {
        FileCreator fileCreator = new FileCreator("FILECREATORTEST");
        Thread.sleep(6000);
    }
}
