package edu.tedu.radar;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Target_Test {

    // (Assertion 1-2): Incorporates 'ctorGuards' from SmokeTest.java
    @Test
    void constructorRcsValidation() {
        // Assertion 1: Negative RCS throws exception
        assertThrows(IllegalArgumentException.class, () -> new Target("T_Neg", 0, 0, -1));
        // Assertion 2: Zero RCS is allowed (Negative boundary check)
        assertDoesNotThrow(() -> new Target("T_Zero", 10, 10, 0));
    }

    // (Assertion 3-6): Checks valid construction and getters (Positive Test)
    @Test
    void targetInitializationAndGetters() {
        String expectedId = "T1";
        double expectedX = 5.0;
        double expectedY = -3.0;
        double expectedRcs = 10.5;

        Target target = new Target(expectedId, expectedX, expectedY, expectedRcs);

        // Assertion 3: Check ID
        assertEquals(expectedId, target.getId());
        // Assertion 4: Check X-coordinate
        assertEquals(expectedX, target.getX());
        // Assertion 5: Check Y-coordinate
        assertEquals(expectedY, target.getY());
        // Assertion 6: Check RCS
        assertEquals(expectedRcs, target.getRcs());
    }
}
