package Tests;

import Backend.NameManagement.Name;
import Backend.NameManagement.NameManager;
import org.junit.Test;

public class TestNameManager {
    @Test
    public void test1(){
        NameManager.getInstance();
        System.out.println(NameManager.getInstance().retrievePrefix("balmin    "));
        System.out.println(NameManager.getInstance().retrievePrefix(""));
    }
}
