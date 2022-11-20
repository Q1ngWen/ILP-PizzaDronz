package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

public record Drone(LngLat coordinate, double moveCount) {
    private static final double MAX_MOVES = 2000;

}
