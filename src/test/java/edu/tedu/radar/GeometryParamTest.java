package edu.tedu.radar;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GeometryParamTest {
    private final Radar radar = new Radar("R1", 100, 0.5);

    @ParameterizedTest
    @CsvSource({
        "0,0, 3,4, 5.0, 53.130102",
        "0,0, -3,4, 5.0, 126.869898"
    })
    void distanceAndBearing(double x1,double y1,double x2,double y2,double dist,double bearing){
        assertAll(
            () -> assertEquals(dist, radar.distanceKm(x1,y1,x2,y2), 1e-6),
            () -> assertEquals(bearing, radar.bearingDeg(x1,y1,x2,y2), 1e-5)
        );
    }
    // Since they are parameterized tests the runs twice
    // So there will we 4 assertions

}
