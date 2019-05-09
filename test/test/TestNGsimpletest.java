package test;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/**
 * TestNGsimpletest
 */
public class TestNGsimpletest {

    @Test
    public void testAdd() {
        String str = "TestNG is working fine";
        assertEquals("TestNG is working fine", str);
    }
}