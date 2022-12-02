package uk.ac.ed.inf.DronePath;

import junit.framework.TestCase;

public class LngLatTest extends TestCase {

    public void testInArea() {
    }

    public void testDistanceTo() {
    }

    public void testCloseTo() {
    }

    public void testNextPosition() {
        LngLat from = new LngLat(-3.1839894336253907,55.94451777690016);
        LngLat to = new LngLat(-3.186724, 55.944494);
        System.out.println(from.nextPosition(CompassDirection.WEST_NORTH_WEST));
    }

    public void testIsIntersecting() {
    }

    public void testOnSegment() {
    }

    public void testOrientation() {
    }

    public void testLng() {
    }

    public void testLat() {
    }

    public void testGetDirectionTo() {
        LngLat center = new LngLat(	-3.1900, 55.9442617);
        LngLat point = new LngLat(	     -3.1900574025148547,
                55.94440028192987);

        System.out.println(center.getDirectionTo(point));
    }
}