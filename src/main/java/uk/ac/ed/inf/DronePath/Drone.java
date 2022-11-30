package uk.ac.ed.inf.DronePath;

import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Orders.Deliveries;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.time.Clock;
import java.util.*;

public class Drone {
    private static final int MAX_MOVES = 2000;
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private LngLat currentCood;
    private RestClient server;
    private int moveCount = 0;
    private Restaurant[] restaurants;
    private NoFlyZone[] noFlyZones;
    private CentralArea centralAreaInstance;

    private OrderValidator validator;
    private PathGenerator pathGenerator;
    private HashMap<Restaurant, List<PathNode>> restaurantPath;
    private List<PathNode> totalFlightPath;
    private Clock clock;

    public Drone(RestClient server, Clock clock) {
        // setting up and fetching all JSON data from REST server
        this.server = server;
        this.clock = clock;
        centralAreaInstance = new CentralArea();
        centralAreaInstance.setCentralAreaCoordinates(server);
        noFlyZones=  NoFlyZone.getNoFlyZones(server);
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator(restaurants);
        pathGenerator = new PathGenerator();
        totalFlightPath = new ArrayList<>();
        this.currentCood = APPLETON_TOWER;

        // initialise the path to the restaurants and pair them to respective restaurants
        restaurantPath = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            PathNode appletonTower = new PathNode(APPLETON_TOWER);
            PathNode restaurantNode = new PathNode(restaurant.getCoordinate());
            PathNode endNode = pathGenerator.AStarSearch(noFlyZones, centralAreaInstance, appletonTower, restaurantNode);
            List<PathNode> path = pathGenerator.getFlightPath(endNode);
//            List<PathNode> pathToFrom = p
            restaurantPath.put(restaurant, path);
            System.out.println(endNode.getValue());
        }
    }

    public HashMap<Order, List<PathNode>> deliverOrders() {
        // validate the orders and rearrange them to ascending by distance to appleton tower
        List<Order> orders = validator.getOrders(server);
        PriorityQueue<Order> sortedOrders = getSortedOrders(restaurants, orders.toArray(new Order[0]), restaurantPath);
        System.out.println("Total orders taken: " + sortedOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        HashMap<List<Deliveries>, List<FlightPath>> deliveryPathTemp = new HashMap<>();
        HashMap<Order, List<PathNode>> deliveryPath = new HashMap<>();

        while (!sortedOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedOrders.poll();
            if (current == null) break;
            if (!validator.isValidOrder(restaurants, current)) {
                deliveryPath.put(current, null);
            }
            Restaurant restaurant = current.getOrdersRestaurant(restaurants);

            System.out.println("current moves taken: " + moveCount);
            // generate path from appleton tower to restaurant
            if (restaurantPath.containsKey(restaurant)) {
                List<PathNode> path = restaurantPath.get(restaurant);
                // check if theres enough moves left for the delivery and add if there is one
                if (2*path.size() + 2 + moveCount <= MAX_MOVES){
                    deliveryPath = addingFlightPath(current, path, restaurants, deliveryPath);
                }
            }
        }
        System.out.println("Total orders processed: " + deliveryPath.size());
        return deliveryPath;
    }

    public HashMap<Order, List<PathNode>> deliverOrders(String orderDate) {
        // validate the orders and rearrange them to ascending by distance to appleton tower
        List<Order> orders = validator.getOrders(server, orderDate);
        PriorityQueue<Order> sortedOrders = getSortedOrders(restaurants, orders.toArray(new Order[0]), restaurantPath);
        System.out.println("Total orders taken: " + sortedOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        HashMap<Order, List<PathNode>> deliveryPath = new HashMap<>();

        while (!sortedOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedOrders.poll();
            if (current == null) break;
            if (!validator.isValidOrder(restaurants, current)) {
                deliveryPath.put(current, null);
                continue;
            }
            Restaurant restaurant = current.getOrdersRestaurant(restaurants);

            System.out.println("current moves taken: " + moveCount);
            // generate path from appleton tower to restaurant
            if (restaurantPath.containsKey(restaurant)) {
                List<PathNode> path = restaurantPath.get(restaurant);
                System.out.println("current path moves " + (2*path.size()+2));
                // check if theres enough moves left for the delivery and add if there is one
                if (2*path.size() + 2 + moveCount <= MAX_MOVES){
                    deliveryPath = addingFlightPath(current, path, restaurants, deliveryPath);
                } else {
                    deliveryPath.put(current, null);
                }
            }
        }
        System.out.println("Total orders processed: " + deliveryPath.size());
        return deliveryPath;
    }

    // getter
    public List<PathNode> getTotalFlightPath() {
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
    public HashMap<Order, List<PathNode>> addingFlightPath(Order order, List<PathNode> path, Restaurant[] restaurants, HashMap<Order, List<PathNode>> deliveryPath) {
        List<PathNode> temp = new ArrayList<>();
        List<PathNode> result = new ArrayList<>();
        temp.addAll(path);
        // add the path from appleton to the restaurant
        totalFlightPath.addAll(temp);
        result.addAll(temp);
        // hover when drone picks up order from restaurant
        PathNode collectOrder = temp.get(temp.size()-1);
        collectOrder.setAngleToPrevious(CompassDirection.NULL.getAngle());
        totalFlightPath.add(collectOrder);
        result.add(collectOrder);
        // reverse the current path to get path back to appleton
        Collections.reverse(temp);
        temp.forEach(p -> p.setAngleToPrevious(CompassDirection.getOppositeDirection(p.getAngleToPrevious())));
        // add the path from restaurant back to appleton
        totalFlightPath.addAll(temp);
        result.addAll(temp);
        // hover when user picks up order
        PathNode deliverOrder = temp.get(temp.size()-1);
        deliverOrder.setAngleToPrevious(CompassDirection.NULL.getAngle());
        totalFlightPath.add(deliverOrder);
        result.add(deliverOrder);
        order.setStatus(OrderOutcome.DELIVERED);
        moveCount += 2* temp.size() + 2;
        deliveryPath.put(order, result);

        return deliveryPath;
    }
}
