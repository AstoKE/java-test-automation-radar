package edu.tedu.radar;

public class Detection {
    private final String targetId;
    private final double distanceKm;
    private final double bearingDeg;
    private final double snr;
    private final ThreatLevel threat;

    public Detection(String targetId, double distanceKm, double bearingDeg, double snr, ThreatLevel threat) {
        this.targetId = targetId; this.distanceKm = distanceKm; this.bearingDeg = bearingDeg;
        this.snr = snr; this.threat = threat;
    }

    public String getTargetId(){ return targetId; }
    public double getDistanceKm(){ return distanceKm; }
    public double getBearingDeg(){ return bearingDeg; }
    public double getSnr(){ return snr; }
    public ThreatLevel getThreat(){ return threat; }
    public boolean isHighConfidence() {
        // Align confidence with your ThreatLevel mapping
        return this.threat == ThreatLevel.HIGH;
    }

    public String assessDetectionPriority() {
        // This logic has 3 decision points, resulting in V(G) = 3 + 1 = 4.

        if (this.threat == ThreatLevel.HIGH && this.distanceKm < 10.0) { // Decision 1 (&& counts as 1)
            return "PRIORITY_1_INTERCEPT";

        } else if (this.threat == ThreatLevel.HIGH) { // Decision 2
            return "PRIORITY_2_MONITOR";

        } else if (this.threat == ThreatLevel.MEDIUM && this.snr > 3.0) { // Decision 3 (&& counts as 1)
            return "PRIORITY_3_TRACK";

        } else {
            return "PRIORITY_4_LOG";
        }
    }

}
