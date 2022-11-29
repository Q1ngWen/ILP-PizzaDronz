package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import junit.framework.TestCase;
import org.junit.Before;
import uk.ac.ed.inf.DronePath.*;

import java.util.ArrayList;
import java.util.List;

public class PathGeneratorTest extends TestCase {
    private LngLat domino;
    private LngLat civerinos;
    private LngLat vegan;
    private LngLat sodeberg;
    private NoFlyZone[] noFlyZones;
    private PathGenerator pathGenerator;
    private LngLat appletonTower;
    private PathNode source;
    private PathNode goal;
    private PathNode restaurant;
    private CentralArea centralArea;

    @Before
    public void setUp() {
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        RestClient server = new RestClient(baseUrl);
        noFlyZones = NoFlyZone.getNoFlyZones(server);
        centralArea = new CentralArea();
        centralArea.setCentralAreaCoordinates(server);
        pathGenerator = new PathGenerator();
        domino = new LngLat(-3.1838572025299072, 55.94449876875712);
        civerinos = new LngLat(	-3.1912869215011597,55.945535152517735);
        vegan = new LngLat(-3.202541470527649, 55.943284737579376);
        sodeberg = new LngLat(-3.1940174102783203, 55.94390696616939);
        appletonTower = new LngLat(-3.186874, 55.944494);
        source = new PathNode(appletonTower);
        goal = new PathNode(vegan);
        restaurant = pathGenerator.AStarSearch(noFlyZones, centralArea, source, goal);
    }

    public void testGetPathToAppleton() {
        List<PathNode> path = pathGenerator.getFlightPath(restaurant);
        List<Point> finalPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            LngLat coordinate = path.get(i).getValue();
            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
            System.out.println(path.get(i).getValue());
        }

        System.out.println(path.size());

//         -- parsing the path to geoJSON format --
        Point start = Point.fromLngLat(appletonTower.lng(), appletonTower.lat());
        Point end = Point.fromLngLat(vegan.lng(), vegan.lat());
        LineString linePath = LineString.fromLngLats(finalPath);
        Geometry g1 = (Geometry) start;
        Geometry g2 = (Geometry) end;
        Geometry g3 = (Geometry) linePath;
        Feature[] features = new Feature[3];
        features[0] = Feature.fromGeometry(g1);
        features[1] = Feature.fromGeometry(g2);
        features[2] = Feature.fromGeometry(g3);
        FeatureCollection fc = FeatureCollection.fromFeatures(features);
        System.out.println(fc.toJson());

    }

    public void testAStarSearch() {
    }

    public void testIsPathValid() {
    }

    public void testIsNodeValid() {
    }
}