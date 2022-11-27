package uk.ac.ed.inf;

import com.mapbox.geojson.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class mainTest {
    public static void main(String[] args){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        RestClient server = new RestClient(baseUrl);
//        CentralArea centralAreaInstance = new CentralArea();
//        centralAreaInstance.setCentralAreaCoordinates(server);
//        LngLat[] centralAreas = centralAreaInstance.getCoordinates();
//        LngLat meadowTop = new LngLat(-3.192473, 55.942617);
//        LngLat kfc = new LngLat(-3.184319,55.946233);
//        LngLat forestHill = new LngLat(-3.192473,55.946233);
//        LngLat busStop = new LngLat(-3.184319,55.942617);
//        LngLat cood1 = new LngLat(-3.190, 55.9442617);
//        LngLat cood2 = new LngLat(-3.012837192, 55.9913912873);
//        System.out.println(cood2.inArea(centralAreas));
//        System.out.println(cood1.inArea(centralAreas));
//        System.out.println(cood1.inCentralArea(server));
//        System.out.println(cood2.inCentralArea(server));
//        System.out.println(cood1.distanceTo(cood2));
//        System.out.println(cood1.closeTo(cood2));
//        System.out.println(cood1.nextPosition(CompassDirection.NORTH));
//        --printing central area coordinates--
//        for (int i = 0; i < centralAreas.length; i++) {
//            System.out.println("Location: " + centralAreas[i].getName());
//            System.out.println("(" + centralAreas[i].getLongitude() + ", " + centralAreas[i].getLatitude() + ")");
//        }

//        --printing list of all restaurants and respective menus--
//        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(server);
//        for (int i = 0; i < restaurants.length; i++) {
//            System.out.println(restaurants[i].getName());
//            System.out.println(restaurants[i].getMenu());
//        }

//        NoFlyZone[] noFlyZones = NoFlyZone.getNoFlyZones(server);
//        for (int i = 0; i < noFlyZones.length; i++) {
//            System.out.println(noFlyZones[i].getName());
//            LngLat[] coordinates = noFlyZones[i].getCoordinates();
//            for (int j = 0; j < coordinates.length; j++) {
//                System.out.println(coordinates[i]);
//            }
////            for (int j = 0; j < coordinates.; j++) {
////                System.out.println(coordinates[i].lng() + ", "+ coordinates[i].lat());
////            }
////            System.out.println();
//        }

//      -- generating a flight path --
//        FlightPath flightPath = new FlightPath();
//        LngLat domino = new LngLat(-3.1838572025299072, 55.94449876875712);
//        LngLat civerinos = new LngLat(	-3.1912869215011597,55.945535152517735);
//        LngLat endPoint = new LngLat(-3.202541470527649,55.943284737579376);
//        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
//        PathNode source = new PathNode(appletonTower);
//        PathNode goal = new PathNode(civerinos);
//        Drone drone = new Drone(source.getValue(), 0);
//        PathNode position = flightPath.AStarSearch(noFlyZones, source, goal);
//        System.out.println(goal.getParent());
//        List<PathNode> path = FlightPath.getPath(position);
//        System.out.println(path.size());
//        List<Point> finalPath = new ArrayList<Point>();
//        for (int i = 0; i < path.size(); i++) {
//            LngLat coordinate = path.get(i).getValue();
//            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
//            System.out.println(path.get(i).getValue());
//        }

//         -- parsing the path to geoJSON format --
//        Point start = Point.fromLngLat(appletonTower.lng(), appletonTower.lat());
//        Point end = Point.fromLngLat(civerinos.lng(), civerinos.lat());
//        LineString linePath = LineString.fromLngLats(finalPath);
//        Geometry g1 = (Geometry) start;
//        Geometry g2 = (Geometry) end;
//        Geometry g3 = (Geometry) linePath;
//        Feature[] features = new Feature[3];
//        features[0] = Feature.fromGeometry(g1);
//        features[1] = Feature.fromGeometry(g2);
//        features[2] = Feature.fromGeometry(g3);
//        FeatureCollection fc = FeatureCollection.fromFeatures(features);
//        System.out.println(fc.toJson());

        // order validation
//        OrderValidator validator = new OrderValidator();
//        Order[] nonValidatedOrders = validator.getNonValidatedOrders(server);
//        System.out.println(nonValidatedOrders.length);
//        validator.isValidCardExpiry("02/23", "02/23");
        String test = "21-12-2022";
        LocalDate date = LocalDate.parse(test);
        System.out.println(date);
    }
}