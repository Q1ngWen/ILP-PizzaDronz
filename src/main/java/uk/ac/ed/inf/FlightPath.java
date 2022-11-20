package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

import java.util.*;

public class FlightPath {
    private NoFlyZone noFlyZone;
    private LngLat dropOffPoint;
    private int ticksElasped;
//    private final Point APPLETON_TOWER = Point.fromLngLat(−3.186874, 55.944494);

    public FlightPath(){}

    public String generateFlightPath(RestClient server) {
        String result;
        LngLat cood = new LngLat(-3.1930, 55.9460);
        Point randomDrop = Point.fromLngLat(-3.1930, 55.9460);
        Point at = Point.fromLngLat(-3.186874, 55.944494);

        Drone drone = new Drone(cood, 0);
        Feature pointFeature = Feature.fromGeometry(randomDrop);
        FeatureCollection fc = FeatureCollection.fromFeature(pointFeature);
        result = fc.toString();
        return result;
    }

    public static void AstarSearch(LngLat source, LngLat goal, Drone drone) {
        Set<LngLat> explored = new HashSet<LngLat>();
        PriorityQueue<LngLat> queue = new PriorityQueue<LngLat>(20,
                new Comparator<LngLat>() {
                    @Override
                    public int compare(LngLat a, LngLat b) {
                        if (a.getfScore() > b.getfScore()) {
                            return 1;
                        } else if (a.getfScore() < b.getfScore()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
        
//        cost from start
        source.setgScore(0);
        queue.add(source);

        boolean found = false;

        while (!queue.isEmpty() && !found) {
            // the coordinate in having the lowest f score value
            LngLat current = queue.poll();

            explored.add(current);
        }
    }

    public static List<LngLat> printPath(LngLat target) {
        List<LngLat> path = new ArrayList<LngLat>();
//        for (LngLat cood = target; cood != null; )
        return path;
    }
}
