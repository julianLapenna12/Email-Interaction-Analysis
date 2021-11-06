package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class Task3UDWTests {

    private static UDWInteractionGraph homebrew1;

    @BeforeAll
    public static void setupTests() {
        homebrew1 = new UDWInteractionGraph("resources/Task1-Homebrew1.txt");
    }

    @Test
    public void testNumComponent() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test.txt");
        Assertions.assertEquals(1, t.NumberOfComponents());
    }

    @Test
    public void testNumComponent1() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test1.txt");
        Assertions.assertEquals(2, t.NumberOfComponents());
    }

    @Test
    public void testPathExists() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test.txt");
        Assertions.assertTrue(t.PathExists(1, 2));
        Assertions.assertTrue(t.PathExists(1, 3));
        Assertions.assertTrue(t.PathExists(1, 4));
        Assertions.assertTrue(t.PathExists(2, 3));
        Assertions.assertTrue(t.PathExists(2, 4));
        Assertions.assertTrue(t.PathExists(3, 4));
    }

    @Test
    public void testPathExists1() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test1.txt");
        Assertions.assertTrue(t.PathExists(1, 2));
        Assertions.assertTrue(t.PathExists(3, 4));
        Assertions.assertFalse(t.PathExists(1, 4));
        Assertions.assertFalse(t.PathExists(2, 3));
    }

    @Test
    public void testSingleUser() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test2.txt");
        Assertions.assertEquals(2, t.getEmailCount(1, 1));
        Assertions.assertEquals(1, t.NumberOfComponents());
        Assertions.assertTrue(t.PathExists(1, 1));
    }

    @Test
    public void testNoUser() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Task3-components-test2.txt");
        UDWInteractionGraph t1 = new UDWInteractionGraph(t, List.of(2));
        Assertions.assertEquals(0, t1.NumberOfComponents());
    }

    @Test
    public void testBlank() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/Blank.txt");
        Assertions.assertEquals(0, t.NumberOfComponents());

        for (int i = -1000000; i < 1245238; i += 2346) {
            for (int j = -2135224; j < 1234624; j += 13678) {
                Assertions.assertFalse(t.PathExists(i, j));
            }
        }
    }

    @Test
    public void testGraphs() {
        Assertions.assertEquals(homebrew1.NumberOfComponents(), 2);
        Assertions.assertTrue(homebrew1.PathExists(99, 1));
        Assertions.assertTrue(homebrew1.PathExists(12, 4));
        Assertions.assertTrue(homebrew1.PathExists(0, 3));
        Assertions.assertFalse(homebrew1.PathExists(12, -11));
        Assertions.assertTrue(homebrew1.PathExists(6, 9));
        Assertions.assertFalse(homebrew1.PathExists(9, 1));
        Assertions.assertFalse(homebrew1.PathExists(-11, 12));
        Assertions.assertFalse(homebrew1.PathExists(3, 6));
        Assertions.assertFalse(homebrew1.PathExists(3, 2));
        Assertions.assertFalse(homebrew1.PathExists(10, 2));
    }

    @Test
    public void testBeegGraph() {
        UDWInteractionGraph big = new UDWInteractionGraph("resources/email-Eu-core-temporal-Dept1.txt");
        System.out.println(big.NumberOfComponents());
    }
}
