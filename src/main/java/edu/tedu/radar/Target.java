package edu.tedu.radar;

public class Target {
    private final String id;
    private final double x, y;
    private final double rcs;

    public Target(String id, double x, double y, double rcs) {
        if (rcs < 0) throw new IllegalArgumentException("rcs < 0");
        this.id = id; this.x = x; this.y = y; this.rcs = rcs;
    }

    public String getId(){ return id; }
    public double getX(){ return x; }
    public double getY(){ return y; }
    public double getRcs(){ return rcs; }
}
