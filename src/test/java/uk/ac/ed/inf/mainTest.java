package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import uk.ac.ed.inf.DronePath.*;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;
import uk.ac.ed.inf.OutFiles.Output;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class mainTest {
    public static void main(String[] args) {
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        RestClient server = new RestClient(baseUrl, "2023-03-15");
//        CentralArea centralAreaInstance = new CentralArea();
//        centralAreaInstance.setCentralAreaCoordinates(server);
//        LngLat[] centralAreas = centralAreaInstance.getCoordinates();
        LngLat meadowTop = new LngLat(-3.192473, 55.942617);
        LngLat kfc = new LngLat(-3.184319, 55.946233);
        LngLat forestHill = new LngLat(-3.192473, 55.946233);
        LngLat busStop = new LngLat(-3.184319, 55.942617);
        LngLat cood1 = new LngLat(-3.190, 55.9442617);
        LngLat cood2 = cood1.nextPosition(CompassDirection.NORTH_EAST);
        System.out.println(cood1);
        System.out.println(cood2.lng());

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
//        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
//        LngLat vegan = new LngLat(-3.202541470527649, 55.943284737579376);
//        LngLat sodeberg = new LngLat(-3.1940174102783203, 55.94390696616939);
//        PathNode source = new PathNode(appletonTower);
//        PathNode goal = new PathNode(domino);
////        Drone drone = new Drone(source.getValue(), 0);
//        NoFlyZone[] noFlyZones=  NoFlyZone.getNoFlyZones(server);
//        CentralArea centralArea = new CentralArea();
//        centralArea.setCentralAreaCoordinates(server);
//        PathNode position = flightPath.AStarSearch(noFlyZones, centralArea, source, goal);
//        System.out.println(goal.getPrevious());
//        List<PathNode> path = flightPath.getFlightPath(position);
//        System.out.println(path.size());
//        List<Point> finalPath = new ArrayList<Point>();
//        for (int i = 0; i < path.size(); i++) {
//            LngLat coordinate = path.get(i).getValue();
//            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
//            System.out.println(path.get(i).getValue());
//        }
//
////         -- parsing the path to geoJSON format --
//        Point start = Point.fromLngLat(appletonTower.lng(), appletonTower.lat());
//        Point end = Point.fromLngLat(domino.lng(), domino.lat());
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
//        System.out.println(appletonTower.distanceTo(sodeberg));
//        System.out.println(appletonTower.distanceTo(vegan));

        // testing the flightpath generation
        PathNode node1 = new PathNode(new LngLat(3.0, 5.0));
        node1.setAngleToPrevious(0);
        PathNode node2 = new PathNode(new LngLat(3.1, 5.1));
        node2.setAngleToPrevious(0);
        PathNode node3 = new PathNode(new LngLat(3.2, 5.2));
        node3.setAngleToPrevious(0);
        PathNode node4 = new PathNode(new LngLat(3.3, 5.3));
        node4.setAngleToPrevious(0);
        PathNode node5 = new PathNode(new LngLat(3.4, 5.4));
        node5.setAngleToPrevious(0);
        PathNode node6 = new PathNode(new LngLat(3.5, 5.5));
        PathNode node7 = new PathNode(new LngLat(3.6, 5.6));
        PathNode node8 = new PathNode(new LngLat(3.7, 5.7));
        PathNode node9 = new PathNode(new LngLat(3.8, 5.8));

        List<PathNode> path = new ArrayList<>(Arrays.asList(node1, node2, node3, node4, node5));
        Drone drone = new Drone(server);
//        Output test = drone.generateOutput(order1, pathNodes);
        List<PathNode> result = null;
        if (path != null) {
            List<PathNode> temp = new ArrayList<>();
            result = new ArrayList<>();
            temp.addAll(path);
            // add the path from appleton to the restaurant
            result.addAll(temp);
            // hover when drone picks up order from restaurant
            PathNode collectOrder = temp.get(temp.size() - 1);
            collectOrder.setAngleToPrevious(CompassDirection.NULL.getAngle());
            result.add(collectOrder);
            // reverse the current path to get path back to appleton
            Collections.reverse(temp);
            temp.forEach(p -> p.setAngleToPrevious(CompassDirection.getOppositeDirection(p.getAngleToPrevious())));
            // add the path from restaurant back to appleton
            result.addAll(temp);
            // hover when user picks up order
            PathNode deliverOrder = temp.get(temp.size() - 1);
            deliverOrder.setAngleToPrevious(CompassDirection.NULL.getAngle());
            result.add(deliverOrder);

            // update status of order
            order1.setOutcome(OrderOutcome.DELIVERED);
        }

    }
}