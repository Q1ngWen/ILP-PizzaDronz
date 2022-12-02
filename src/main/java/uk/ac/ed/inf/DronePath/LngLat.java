package uk.ac.ed.inf.DronePath;

/**
 * {@link LngLat} allows the drone to view its position using coordinates. The command helps manage any navigational
 * functions required by the drone to travel around Edinburgh.
 *
 * @param lng The longitude, measured in degrees.
 * @param lat The latitude, measured in degrees.
 */

public record LngLat(double lng, double lat) {
    private static final double MOVE_DISTANCE = 0.00015;

    /**
     * The function calculates the bounds of a polygon (formed by the corner coordinates of the central area provided)
     * This is done with the axis crossing method via the Winding Number algorithm. The function searches for a 'line'
     * that begins at the tested coordinate, and extends to infinity to the right side of the longitude (x-axis).
     * For each polygon segment, it checks if the line crosses it. If the total number of intersections is odd,
     * then the coordinate is within the central area, else, it is outside.
     *
     * @return Returns true if a coordinate is within or on the bounds of the central area.
     * @see <a href="https://www.engr.colostate.edu/~dga/documents/papers/point_in_polygon.pdf">Winding Number algorithm by Dan Sunday</a>.
     */
    public boolean inArea(LngLat[] coordinates) {
        int length = coordinates.length;
        // check if the coordinate is on the line of the polygon
        if (length < 3) {
            System.err.println("Insufficient number of coordinates provided, no polygon could be formed");
        }

        boolean result = false;
        for (int i = 0, j = length - 1; i < length; j = i++) {
            if (((coordinates[i].lng() > this.lng) != (coordinates[j].lng() >= lng)) &&
                    (lat <= (coordinates[j].lat() - coordinates[i].lat()) * (lng - coordinates[i].lng()) /
                            (coordinates[j].lng() - coordinates[i].lng()) + coordinates[i].lat())) {
                result = !result;
            }
        }
        return result;
    }

    /**
     * @param cood The {@link LngLat} coordinate that will be calculated.
     * @return The distance between 2 coordinates.
     */
    public double distanceTo(LngLat cood) {
        double distance = 0;
        try {
            distance = Math.sqrt(Math.pow((lng - cood.lng), 2) + Math.pow((lat - cood.lat), 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distance;
    }

    /**
     * @param cood The {@link LngLat} coordinate that will be calculated.
     * @return True if the coordinate is within a 0.00015 degrees radius of each other.
     */
    public boolean closeTo(LngLat cood) {
        boolean result = false;
        try {
            result = !(distanceTo(cood) >= 0.00015);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param direction The {@link CompassDirection} that directs the direction for the drone to fly to.
     * @return The new coordinate after travelling in the given direction.
     */
    public LngLat nextPosition(CompassDirection direction) {
        double angle = Math.toRadians(direction.getAngle());
        return new LngLat(this.lng + MOVE_DISTANCE * Math.cos(angle),
                this.lat + MOVE_DISTANCE * Math.sin(angle));
    }

    /**
     * @param cood The {@link LngLat} that will be compared with the current {@link LngLat}.
     * @return The angle between the current and input {@link LngLat}.
     * The angle starts from 0 at East and increases to 360 in an anticlockwise manner.
     */
    public double getDirectionTo(LngLat cood) {
        double difLng = cood.lng - this.lng;
        double difLat = cood.lat - this.lat;
        double angle = Math.round((Math.toDegrees(Math.atan2(difLat, difLng))) * 2) / 2.0;
        return (angle + 360) % 360;
    }

    /**
     * @param p1 A {@link LngLat} coordinate forming line segment 1.
     * @param q1 A {@link LngLat} coordinate forming line segment 1.
     * @param p2 A {@link LngLat} coordinate forming line segment 2.
     * @param q2 A {@link LngLat} coordinate forming line segment 2.
     * @return The function returns true if the the 2 line segments intersect each other.
     * @see <a href="https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/">How to check
     * if two given line segments intersect?</a>
     */
    public boolean isIntersecting(LngLat p1, LngLat q1, LngLat p2, LngLat q2) {
        // find the 4 orientations needed for general and special cases
        double o1 = orientation(p1, q1, p2);
        double o2 = orientation(p1, q1, q2);
        double o3 = orientation(p2, q2, p1);
        double o4 = orientation(p2, q2, q1);

        // general case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // special case
        // p1, q1, p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1, q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2, p1 are collinear and p1 lies on segment p1q1
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2, q1 are collinear and q1 lies on segment p1q1
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false;
    }

    // -- helper functions below for checking in area function (checking line segments) --

    /**
     * @param p A collinear {@link LngLat} coordinate.
     * @param q A collinear {@link LngLat} coordinate.
     * @param r A collinear {@link LngLat} coordinate.
     * @return Function returns true if coordinate q lies on the line segment pr.
     * @see <a href="https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/">How to check
     * if two given line segments intersect?</a>
     */
    public boolean onSegment(LngLat p, LngLat q, LngLat r) {
        if (q.lat() <= Math.max(p.lat(), r.lat()) && q.lat() >= Math.min(p.lat(), r.lat()) &&
                q.lng() <= Math.max(p.lng(), r.lng()) && q.lng() >= Math.min(p.lng(), r.lng())) {
            return true;
        }
        return false;
    }

    /**
     * @param p A {@link LngLat} coordinate.
     * @param q A {@link LngLat} coordinate.
     * @param r A {@link LngLat} coordinate.
     * @return Returns the orientation of ordered triplets (p, q, r) with the corresponding values:
     * 0 -> p, q, r are collinear
     * 1 -> clockwise
     * 2 -> counter clockwise
     * @see <a href="https://www.geeksforgeeks.org/orientation-3-ordered-points/">Orientation of 3 ordered coordinates</a>
     */
    public double orientation(LngLat p, LngLat q, LngLat r) {
        double val = (q.lat() - p.lat()) * (r.lng() - q.lng()) -
                (q.lng() - p.lng()) * (r.lat() - q.lat());
        if (val == 0) return 0; //collinear
        return (val > 0) ? 1 : 2; // clockwise or counterclockwise
    }
}