package uk.ac.ed.inf.Map;

import junit.framework.TestCase;

public class LngLatTest extends TestCase {
    public LngLat cood1;
    public LngLat cood2;
    public LngLat cood3;
    public LngLat cood4;
    public LngLat cood5;
    public LngLat cood6;
    public LngLat cood7;
    public LngLat cood8;
    public LngLat cood9;
    public LngLat cood10;
    public LngLat[] area;

    public void setUp() throws Exception {
        super.setUp();
        cood1 = new LngLat(-3.181630, 55.945809);
        cood2 = new LngLat(-3.181630, 55.945545);
        cood3 = new LngLat(-3.182372, 55.945484);
        cood4 = new LngLat(-3.182227, 55.945547);
            cood5 = new LngLat(-3.182729, 55.945809);
        cood6 = new LngLat(-3.182105, 55.945703);
        cood7 = new LngLat(-3.181630, 55.945586);
        cood8 = new LngLat(-3.182529, 55.945513);
        cood9 = new LngLat(-3.182320, 55.945809);
        cood10 = new LngLat(-3.1821795, 55.945809);
        area = new LngLat[]{cood1, cood2, cood3, cood4, cood5};
    }

    // test case 1: checking if a point is in an area function
    public void testInArea() {
        // test condition 1: point is within area, return true
        assertTrue(cood6.inArea(area));

        // test condition 2: point is on the vertical bounds of the area, return true
//        assertTrue(cood7.inArea(area));

        // test condition 3: point is on the horizontal bounds of the area, return true
        assertTrue(cood9.inArea(area));

        // test condition 4: point is on the diagonal bounds of the area, return true
        assertTrue(cood10.inArea(area));

        // test condition 3: point is not within area, return false
        assertFalse(cood8.inArea(area));
    }

    // test case 2: checking the calculated distance between 2 points
    public void testDistanceTo() {
        // test condition 1: checking calculated distance correctness in all directions
        // direction 1: vertical distance difference
        assertEquals((double)Math.round(cood2.distanceTo(cood1) * 1000000) / 1000000, 0.000264);
        // direction 2: horizontal distance difference
        assertEquals((double)Math.round(cood4.distanceTo(cood6) * 1000000) / 1000000, 0.000198);
        // direction 3: diagonal distance difference
        assertEquals((double)Math.round(cood3.distanceTo(cood8) * 1000000) / 1000000, 0.000160);

        // test condition 2: checking if same points return 0
        assertEquals((double)Math.round(cood2.distanceTo(cood2) * 1000000) / 1000000, 0.0);

        // https://dev.to/itnext/writing-good-unit-tests-a-step-by-step-tutorial-1l4f
        //(double)Math.round(cood2.distanceTo(cood1) * 1000000) / 1000000
    }

    // test case 3: checking if 2 coordinates are close to each other (less than 0.00015 degrees difference)
    public void testCloseTo() {
        LngLat cood;

        // test condition 1: coordinate A is less than 0.00015 degrees away from coordinate B
        cood = new LngLat(-3.182729, 55.945700);
        assertTrue(cood.closeTo(cood5));

        // test condition 2: coordinate A is exactly 0.00015 degrees away from coordinate B
        cood = new LngLat(-3.182729, 55.945659);
        assertTrue(cood.closeTo(cood5));

        // test condition 3: coordinate A is more than 0.00015 degrees away from coordinate B
        assertFalse(cood1.closeTo(cood3));

    }

    // test case:
    public void testNextPosition() {
    }

    public void testGetDirectionTo() {
    }

    // test case: checking
    public void testIsIntersecting() {
    }

    // test case: checking if any point lies on the line segment of 2 points.
    public void testOnSegment() {
        // test condition 1: point is on a segment

        // test condition 2: point is not on a segment

        // test condition 3: point is right next to a segment
    }

    // test case: checking the orientation of 3 coordinates
    public void testOrientation() {
        // test condition 1: coordinates are collinear with each other


        // test condition 2: coordinates are clockwise with each other

        // test condition 3: coordinates are anti-clockwise with each other

        // test condition 4: coordinates are all the same/equal

    }
}