package edu.tedu.radar;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SmokeTest {
    @Test
    void ctorGuards() {
        assertThrows(IllegalArgumentException.class, () -> new Radar("R", 0, 0.1));
        assertThrows(IllegalArgumentException.class, () -> new Target("T", 0, 0, -1));
    }

    // There is 2 assertions

}
