package uk.ac.ed.inf.DronePath;

/**
 * {@link CompassDirection} represents list of all 16 possible compass points the drone can navigate to.
 */
public enum CompassDirection {
    NORTH(0),
    SOUTH(Math.PI),
    EAST(Math.PI/2),
    WEST(3*Math.PI/2),
    NORTH_EAST(Math.PI/4),
    NORTH_WEST(7*Math.PI/4),
    SOUTH_EAST(3*Math.PI/4),
    SOUTH_WEST(5*Math.PI/4),
    NORTH_NORTH_EAST(Math.PI/8),
    EAST_NORTH_EAST(3*Math.PI/8),
    EAST_SOUTH_EAST(5*Math.PI/8),
    SOUTH_SOUTH_EAST(7*Math.PI/8),
    SOUTH_SOUTH_WEST(9*Math.PI/8),
    WEST_SOUTH_WEST(11*Math.PI/8),
    WEST_NORTH_WEST(13*Math.PI/8),
    NORTH_NORTH_WEST(15*Math.PI/8),
    NULL(0);

    private final double angle;

    CompassDirection(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
