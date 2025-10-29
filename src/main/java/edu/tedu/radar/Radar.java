package edu.tedu.radar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Radar {
    private final String id;
    private final double maxRangeKm;
    private final double snrThreshold;

    public Radar(String id, double maxRangeKm, double snrThreshold) {
        if (maxRangeKm <= 0 || snrThreshold < 0) throw new IllegalArgumentException("Invalid parameters");
        this.id = id;
        this.maxRangeKm = maxRangeKm;
        this.snrThreshold = snrThreshold;
    }

    public List<Detection> scan(List<Target> targets) {
        List<Detection> detections = new ArrayList<>();
        for (Target t : targets) {
            double distance = distanceKm(0, 0, t.getX(), t.getY());
            if (distance <= maxRangeKm) {
                double snr = t.getRcs() / (1.0 + distance);
                if (snr >= snrThreshold) {
                    double bearing = bearingDeg(0, 0, t.getX(), t.getY());
                    ThreatLevel level = snr > snrThreshold * 2 ? ThreatLevel.HIGH :
                            snr > snrThreshold * 1.2 ? ThreatLevel.MEDIUM :
                                    ThreatLevel.LOW;
                    detections.add(new Detection(t.getId(), distance, bearing, snr, level));
                }
            }
        }
        detections.sort(Comparator.comparing(Detection::getTargetId));
        return detections;
    }

    // helpers (package-private) – good for parameterized testing
    double distanceKm(double x1,double y1,double x2,double y2){ return Math.hypot(x2-x1, y2-y1); }
    double bearingDeg(double x1,double y1,double x2,double y2){
        double ang = Math.toDegrees(Math.atan2(y2-y1, x2-x1));
        return (ang + 360) % 360;
    }

    public String getId() { return id; }
    public double getMaxRangeKm() { return maxRangeKm; }
    public double getSnrThreshold() { return snrThreshold; }
}
