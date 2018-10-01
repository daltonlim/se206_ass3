package Tests;

import Backend.File.TextFileParser;
import org.junit.Test;

import java.io.File;

public class TextFileParserTest {
    @Test
    public void TestSomething(){
        TextFileParser textFileParser= new TextFileParser(new File("/home/daltonl/GoogleDrive/GoogleDriveSync" +
                "/se206_ass3/myfile.txt"));
        System.out.println(textFileParser.getNamesToAdd());
    }
}
