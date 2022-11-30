package uk.ac.ed.inf;

import uk.ac.ed.inf.DronePath.CompassDirection;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class mainTest {
    public static void main(String[] args){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        RestClient server = new RestClient(baseUrl);
//        CentralArea centralAreaInstance = new CentralArea();
//        centralAreaInstance.setCentralAreaCoordinates(server);
//        LngLat[] centralAreas = centralAreaInstance.getCoordinates();
        LngLat meadowTop = new LngLat(-3.192473, 55.942617);
        LngLat kfc = new LngLat(-3.184319,55.946233);
        LngLat forestHill = new LngLat(-3.192473,55.946233);
        LngLat busStop = new LngLat(-3.184319,55.942617);
        LngLat cood1 = new LngLat(-3.190, 55.9442617);
        LngLat cood2 = cood1.nextPosition(CompassDirection.NORTH_EAST);
        System.out.println(cood1);
        System.out.println(cood2.lng());

        System.out.println(cood2.lat());
        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        test.add(6);
        test.add(7);
        test.add(8);
        System.out.println(test);
        List<Integer> test2 = test;
//        Collections.reverse(test2);
        System.out.println(test);
        System.out.println(test2);
        List<Integer> test3 = new ArrayList<>();
        test3.addAll(test);
        Collections.reverse(test2);
        System.out.println(test);
        System.out.println(test2);
        System.out.println(test3);
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
//        PathGenerator flightPath = new PathGenerator();
//        LngLat domino = new LngLat(-3.1838572025299072, 55.94449876875712);
//        LngLat civerinos = new LngLat(	-3.1912869215011597,55.945535152517735);
//        LngLat endPoint = new LngLat(-3.202541470527649,55.943284737579376);
        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        LngLat vegan = new LngLat(-3.202541470527649, 55.943284737579376);
        LngLat sodeberg = new LngLat(-3.1940174102783203, 55.94390696616939);
        PathNode source = new PathNode(appletonTower);
        PathNode goal = new PathNode(vegan);
//        Drone drone = new Drone(source.getValue(), 0);
//        PathNode position = flightPath.AStarSearch(noFlyZones, source, goal);
//        System.out.println(goal.getParent());
//        List<PathNode> path = PathGenerator.getPath(position);
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
//        String test = "21-12-2022";
//        LocalDate date = LocalDate.parse(test);
//        System.out.println(date);
        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(server);
        String pizzaItem1 = "Super Cheese";
        String pizzaItem3 = "Margarita";
        String pizzaItem4 = "Calzone";

        String[] pizzaOrder1 = {pizzaItem1};
        String[] pizzaOrder2 = {pizzaItem3, pizzaItem4, pizzaItem3, pizzaItem4};

        Order order1 = new Order("1AFFE082", "2023-01-01", "Gilberto Handshoe",
                "2402902", "04/28", "922", 1500, pizzaOrder1);
        Order order2 = new Order("12340B01", "2023-03-29", "Vernice Kaza",
                "4355175523891417", "07/12", "550", 5300, pizzaOrder2);

//        System.out.println(order1.getOrdersRestaurant(restaurants));
//        System.out.println(order2.getOrdersRestaurant(restaurants));
        System.out.println(appletonTower.distanceTo(sodeberg));
        System.out.println(appletonTower.distanceTo(vegan));
    }
}