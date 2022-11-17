package uk.ac.ed.inf;

/**
 * {@link CompassDirection} represents list of all 16 possible compass points the drone can navigate to.
 */
public enum CompassDirection {
    NORTH, SOUTH, EAST, WEST,
    NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST,
    NORTH_NORTH_EAST, EAST_NORTH_EAST, EAST_SOUTH_EAST, SOUTH_SOUTH_EAST,
    SOUTH_SOUTH_WEST, WEST_SOUTH_WEST, WEST_NORTH_WEST, NORTH_NORTH_WEST,
    NULL
}
