package cpen221.mp2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class Task4DWTests {

    private static DWInteractionGraph dwig1;
    private static DWInteractionGraph dwig2;
    private static DWInteractionGraph dwig3;
    private static DWInteractionGraph dwig4;
    private static DWInteractionGraph dwig5;
    private static DWInteractionGraph dwig6;

    @BeforeAll
    public static void setupTests() {
        dwig1 = new DWInteractionGraph("resources/Task4Transactions1.txt");
        dwig2 = new DWInteractionGraph("resources/Task4Transactions2.txt");
        dwig3 = new DWInteractionGraph("resources/Task4Transactions3.txt");
        dwig4 = new DWInteractionGraph("resources/Task1-empty-file.txt");
        dwig5 = new DWInteractionGraph("resources/Task4Transactions4.txt");
        dwig6 = new DWInteractionGraph("resources/Task4Transactions5.txt");
    }

    @Test
    public void testMaxedBreachedUserCount1() {
        assertTimeoutPreemptively(Duration.ofSeconds(75), () -> {
            // Attacking user 7 any time in the window [0,120] will pollute 8 users in a 2 hour window.
            Assertions.assertEquals(8, dwig1.MaxBreachedUserCount(2));
        });
    }

    @Test
    public void testMaxedBreachedUserCount2() {
        assertTimeoutPreemptively(Duration.ofSeconds(75), () -> {
            // Attacking user 3 at t=0, or attacking user 5 any time in the window [0,60] will pollute
            // 10 users in a 4 hour window.
            Assertions.assertEquals(10, dwig2.MaxBreachedUserCount(4));
        });
    }

    @Test
    public void testMaxedBreachedUserCount3() {
        assertTimeoutPreemptively(Duration.ofSeconds(75), () -> {
            // Attacking user 4 at t=3600 will lead to users 4, 5, 6, 3, and 1 (5 users) to be polluted
            // in a 6-hour-long window after the attack starts.
            Assertions.assertEquals(5, dwig3.MaxBreachedUserCount(6));
        });
    }

    @Test
    public void testMaxedBreachedUserCount4() {
        assertTimeoutPreemptively(Duration.ofSeconds(75), () -> {
            // an empty graph leads to 0 breaches
            Assertions.assertEquals(0, dwig4.MaxBreachedUserCount(4000));
        });
    }

    @Test
    public void testMaxedBreachedUserCount5() {
        assertTimeoutPreemptively(Duration.ofSeconds(150), () -> {
            Assertions.assertEquals(1, dwig5.MaxBreachedUserCount(0));
            Assertions.assertEquals(3, dwig5.MaxBreachedUserCount(1));
            Assertions.assertEquals(4, dwig5.MaxBreachedUserCount(2));
            Assertions.assertEquals(1001, dwig5.MaxBreachedUserCount(1501));
        });
    }

    @Test
    public void testMaxedBreachedUserCount6() {
        assertTimeoutPreemptively(Duration.ofSeconds(150), () -> {
            Assertions.assertEquals(1, dwig6.MaxBreachedUserCount(0));
            Assertions.assertEquals(3601, dwig6.MaxBreachedUserCount(1));
            Assertions.assertEquals(7199, dwig6.MaxBreachedUserCount(2));
        });
    }
}
