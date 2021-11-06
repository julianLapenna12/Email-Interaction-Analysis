package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Task1UDWTests {
    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph testGraph1;
    private static UDWInteractionGraph testGraph2;
    private static UDWInteractionGraph blank;
    private static UDWInteractionGraph selfInteractions;
    private static UDWInteractionGraph multipleSelfInteractions;
    private static UDWInteractionGraph negativeIndexes;
    private static UDWInteractionGraph negativeIndexes2;

    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        testGraph1 = new UDWInteractionGraph(testGraphBase, new int[]{0, 9});
        testGraph2 = new UDWInteractionGraph(testGraphBase, new int[]{10, 11});
        blank = new UDWInteractionGraph("resources/Blank.txt");
        selfInteractions = new UDWInteractionGraph("resources/Self_single.txt");
        multipleSelfInteractions = new UDWInteractionGraph("resources/Self_multiple.txt");
        negativeIndexes = new UDWInteractionGraph("resources/Self_single_with_negatives.txt");
        negativeIndexes2 = new UDWInteractionGraph("resources/Self_multiple_with_negatives.txt");
    }

    @Test
    public void testGetUserIds() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), testGraphBase.getUserIDs());
    }

    @Test
    public void testGetUserIds1() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), testGraph2.getUserIDs());
    }

    @Test
    public void testGetEmailCount() {
        Assertions.assertEquals(2, testGraphBase.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraphBase.getEmailCount(0, 1));
    }

    @Test
    public void testGetEmailCount1() {
        Assertions.assertEquals(2, testGraph1.getEmailCount(1, 0));
        Assertions.assertEquals(2, testGraph1.getEmailCount(0, 3));
    }

    @Test
    public void testGetEmailCount2() {
        Assertions.assertEquals(0, testGraph2.getEmailCount(1, 0));
        Assertions.assertEquals(1, testGraph2.getEmailCount(1, 3));
    }

    @Test
    public void testUserConstructor() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), t.getUserIDs());
        Assertions.assertEquals(2, t.getEmailCount(0, 1));
        Assertions.assertEquals(2, t.getEmailCount(0, 3));
    }

    @Test
    public void testConstructionFromDW() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructionFromDW1() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Task1-2Transactions.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 8)), udwig.getUserIDs());
        Assertions.assertEquals(2, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructorBlankVariant() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList()), blank.getUserIDs());
        Assertions.assertEquals(0, blank.getEmailCount(0, 1));
        Assertions.assertEquals(0, blank.getEmailCount(2, 3));
    }

    @Test
    public void testConstructorBlankVariant2() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Blank.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);

        Assertions.assertEquals(new HashSet<>(Arrays.asList()), udwig.getUserIDs());
        Assertions.assertEquals(0, udwig.getEmailCount(0, 1));
        Assertions.assertEquals(0, udwig.getEmailCount(2, 3));
    }

    @Test
    public void testConstructorBlankVariant3() {
        DWInteractionGraph dwig = new DWInteractionGraph("resources/Blank.txt");
        UDWInteractionGraph udwig = new UDWInteractionGraph(dwig);

        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(udwig, userFilter);

        Assertions.assertEquals(new HashSet<>(Arrays.asList()), udwig.getUserIDs());
        Assertions.assertEquals(0, t.getEmailCount(5, 11));
        Assertions.assertEquals(0, t.getEmailCount(42, 32));
    }

    @Test
    public void testConstructorBlankVariant4() {
        UDWInteractionGraph udwig = new UDWInteractionGraph(blank, new int[]{5, 9});

        List<Integer> userFilter = Arrays.asList(4, 6);
        UDWInteractionGraph t = new UDWInteractionGraph(udwig, userFilter);

        Assertions.assertEquals(new HashSet<>(Arrays.asList()), udwig.getUserIDs());
        Assertions.assertEquals(0, t.getEmailCount(5, 11));
        Assertions.assertEquals(0, t.getEmailCount(42, 32));
    }

    @Test
    public void testConstructorWithSelfInteractions() {
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0)), selfInteractions.getUserIDs());
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1)), negativeIndexes.getUserIDs());
        Assertions.assertEquals(new HashSet<>(Arrays.asList(-67, -11, -2, 0, 1, 3, 11)), negativeIndexes2.getUserIDs());
        Assertions.assertEquals(new HashSet<>(Arrays.asList(0, 1, 4, 5, 12, 322, 560)), multipleSelfInteractions.getUserIDs());
    }

}
