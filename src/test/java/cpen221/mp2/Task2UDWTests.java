package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Task2UDWTests {

    private static UDWInteractionGraph testGraphBase;
    private static UDWInteractionGraph blank;
    private static UDWInteractionGraph selfInteractions;
    private static UDWInteractionGraph multipleSelfInteractions;
    private static UDWInteractionGraph homebrew1;
    private static UDWInteractionGraph big;

    @BeforeAll
    public static void setupTests() {
        testGraphBase = new UDWInteractionGraph("resources/Task1-2UDWTransactions.txt");
        blank = new UDWInteractionGraph("resources/Blank.txt");
        selfInteractions = new UDWInteractionGraph("resources/Self_single.txt");
        multipleSelfInteractions = new UDWInteractionGraph("resources/Self_multiple.txt");
        homebrew1 = new UDWInteractionGraph("resources/Task1-Homebrew1.txt");
    }

    @Test
    public void testReportActivityInTimeWindow() {
        int[] result = testGraphBase.ReportActivityInTimeWindow(new int[]{0, 1});
        Assertions.assertEquals(3, result[0]);
        Assertions.assertEquals(2, result[1]);
    }

    @Test
    public void testReportOnUser() {
        int[] result = testGraphBase.ReportOnUser(0);
        Assertions.assertEquals(6, result[0]);
        Assertions.assertEquals(3, result[1]);
    }


    @Test
    public void testReportOnUser1() {
        List<Integer> userFilter = Arrays.asList(0, 1);
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, userFilter);
        int[] result = t.ReportOnUser(0);
        Assertions.assertEquals(6, result[0]);
        Assertions.assertEquals(3, result[1]);
    }

    @Test
    public void testReportOnUser2() {
        int[] result = testGraphBase.ReportOnUser(4);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[1]);
    }

    @Test
    public void testNthActiveUser() {
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, new int[]{0, 2});
        Assertions.assertEquals(0, t.NthMostActiveUser(1));
    }

    @Test
    public void testNthActiveUser1() {
        UDWInteractionGraph t = new UDWInteractionGraph(testGraphBase, new int[]{0, 2});
        Assertions.assertEquals(1, t.NthMostActiveUser(2));
    }

    @Test
    public void blankTest1() {
        int[] result;
        for (int i = -20; i < 20; i += 7) {
            for (int j = -3; j < 4; j++) {
                result = blank.ReportActivityInTimeWindow(new int[]{i, j});
                Assertions.assertEquals(0, result[0]);
                Assertions.assertEquals(0, result[1]);

                result = blank.ReportOnUser(i + j);
                Assertions.assertEquals(0, result[0]);
                Assertions.assertEquals(0, result[1]);

                Assertions.assertEquals(-1, blank.NthMostActiveUser(j + 4));
            }
        }
    }

    @Test
    public void testSelfInteractions() {
        int[] result = selfInteractions.ReportOnUser(0);
        Assertions.assertEquals(11, result[0]);
        Assertions.assertEquals(1, result[1]);
    }

    @Test
    public void testSelfInteractions2() {
        int[] result = multipleSelfInteractions.ReportOnUser(0);
        Assertions.assertEquals(4, result[0]);
        Assertions.assertEquals(1, result[1]);

        int[] result1 = multipleSelfInteractions.ReportOnUser(1);
        Assertions.assertEquals(3, result1[0]);
        Assertions.assertEquals(1, result1[1]);
    }

    @Test
    public void testSelfInteractions3() {
        int[] result = multipleSelfInteractions.ReportActivityInTimeWindow(new int[]{0, 20});
        Assertions.assertEquals(6, result[0]);
        Assertions.assertEquals(12, result[1]);

        int[] result1 = multipleSelfInteractions.ReportActivityInTimeWindow(new int[]{-12, 1});
        Assertions.assertEquals(3, result1[0]);
        Assertions.assertEquals(4, result1[1]);
    }

    @Test
    public void testHomebrew() {
        int[] result = homebrew1.ReportActivityInTimeWindow(new int[]{-10, 10});
        Assertions.assertEquals(7, result[0]);
        Assertions.assertEquals(5, result[1]);

        int[] result1 = homebrew1.ReportOnUser(1);
        Assertions.assertEquals(1, result1[0]);
        Assertions.assertEquals(1, result1[1]);

        int[] result8 = homebrew1.ReportOnUser(8);
        Assertions.assertEquals(0, result8[0]);
        Assertions.assertEquals(0, result8[1]);

        int[] result0 = homebrew1.ReportOnUser(0);
        Assertions.assertEquals(4, result0[0]);
        Assertions.assertEquals(4, result0[1]);

        int[] result10 = homebrew1.ReportOnUser(10);
        Assertions.assertEquals(4, result10[0]);
        Assertions.assertEquals(3, result10[1]);
    }

    @Test
    public void testActiveUsers() {
        UDWInteractionGraph t = new UDWInteractionGraph(homebrew1, new int[]{0, 2});
        Assertions.assertEquals(1, t.NthMostActiveUser(2));
    }

    @Test
    public void testActiveUsers2() {
        UDWInteractionGraph t = new UDWInteractionGraph(homebrew1, new int[]{-10, 12});
        Assertions.assertEquals(-1, t.NthMostActiveUser(0));
        Assertions.assertEquals(0, t.NthMostActiveUser(1));
    }

    @Test
    public void testActiveUsers3() {
        UDWInteractionGraph t = new UDWInteractionGraph(homebrew1, new int[]{-10, 12});
        Assertions.assertEquals(3, t.NthMostActiveUser(2));
        Assertions.assertEquals(10, t.NthMostActiveUser(3));
    }

    @Test
    public void testActiveUsers4() {
        UDWInteractionGraph t = new UDWInteractionGraph(homebrew1, new int[]{-10, 12});
        Assertions.assertEquals(12, t.NthMostActiveUser(8));
        Assertions.assertEquals(-1, t.NthMostActiveUser(12));
    }

    @Test
    public void testActiveUsers5() {
        UDWInteractionGraph t = new UDWInteractionGraph("resources/active-user-test.txt");
        Assertions.assertEquals(8, t.NthMostActiveUser(1));
        Assertions.assertEquals(12, t.NthMostActiveUser(2));
        Assertions.assertEquals(0, t.NthMostActiveUser(3));
        Assertions.assertEquals(2, t.NthMostActiveUser(4));
        Assertions.assertEquals(10, t.NthMostActiveUser(5));
        Assertions.assertEquals(4, t.NthMostActiveUser(6));
        Assertions.assertEquals(6, t.NthMostActiveUser(7));

        for (int i = 0; i > -3; i--) {
            Assertions.assertEquals(-1, t.NthMostActiveUser(i));
        }

        for (int i = 8; i < 11; i++) {
            Assertions.assertEquals(-1, t.NthMostActiveUser(i));
        }
    }
}
