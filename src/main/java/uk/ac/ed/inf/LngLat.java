package uk.ac.ed.inf;

/**
 * {@link LngLat} allows the drone to view its position using coordinates. The command helps manage any navigational
 * functions required by the drone to travel around Edinburgh.
 *
 * @param lng The longitude, measured in degrees.
 * @param lat The latitude, measured in degrees.
 */

public record LngLat(double lng, double lat) {

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
    public boolean inCentralArea() {
        // getting the central area corner coordinates
        CentralAreaData centralAreaData = CentralAreaData.getCentral_area_instance();
        CentralDataResponse[] coordinates = centralAreaData.getCoordinates();

        // 1st condition: coordinate's longitude must be between the longitude values of all the segments coordinates,
        // as the horizontal line must then intersect the segment
        // 2nd condition:  the code first multiples then divides, so that small differences between coordinates (which
        // might result in 0 after the division) are supported
        boolean result = false;
        for (int i = 0, j = coordinates.length - 1; i < coordinates.length; j = i++) {
            if ((coordinates[i].longitude() >= lng) != (coordinates[j].longitude() >= lng) &&
                    (lat <= (coordinates[j].latitude() - coordinates[i].latitude()) * (lng - coordinates[i].longitude()) /
                            (coordinates[j].longitude() - coordinates[i].longitude()) + coordinates[i].latitude())) {
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
        // calculating all longitude and latitude distances to be added after travelling in given direction
        double angle_22_5_sin = 0.00015 * Math.sin(Math.PI / 8);
        double angle_22_5_cos = 0.00015 * Math.cos(Math.PI / 8);
        double angle_45 = 0.00015 * Math.sin(Math.PI / 4);

        switch (direction) {
            case NORTH:
                return new LngLat(lng, lat + 0.00015);
            case SOUTH:
                return new LngLat(lng, lat - 0.00015);
            case EAST:
                return new LngLat(lng + 0.00015, lat);
            case WEST:
                return new LngLat(lng - 0.00015, lat);
            case NORTH_EAST:
                return new LngLat(lng + angle_45, lat + angle_45);
            case SOUTH_EAST:
                return new LngLat(lng + angle_45, lat - angle_45);
            case SOUTH_WEST:
                return new LngLat(lng - angle_45, lat - angle_45);
            case NORTH_WEST:
                return new LngLat(lng - angle_45, lat + angle_45);
            case NORTH_NORTH_EAST:
                return new LngLat(lng + angle_22_5_sin, lat + angle_22_5_cos);
            case EAST_NORTH_EAST:
                return new LngLat(lng + angle_22_5_cos, lat + angle_22_5_sin);
            case EAST_SOUTH_EAST:
                return new LngLat(lng + angle_22_5_cos, lat - angle_22_5_sin);
            case SOUTH_SOUTH_EAST:
                return new LngLat(lng + angle_22_5_sin, lat - angle_22_5_cos);
            case SOUTH_SOUTH_WEST:
                return new LngLat(lng - angle_22_5_sin, lat - angle_22_5_cos);
            case WEST_SOUTH_WEST:
                return new LngLat(lng - angle_22_5_cos, lat - angle_22_5_sin);
            case WEST_NORTH_WEST:
                return new LngLat(lng - angle_22_5_cos, lat + angle_22_5_sin);
            case NORTH_NORTH_WEST:
                return new LngLat(lng - angle_22_5_sin, lat + angle_22_5_cos);
            default:
                return new LngLat(lng, lat);
        }
    }
}

// trial