package uk.ac.ed.inf.DronePath;

/**
 * {@link CompassDirection} represents list of all 16 possible compass points the drone can navigate to.
 */
public enum CompassDirection {
    EAST(0),
    EAST_NORTH_EAST(22.5),
    NORTH_EAST(45),
    NORTH_NORTH_EAST(67.5),
    NORTH(90),
    NORTH_NORTH_WEST(112.5),
    NORTH_WEST(135),
    WEST_NORTH_WEST(157.5),
    WEST(180),
    WEST_SOUTH_WEST(202.5),
    SOUTH_WEST(225),
    SOUTH_SOUTH_WEST(247.5),
    SOUTH(270),
    SOUTH_SOUTH_EAST(292.5),

    SOUTH_EAST(315),

    EAST_SOUTH_EAST(337.5),
    NULL(0);

    private final double angle;

    /**
     * @param angle Numeric representation of the direction that {@link CompassDirection} points.
     */
    CompassDirection(double angle) {
        this.angle = angle;
    }

    /**
     * @return Returns the angle of the current {@link CompassDirection}.
     */
    public double getAngle() {
        return angle;
    }

}
