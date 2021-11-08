package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Task1DWTests {

    private static DWInteractionGraph dwig;
    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;
    private static DWInteractionGraph dwig5;
    private static DWInteractionGraph dwig6;
    private static DWInteractionGraph dwig7;

    @BeforeAll
    public static void setupTests() {
        dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        dwig1 = new DWInteractionGraph(dwig, new int[]{3, 9});
        dwig2 = new DWInteractionGraph(dwig, Arrays.asList(2, 3, 4));
        dwig3 = new DWInteractionGraph("resources/more_than_single_space.txt");
        dwig4 = new DWInteractionGraph("resources/Blank.txt");
        dwig5 = new DWInteractionGraph("resources/email-Eu-core-temporal.txt");
        dwig6 = new DWInteractionGraph(dwig4, new int[]{3, 9});
        dwig7 = new DWInteractionGraph("resources/Blank.txt", new int[]{3, 9});
    }

    @Test
    public void test1GetUserIDsBase() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph1() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(0, 1, 4, 8));
        Assertions.assertEquals(expected, dwig1.getUserIDs());
    }

    @Test
    public void test1GetUserIDsGraph2() {
        Set<Integer> expected = new HashSet<>(Arrays.asList(2, 3, 4, 8));
        Assertions.assertEquals(expected, dwig2.getUserIDs());
    }

    @Test
    public void test1GetEmailCountBase() {
        Assertions.assertEquals(2, dwig.getEmailCount(2, 3));
        Assertions.assertEquals(0, dwig.getEmailCount(8, 4));
    }

    @Test
    public void test1GetEmailCountGraph1() {
        Assertions.assertEquals(1, dwig1.getEmailCount(1, 0));
        Assertions.assertEquals(1, dwig1.getEmailCount(8, 0));

    }

    @Test
    public void test1GetEmailCountGraph2() {
        Assertions.assertEquals(1, dwig2.getEmailCount(4, 8));
        Assertions.assertEquals(2, dwig2.getEmailCount(2, 3));
    }

    @Test
    public void testGetDataWithSpaces() {
        Assertions.assertEquals(2, dwig3.getEmailCount(1, 3));

    }

    @Test
    public void testBlankDocument() {
        Assertions.assertEquals(0, dwig4.getEmailCount(0, 0));
        Assertions.assertEquals(0, dwig4.getEmailCount(1, 2));
        Assertions.assertEquals(0, dwig6.getEmailCount(1, 2));
        Assertions.assertEquals(0, dwig7.getEmailCount(1, 2));
    }

    @Test
    public void testGetIdsBlankDocument() {
        Set<Integer> expected = new HashSet<>();
        Assertions.assertEquals(expected, dwig7.getUserIDs());
    }

    @Test
    public void testLargeDocument(){
        System.out.println(dwig4.getEmailCount(416, 356));
    }
}
