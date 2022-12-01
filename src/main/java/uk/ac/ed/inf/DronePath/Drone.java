package uk.ac.ed.inf.DronePath;

import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.OutFiles.Output;
import uk.ac.ed.inf.Restaurants.Restaurant;

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
    private long ticksSinceStartOfCalculation;

    public Drone(RestClient server) {
        // setting up and fetching all JSON data from REST server
        this.server = server;
        this.ticksSinceStartOfCalculation = System.nanoTime();
        System.out.println(ticksSinceStartOfCalculation);
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

    public List<Output> deliverOrders() {
        // validate the orders and rearrange them to ascending by distance to appleton tower
        List<Order> orders = validator.getOrders(server);
        PriorityQueue<Order> sortedOrders = getSortedOrders(restaurants, orders.toArray(new Order[0]), restaurantPath);
        System.out.println("Total orders taken: " + sortedOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        List<Output> outputList = new ArrayList<>();

        while (!sortedOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedOrders.poll();
            if (current == null) break;
            if (!validator.isValidOrder(restaurants, current)) {
                outputList.add(generateOutput(current, null));
                continue;
            }
            Restaurant restaurant = current.getOrdersRestaurant(restaurants);

            System.out.println("current moves taken: " + moveCount);
            // generate path from appleton tower to restaurant
            if (restaurantPath.containsKey(restaurant)) {
                List<PathNode> path = restaurantPath.get(restaurant);
                // check if theres enough moves left for the delivery and add if there is one
                if (2*path.size() + 2 + moveCount <= MAX_MOVES){
                    outputList.add(generateOutput(current, path));
                } else {
                    outputList.add(generateOutput(current, null));
                }
            }
        }
        System.out.println("Total orders processed: " + outputList.size());
        return outputList;
    }

    public List<Output> deliverOrders(String orderDate) {
        // validate the orders and rearrange them to ascending by distance to appleton tower
        List<Order> orders = validator.getOrders(server, orderDate);
        PriorityQueue<Order> sortedOrders = getSortedOrders(restaurants, orders.toArray(new Order[0]), restaurantPath);
        System.out.println("Total orders taken: " + sortedOrders.size());

        // loop through list of orders till either order list is empty or max number of moves reached
        // generate path and add to list of path nodes
        List<Output> outputList = new ArrayList<>();

        while (!sortedOrders.isEmpty() || moveCount <= MAX_MOVES) {
            Order current = sortedOrders.poll();
            if (current == null) break;
            if (!validator.isValidOrder(restaurants, current)) {
                outputList.add(generateOutput(current, null));
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
                    outputList.add(generateOutput(current, path));
                } else {
                    outputList.add(generateOutput(current, null));
                }
            }
        }
        System.out.println("Total orders processed: " + outputList.size());
        return outputList;
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

    // function that adds to total flight path and generates the output for an order
    public Output generateOutput(Order order, List<PathNode> path) {
        List<PathNode> result = null;
        if (path != null) {
            List<PathNode> temp = new ArrayList<>();
            result = new ArrayList<>();
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
            moveCount += 2* temp.size() + 2;

            // update status of order
            order.setOutcome(OrderOutcome.DELIVERED);
        }
        
        // create output
        long currentTicks = System.nanoTime() - this.ticksSinceStartOfCalculation;
        Output output = new Output(order, currentTicks, result);

        return output;
    }
}
