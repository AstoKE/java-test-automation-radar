package edu.tedu.radar;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RadarScanTest {

    @Test
    void scanDetectsTargetsWithinRangeAndThreshold() {
        Radar radar = new Radar("R1", 10.0, 0.5);
        List<Target> targets = List.of(
                new Target("A", 3, 4, 5),    // distance = 5, snr = 5/6 ≈ 0.83 (passes)
                new Target("B", 12, 0, 10),  // out of range
                new Target("C", 1, 1, 0.3)   // snr = 0.3 / 2 = 0.15 (too low)
        );
        List<Detection> detections = radar.scan(targets);

        assertEquals(1, detections.size());
        Detection d = detections.get(0);

        assertAll(
                () -> assertEquals("A", d.getTargetId()),
                () -> assertTrue(d.getSnr() >= 0.5),
                () -> assertEquals(5.0, d.getDistanceKm(), 1e-9),
                () -> assertEquals(53.130102, d.getBearingDeg(), 1e-5)
        );
        assertNotEquals(ThreatLevel.LOW, d.getThreat());
    }
    // 6 assertions

    @Test
    void constructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Radar("R", 0, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new Target("T", 0, 0, -1));
    }
    // 2 assertions

    @Test
    void lambdaFilterTest() {
        Radar radar = new Radar("R2", 20.0, 0.5);
        List<Detection> detections = radar.scan(List.of(
                new Target("A", 2, 0, 5),
                new Target("B", 8, 0, 1)
        ));

        // lambda filter (extra point)
        List<Detection> filtered = detections.stream()
                .filter(d -> d.getThreat() == ThreatLevel.HIGH && d.getBearingDeg() <= 90)
                .toList();

        assertTrue(filtered.stream().allMatch(Detection::isHighConfidence) || filtered.isEmpty());
    }
    // 1 assertion

    @Test
    void listOrderIsSortedByTargetId() {
        Radar radar = new Radar("R3", 50.0, 0.1);
        List<Target> targets = List.of(
                new Target("C", 0, 5, 2),
                new Target("A", 1, 0, 2),
                new Target("B", 0, 1, 2)
        );

        List<Detection> detections = radar.scan(targets);
        List<String> ids = detections.stream().map(Detection::getTargetId).toList();

        assertIterableEquals(List.of("A", "B", "C"), ids);
    }
    // 1 assertion

    @Test
    void highThreatImpliesHighConfidence() {
        Radar radar = new Radar("R2", 20.0, 0.5);
        var detections = radar.scan(List.of(
                new Target("A", 2, 0, 5),   // yakın ve yüksek rcs → yüksek snr → HIGH bekleriz
                new Target("B", 8, 0, 1)
        ));

        var highs = detections.stream()
                .filter(d -> d.getThreat() == ThreatLevel.HIGH)
                .toList();


        assertTrue(highs.stream().allMatch(Detection::isHighConfidence));
    }
    // 1 assertion
}
