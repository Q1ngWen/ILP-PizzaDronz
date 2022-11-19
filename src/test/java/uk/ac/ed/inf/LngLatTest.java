package uk.ac.ed.inf;

import junit.framework.TestCase;

public class LngLatTest extends TestCase {

    public void testInCentralArea() {
    }

    public void testDistanceTo() {
    }

    public void testCloseTo() {
    }

    public void testNextPosition() {
    }

    public void testLng() {
        LngLat coordinate1 = new LngLat(-3.123141251235152, 123.12341234);
        assertEquals(-3.123141251235152, coordinate1.lng());

        LngLat coordinate2 = new LngLat(0.000000, 0.000000);
        assertEquals(0.0, coordinate2.lng());

        LngLat coordinate3 = new LngLat(12321.2245123410, -34.1234123498);
        assertEquals(12321.2245123410, coordinate3.lng());
    }

    public void testLat() {
        LngLat coordinate1 = new LngLat(-3.123141251235152, 123.12341234);
        assertEquals(123.12341234, coordinate1.lat());

        LngLat coordinate2 = new LngLat(0.000000, 0.000000);
        assertEquals(0.0, coordinate2.lat());

        LngLat coordinate3 = new LngLat(12321.2245123410, -34.1234123498);
        assertEquals(-34.1234123498, coordinate3.lat());
    }
}