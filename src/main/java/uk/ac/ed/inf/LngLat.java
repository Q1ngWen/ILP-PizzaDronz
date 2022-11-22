package uk.ac.ed.inf;

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
    public boolean inArea(RestClient server) {
        // getting the central area corner coordinates
        CentralArea[] centralAreas = new CentralArea().getCoordinates(server);

        // 1st condition: coordinate's longitude must be between the longitude values of all the segments coordinates,
        // as the horizontal line must then intersect the segment
        // 2nd condition:  the code first multiples then divides, so that small differences between coordinates (which
        // might result in 0 after the division) are supported
        boolean result = false;
        for (int i = 0, j = centralAreas.length - 1; i < centralAreas.length; j = i++) {
            if ((centralAreas[i].getLongitude() >= lng) != (centralAreas[j].getLongitude() >= lng) &&
                    (lat <= (centralAreas[j].getLatitude() - centralAreas[i].getLatitude()) * (lng - centralAreas[i].getLongitude()) /
                            (centralAreas[j].getLongitude() - centralAreas[i].getLongitude()) + centralAreas[i].getLatitude())) {
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
        return new LngLat(this.lng + MOVE_DISTANCE * Math.cos(direction.getAngle()),
                this.lat + MOVE_DISTANCE * Math.sin(direction.getAngle()));
    }
}

