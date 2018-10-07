package Tests;

import Backend.NameManagement.NameManager;
import org.junit.Test;

import javax.swing.plaf.SpinnerUI;

public class TestNameClass {
    @Test
    public void testRandomName() {
        for (int i = 0; i < 10; i++) {
            System.out.println(NameManager.getInstance().getRandomGoodFile("Li"));
        }
    }
    @Test
    public void testUnknown(){
        System.out.println(NameManager.getInstance().getFileDatesForName("Bruce Chen"));
    }
}
