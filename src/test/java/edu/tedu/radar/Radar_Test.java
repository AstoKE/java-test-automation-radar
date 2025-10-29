package edu.tedu.radar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Radar_Test {

    private final Radar radarForParamTest = new Radar("R_Param", 100, 0.5);

    private Target targetAtDistanceWithSnr(String id, double distance, double snr) {
        double rcs = snr * (1.0 + distance);
        return new Target(id, distance, 0.0, rcs); // (x=distance, y=0) => distance sabit ve kolay
    }

    private Target targetForThreat(String id, double distance, double thr, String threatKind) {
        double eps = 1e-3 * Math.max(1.0, thr);
        double snr;
        switch (threatKind) {
            case "LOW":    snr = thr;                    break;                 // eşik
            case "MEDIUM": snr = 1.2 * thr + eps;       break;                 // 1.2*thr üstü
            case "HIGH":   snr = 2.0 * thr + eps;       break;                 // 2*thr üstü
            default: throw new IllegalArgumentException("Unknown kind: " + threatKind);
        }
        return targetAtDistanceWithSnr(id, distance, snr);
    }

    // (Assertion 16-19): Incorporates GeometryParamTest.java (4 assertions)
    @ParameterizedTest
    @CsvSource({
            "0,0, 3,4, 5.0, 53.130102",   // Data Set 1
            "0,0, -3,4, 5.0, 126.869898" // Data Set 2
    })
    void distanceAndBearing(double x1, double y1, double x2, double y2,
                            double dist, double bearing) {
        assertAll(
                () -> assertEquals(dist, radarForParamTest.distanceKm(x1, y1, x2, y2), 1e-6), // 16,17
                () -> assertEquals(bearing, radarForParamTest.bearingDeg(x1, y1, x2, y2), 1e-5) // 18,19
        );
    }

    // (Assertion 20-25): scanDetectsTargetsWithinRangeAndThreshold (6 assertions)
    @Test
    void scanDetectsTargetsWithinRangeAndThreshold() {
        Radar radar = new Radar("R1", 10.0, 0.5);
        List<Target> targets = List.of(
                new Target("A", 3, 4, 5),    // distance = 5, snr = 5/6 ≈ 0.83 (passes)
                new Target("B", 12, 0, 10),  // out of range
                new Target("C", 1, 1, 0.3)   // snr ≈ 0.12 (too low)
        );
        List<Detection> detections = radar.scan(targets);

        assertEquals(1, detections.size()); // 20
        Detection d = detections.get(0);

        assertAll(
                () -> assertEquals("A", d.getTargetId()),            // 21
                () -> assertTrue(d.getSnr() >= 0.5),                 // 22
                () -> assertEquals(5.0, d.getDistanceKm(), 1e-9),    // 23
                () -> assertEquals(53.130102, d.getBearingDeg(), 1e-5) // 24
        );
        assertNotEquals(ThreatLevel.LOW, d.getThreat());             // 25
    }

    // (Assertion 26-27): constructorValidation for Radar (2 assertions)
    @Test
    void radarConstructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Radar("R", 0, 0.5));   // 26
        assertThrows(IllegalArgumentException.class, () -> new Radar("R", 10, -0.1)); // 27
    }

    // (Assertion 28): listOrderIsSortedByTargetId (1 assertion)
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

        assertIterableEquals(List.of("A", "B", "C"), ids); // 28
    }

    // (Assertion 29): highThreatImpliesHighConfidence (1 assertion)
    @Test
    void highThreatImpliesHighConfidence() {
        Radar radar = new Radar("R2", 20.0, 0.5);
        var detections = radar.scan(List.of(
                new Target("A", 2, 0, 5), // likely HIGH
                new Target("B", 8, 0, 1)
        ));

        var highs = detections.stream()
                .filter(d -> d.getThreat() == ThreatLevel.HIGH)
                .toList();

        assertTrue(highs.stream().allMatch(Detection::isHighConfidence)); // 29
    }

    // (Assertion 30-34): Threat level boundaries
    @Test
    void threatLevelBoundaryChecks() {
        Radar radar = new Radar("T", 10.0, 1.0);
        double dist = 1.0; // SNR = RCS / 2

        double eps = 1e-6;

        // MEDIUM için 1.2'nin AZ ÜSTÜ
        Target tM = new Target("M", dist, 0, 2.4 + 2*eps); // snr ≈ 1.200001 > 1.2
        // HIGH için 2.0'ın AZ ÜSTÜ (gerekirse)
        Target tH = new Target("H", dist, 0, 4.0 + 2*eps); // snr ≈ 2.000001 > 2.0
        // LOW
        Target tL = new Target("L", dist, 0, 2.0);         // snr = 1.0

        List<Detection> detections = radar.scan(List.of(tM, tH, tL));
        Detection low    = detections.stream().filter(d -> d.getTargetId().equals("L")).findFirst().get();
        Detection medium = detections.stream().filter(d -> d.getTargetId().equals("M")).findFirst().get();
        Detection high   = detections.stream().filter(d -> d.getTargetId().equals("H")).findFirst().get();

        assertEquals(ThreatLevel.LOW,    low.getThreat());
        assertEquals(ThreatLevel.MEDIUM, medium.getThreat());
        assertEquals(ThreatLevel.HIGH,   high.getThreat());
        assertEquals(3, detections.size());
        assertNotNull(low);
    }


    // (Assertion 35-39): Range boundary detection
    @Test
    void rangeBoundaryDetection() {
        Radar radar = new Radar("R4", 5.0, 0.1);

        Target tAt = new Target("T_At", 5.0, 0, 1.0);       // on boundary
        Target tOut = new Target("T_Out", 5.000001, 0, 1.0); // just outside
        Target tIn = new Target("T_In", 1.0, 0, 1.0);

        List<Detection> detections = radar.scan(List.of(tAt, tOut, tIn));

        assertEquals(2, detections.size()); // 35

        assertAll(
                () -> assertTrue(detections.stream().anyMatch(d -> d.getTargetId().equals("T_At"))),  // 36
                () -> assertFalse(detections.stream().anyMatch(d -> d.getTargetId().equals("T_Out"))),// 37
                () -> assertTrue(detections.stream().anyMatch(d -> d.getTargetId().equals("T_In")))    // 38
        );

        double distAt = detections.stream()
                .filter(d -> d.getTargetId().equals("T_At"))
                .findFirst().get()
                .getDistanceKm();
        assertEquals(5.0, distAt, 1e-9); // 39
    }

    // (Assertion 40-42): No targets pass (negative)
    @Test
    void noTargetsPassFilters() {
        Radar radar = new Radar("R5", 10.0, 5.0); // very high threshold
        List<Target> targets = List.of(
                new Target("TooFar", 15.0, 0, 10.0),
                new Target("LowSNR", 1.0, 0, 1.0) // snr = 0.5 < 5.0
        );

        List<Detection> detections = radar.scan(targets);

        assertTrue(detections.isEmpty()); // 40
        assertEquals(0, detections.size()); // 41
        assertNotNull(radar); // 42
    }

    // (Assertion 43-45): Bearing/distance helpers
    @Test
    void bearingBoundaryCases() {
        Target t0 = new Target("T0", 1.0, 0.0, 1.0);     // 0 deg
        Target t90 = new Target("T90", 0.0, 1.0, 1.0);   // 90 deg
        Target t225 = new Target("T225", -1.0, -1.0, 1.0);// 225 deg

        assertEquals(0.0,   radarForParamTest.bearingDeg(0, 0, t0.getX(),   t0.getY()),   1e-9); // 43
        assertEquals(90.0,  radarForParamTest.bearingDeg(0, 0, t90.getX(),  t90.getY()),  1e-9); // 44
        assertEquals(225.0, radarForParamTest.bearingDeg(0, 0, t225.getX(), t225.getY()), 1e-9); // 45
    }
    // (Assertion 46-47) getId and getMaxRangeKm correctness
    @Test
    void gettersReturnConstructorValues() {
        Radar r = new Radar("R1", 10.0, 0.5);

        assertAll(
                () -> assertEquals("R1", r.getId()),
                () -> assertEquals(10.0, r.getMaxRangeKm(), 1e-9)
        );
    }
    @Test
    void emptyInput_returnsEmptyList() {
        Radar r = new Radar("R", 10.0, 0.5);
        List<Detection> out = r.scan(List.of());
        assertTrue(out.isEmpty());
    }
    @Test
    void inRangeButBelowThreshold_returnsEmpty() {
        Radar r = new Radar("R", 10.0, 0.9); // geniş menzil, yüksek eşik
        Target lowSnr = new Target("L", 1.0, 0.0, 1.0); // rcs=1.0 → snr=0.5 < 0.9
        List<Detection> out = r.scan(List.of(lowSnr));
        assertTrue(out.isEmpty(), "SNR eşik altı olduğu için detection olmamalı");
    }
    @Test
    void threat_low_at_threshold() {
        double thr = 1.0;
        Radar r = new Radar("R", 10.0, thr);
        Target t = targetForThreat("L", 1.0, thr, "LOW");
        var out = r.scan(List.of(t));
        assertEquals(1, out.size());
        assertEquals(ThreatLevel.LOW, out.get(0).getThreatLevel());
    }

    @Test
    void threat_medium_above_1p2_thr() {
        double thr = 1.0;
        Radar r = new Radar("R", 10.0, thr);
        Target t = targetForThreat("M", 1.0, thr, "MEDIUM");
        var out = r.scan(List.of(t));
        assertEquals(1, out.size());
        assertEquals(ThreatLevel.MEDIUM, out.get(0).getThreatLevel());
    }

    @Test
    void threat_high_above_2_thr() {
        double thr = 1.0;
        Radar r = new Radar("R", 10.0, thr);
        Target t = targetForThreat("H", 1.0, thr, "HIGH");
        var out = r.scan(List.of(t));
        assertEquals(1, out.size());
        assertEquals(ThreatLevel.HIGH, out.get(0).getThreatLevel());
    }

}

