package uk.ac.ed.inf.DronePath;

import com.mapbox.geojson.*;
import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.*;

public class Drone {
    private static final int MAX_MOVES = 2000;
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private LngLat currentCood;
    private int moveCount = 0;
    private Restaurant[] restaurants;
    private Order[] nonValidatedOrders;
    private NoFlyZone[] noFlyZones;
    private CentralArea centralAreaInstance;
    private LngLat[] centralAreas;

    private OrderValidator validator;
    private FlightPath flightPath;

    public Drone(LngLat currentCood) {
        this.currentCood = currentCood;
    }

    public List<PathNode> deliverOrders(RestClient server) {
        // setting up and fetching all JSON data from REST server
        centralAreaInstance = new CentralArea();
        centralAreaInstance.setCentralAreaCoordinates(server);
        centralAreas = centralAreaInstance.getCoordinates();
        noFlyZones=  NoFlyZone.getNoFlyZones(server);
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator();
        validator.setUpValidator(restaurants);
        nonValidatedOrders = validator.getNonValidatedOrders(server);
        flightPath = new FlightPath();

        // initialise the path to the restaurants and pair them to respective restaurants
        HashMap<Restaurant, List<PathNode>> restaurantPath = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            PathNode appletonTower = new PathNode(APPLETON_TOWER);
            PathNode restaurantNode = new PathNode(restaurant.getCoordinate());
            PathNode endNode = flightPath.AStarSearch(noFlyZones, centralAreaInstance, appletonTower, restaurantNode);
            List<PathNode> path = flightPath.getFlightPath(endNode);
            restaurantPath.put(restaurant, path);
        }

        // validate the orders and rearrange them to ascending by distance to appleton tower
        Order[] validatedOrders = validator.getValidOrders(restaurants, nonValidatedOrders);
        PriorityQueue<Order> sortedValidOrders = getSortedOrders(restaurants, validatedOrders,restaurantPath);
        System.out.println(sortedValidOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        List<PathNode> totalFlightPath = new ArrayList<>();
        List<Order> deliveredOrders = new ArrayList<>();



        while (!sortedValidOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedValidOrders.poll();
            if (current == null) break;
            Restaurant restaurant = current.getOrdersRestaurant(restaurants);

            System.out.println("current moves taken: " + moveCount);
            // generate path from appleton tower to restaurant
            if (restaurantPath.containsKey(restaurant)) {
                List<PathNode> path = restaurantPath.get(restaurant);
                // check if theres enough moves left for the delivery and add if there is one
                if (2*path.size() + 2 + moveCount <= MAX_MOVES){
                    addingFlightPath(current, path, totalFlightPath, deliveredOrders);
                }
            }

        }

        System.out.println(deliveredOrders.size());
        return totalFlightPath;
    }

    public List<PathNode> deliverOrders(RestClient server, String orderDate) {
        // setting up and fetching all JSON data from REST server
        centralAreaInstance = new CentralArea();
        centralAreaInstance.setCentralAreaCoordinates(server);
        centralAreas = centralAreaInstance.getCoordinates();
        noFlyZones=  NoFlyZone.getNoFlyZones(server);
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator();
        validator.setUpValidator(restaurants);
        nonValidatedOrders = validator.getNonValidatedOrders(server);
        flightPath = new FlightPath();

        // initialise the path to the restaurants and pair them to respective restaurants
        HashMap<Restaurant, List<PathNode>> restaurantPath = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            PathNode appletonTower = new PathNode(APPLETON_TOWER);
            PathNode restaurantNode = new PathNode(restaurant.getCoordinate());
            PathNode endNode = flightPath.AStarSearch(noFlyZones, centralAreaInstance, appletonTower, restaurantNode);
            List<PathNode> path = flightPath.getFlightPath(endNode);
            restaurantPath.put(restaurant, path);
        }

