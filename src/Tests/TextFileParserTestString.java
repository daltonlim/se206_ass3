package Tests;

import Backend.File.TextFileParser;
import org.junit.Test;

import java.io.File;

public class TextFileParserTestString {
    @Test
    public void TestSomething(){
        TextFileParser textFileParser= new TextFileParser("li");
        System.out.println(textFileParser.getNamesToAdd());
        System.out.println(textFileParser.getNotPossibleNames());
        System.out.println(textFileParser.getPartialNames());
    }
}
