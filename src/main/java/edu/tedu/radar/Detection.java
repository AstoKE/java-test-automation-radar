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
    public ThreatLevel getThreatLevel(){ return threat; }

}
