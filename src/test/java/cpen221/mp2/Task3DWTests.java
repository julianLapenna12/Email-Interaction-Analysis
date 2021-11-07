package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task3DWTests {

    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;

    @BeforeAll
    public static void setupTests() {
        dwig1 = new DWInteractionGraph("resources/Task3Transactions1.txt");
        dwig2 = new DWInteractionGraph("resources/Task3Transactions2.txt");
        dwig3 = new DWInteractionGraph("resources/Task_3_Path_Test.txt");
        dwig4 = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
    }

    @Test
    public void testBFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, dwig1.BFS(1, 6));
    }

    @Test
    public void testDFSGraph1() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6);
        Assertions.assertEquals(expected, dwig1.DFS(1, 6));
    }

    @Test
    public void testBFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 5, 6, 4, 8, 7, 2, 9, 10);
        Assertions.assertEquals(expected, dwig2.BFS(1, 10));
    }

    @Test
    public void testDFSGraph2() {
        List<Integer> expected = Arrays.asList(1, 3, 4, 8, 5, 7, 2, 9, 10);
        Assertions.assertEquals(expected, dwig2.DFS(1, 10));
    }

    @Test
    public void testDFSGraph3() {
        List<Integer> expected = Arrays.asList(6, 3, 9, 300, 12, 0);
        Assertions.assertEquals(expected, dwig3.DFS(6, 0));
    }
    @Test
    public void testLargeGraph(){
        Assertions.assertTrue(dwig4.DFS(415, 356).contains(356));
        Assertions.assertTrue(dwig4.BFS(415, 356).contains(356));
    }
    @Test
    public void testSelf(){
        Assertions.assertEquals(Arrays.asList(415),dwig4.BFS(415, 415) );
        Assertions.assertEquals(Arrays.asList(415),dwig4.DFS(415, 415) );
    }
    @Test
    public void testNullPathDFS(){
        Assertions.assertNull( dwig1.DFS(1, 10));
    }

    @Test
    public void testNullPathBFS(){
        Assertions.assertNull( dwig1.BFS(1, 10));
    }
}