        // validate the orders and rearrange them to ascending by distance to appleton tower
        Order[] validatedOrders = validator.getValidOrders(restaurants, nonValidatedOrders, orderDate);
        PriorityQueue<Order> sortedValidOrders = getSortedOrders(restaurants, validatedOrders,restaurantPath);
        System.out.println(sortedValidOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        List<PathNode> totalFlightPath = new ArrayList<>();
        List<Order> deliveredOrders = new ArrayList<>();

        while (!sortedValidOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedValidOrders.poll();
            if (current == null) break;
            Restaurant restaurant = current.getOrdersRestaurant(restaurants);
            List<PathNode> path = restaurantPath.get(restaurant);
            // generate path from appleton tower to restaurant
            if (restaurantPath.containsKey(restaurant)) {
//                List<PathNode> path = restaurantPath.get(restaurant);
                // check if theres enough moves left for the delivery and add if there is one
                if (2*path.size() + 2 + moveCount <= MAX_MOVES){
                    addingFlightPath(current, path, totalFlightPath, deliveredOrders);
                }
            }
            System.out.println(moveCount);
            List<Point> finalPath = new ArrayList<>();
            for (int i = 0; i < path.size(); i++) {
                LngLat coordinate = path.get(i).getValue();
                finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
            }
            LineString linePath = LineString.fromLngLats(finalPath);
            Geometry g3 = (Geometry) linePath;
            Feature[] features = new Feature[1];
            features[0] = Feature.fromGeometry(g3);
            FeatureCollection fc = FeatureCollection.fromFeatures(features);
            System.out.println(fc.toJson());

        }

        System.out.println(deliveredOrders.size());
        return totalFlightPath;
    }

    // -- helper functions for delivering orders --
    // function that returns a list of orders in ascending order of distance to appleton tower
    public PriorityQueue<Order> getSortedOrders(Restaurant[] restaurants, Order[] orders, HashMap<Restaurant, List<PathNode>> restaurantPath) {
        PriorityQueue<Order> orderPriorityQueue = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
//                LngLat restaurant1 = o1.getOrdersRestaurant(restaurants).getCoordinate();
//                LngLat restaurant2 = o2.getOrdersRestaurant(restaurants).getCoordinate();
                double delivery1 = restaurantPath.get(o1.getOrdersRestaurant(restaurants)).size();
                double delivery2 = restaurantPath.get(o2.getOrdersRestaurant(restaurants)).size();
                if (delivery1 > delivery2) {
                    return 1;
                } else if (delivery1 < delivery2) {
                    return -1;
                } else return 0;
            }
        });
        for (Order order : orders) {
            orderPriorityQueue.add(order);
        }
        return orderPriorityQueue;
    }

    // function checks if the generated flight path is feasible - move count, etc. and adds it to the total flight path
    public void addingFlightPath(Order order, List<PathNode> path, List<PathNode> totalFlightPath, List<Order> delivered) {
        if (2*path.size() + moveCount <= MAX_MOVES) {
            List<PathNode> temp = new ArrayList<>();
            temp = path;
            System.out.println(path.size());
            // add the path from appleton to the restaurant
            totalFlightPath.addAll(path);
            // hover when drone picks up order from restaurant
            System.out.println("From appleton to restaurant");
            System.out.println(path.get(0).getValue());
            System.out.println(path.get(path.size()-1).getValue());
            totalFlightPath.add(path.get(path.size()-1));
            // add the path from restaurant back to appleton
            Collections.reverse(path);
            totalFlightPath.addAll(path);
            // hover when user picks up order
            System.out.println("From restaurant to appleton");
            System.out.println(path.get(0).getValue());
            System.out.println(path.get(path.size()-1).getValue());
            totalFlightPath.add(path.get(path.size()-1));
            Collections.reverse(path);
            order.setStatus(OrderOutcome.DELIVERED);
            moveCount += 2* path.size() + 2;
            delivered.add(order);
        }
    }
}
