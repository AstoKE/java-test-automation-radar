package edu.tedu.radar;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Detection_Test {

    // (Assertion 7-12): Checks valid construction and all getters
    @Test
    void detectionInitializationAndGetters() {
        Detection d = new Detection("D1", 15.0, 90.0, 3.5, ThreatLevel.HIGH);

        // Assertion 7: Check Target ID
        assertEquals("D1", d.getTargetId());
        // Assertion 8: Check Distance
        assertEquals(15.0, d.getDistanceKm());
        // Assertion 9: Check Bearing
        assertEquals(90.0, d.getBearingDeg());
        // Assertion 10: Check SNR
        assertEquals(3.5, d.getSnr());
        // Assertion 11: Check Threat Level
        assertEquals(ThreatLevel.HIGH, d.getThreat());
        // Assertion 12: Check object is not null
        assertNotNull(d);
    }

    // (Assertion 13-15): Tests the isHighConfidence() logic boundary for all threat levels
    @Test
    void highConfidenceLogic() {
        Detection lowThreat = new Detection("L", 1, 1, 1, ThreatLevel.LOW);
        Detection mediumThreat = new Detection("M", 1, 1, 2, ThreatLevel.MEDIUM);
        Detection highThreat = new Detection("H", 1, 1, 3, ThreatLevel.HIGH);

        // Assertion 13: LOW Threat should NOT be High Confidence
        assertFalse(lowThreat.isHighConfidence());
        // Assertion 14: MEDIUM Threat should NOT be High Confidence
        assertFalse(mediumThreat.isHighConfidence());
        // Assertion 15: HIGH Threat SHOULD be High Confidence
        assertTrue(highThreat.isHighConfidence());
    }
}