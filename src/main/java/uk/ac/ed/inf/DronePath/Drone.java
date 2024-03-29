package uk.ac.ed.inf.DronePath;

import uk.ac.ed.inf.*;
import uk.ac.ed.inf.Map.CentralArea;
import uk.ac.ed.inf.Map.LngLat;
import uk.ac.ed.inf.Map.NoFlyZone;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.OutFiles.Output;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.*;

/**
 * {@link Drone} class is the body of the system and brings together all the functionalities. It is a virtual
 * representation of a drone that also simulates its limitations. It generates an optimal path and traverses
 * through Edinburgh city to deliver pizza orders.
 */
public class Drone {
    private static final int MAX_MOVES = 2000;
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private final RestClient server;
    private int moveCount = 0;
    private final Restaurant[] restaurants;

    private final OrderValidator validator;
    private final HashMap<Restaurant, List<PathNode>> restaurantPath;
    private final List<PathNode> totalFlightPath;
    private final long baseTickElapsed;

    /**
     * Constructor that initialises all data required to simulate Edinburgh City and a drone.
     * This includes: {@link CentralArea}, {@link NoFlyZone}, {@link Restaurant}, {@link OrderValidator}, etc.
     *
     * @param server {@link RestClient} allows data to be retrieved from the ILP REST Server.
     */
    public Drone(RestClient server) {
        // setting up and fetching all JSON data from REST server
        this.server = server;
        this.baseTickElapsed = System.nanoTime();
        CentralArea centralAreaInstance = new CentralArea();
        centralAreaInstance.setCentralAreaCoordinates(server);
        NoFlyZone[] noFlyZones = NoFlyZone.getNoFlyZones(server);
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator(restaurants);
        PathGenerator pathGenerator = new PathGenerator();
        totalFlightPath = new ArrayList<>();

        // initialise the path to the restaurants and pair them to respective restaurants
        restaurantPath = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            PathNode appletonTower = new PathNode(APPLETON_TOWER);
            PathNode restaurantNode = new PathNode(restaurant.getCoordinate());
            PathNode endNode = pathGenerator.AStarSearch(noFlyZones, centralAreaInstance, appletonTower, restaurantNode);
            List<PathNode> path = pathGenerator.getFlightPath(endNode);
            restaurantPath.put(restaurant, path);
        }
    }

    /**
     * Function validates each order and processes its outcome. {@link Order} details, time taken to calculate
     * the path, and the order's delivery path are stored to a list of {@link Output}.
     *
     * @param orderDate {@link String} date to get a specific day's list of {@link Order}.
     * @return Returns a list of {@link Output} consisting of the outcomes of a processed {@link Order}.
     */
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
            // checks if the order is valid and if the drone has enough moves left to deliver the order
            // then add the results of the order delivered into the output list
            if (restaurantPath.containsKey(restaurant)) {
                List<PathNode> path = restaurantPath.get(restaurant);
                // checking if it's a valid move
                if (2 * path.size() + moveCount <= MAX_MOVES) {
                    outputList.add(generateOutput(current, path));
                } else {
                    // orders not delivered are given a null path
                    outputList.add(generateOutput(current, null));
                }
            }
        }
        System.out.println("Total orders processed: " + outputList.size());
        return outputList;
    }

    /**
     * Getter function for total flight paths travelled
     *
     * @return Returns a list of {@link PathNode} consisting of all paths taken when delivering {@link Order}.
     */
    public List<PathNode> getTotalFlightPath() {
        return totalFlightPath;
    }

    // -- helper functions for delivering orders --

    /**
     * Helper function for sorting a list of orders
     *
     * @param restaurants    List of {@link Restaurant} from the ILP REST server.
     * @param orders         List of {@link Order} from the ILP REST server.
     * @param restaurantPath {@link HashMap} of {@link Restaurant} paired with the list of {@link PathNode} from
     *                       Appleton Tower to the restaurant.
     * @return Returns a priority queue which sorts {@link Order} according to the number of moves required
     * for the {@link Drone} to deliver the orders.
     */
    public PriorityQueue<Order> getSortedOrders(Restaurant[] restaurants, Order[] orders, HashMap<Restaurant, List<PathNode>> restaurantPath) {
        PriorityQueue<Order> orderPriorityQueue = new PriorityQueue<>((o1, o2) -> {
            double delivery1 = restaurantPath.get(o1.getOrdersRestaurant(restaurants)).size();
            double delivery2 = restaurantPath.get(o2.getOrdersRestaurant(restaurants)).size();
            if (delivery1 > delivery2) {
                return 1;
            } else if (delivery1 < delivery2) {
                return -1;
            } else return 0;
        });
        Collections.addAll(orderPriorityQueue, orders);
        return orderPriorityQueue;
    }

    /**
     * Helper function that generates the output for an order and adds to total flight path
     *
     * @param order {@link Order} to be processed.
     * @param path  List of {@link PathNode} from Appleton Tower to the order's {@link Restaurant}.
     * @return {@link Output} with the outcome of the {@link Order} processed.
     */
    public Output generateOutput(Order order, List<PathNode> path) {
        List<PathNode> result = null;
        if (path != null) {
            List<PathNode> temp = new ArrayList<>(path);
            // add the path from appleton to the restaurant
            result = new ArrayList<>(temp);
            totalFlightPath.addAll(temp);
            // reverse the path to get the path back to appleton
            Collections.reverse(temp);
            // add the reversed path to appleton into the results
            result.addAll(temp);
            totalFlightPath.addAll(temp);
            moveCount += 2 * temp.size();

            // update status of order
            order.setOutcome(OrderOutcome.DELIVERED);
        }

        // create output

        return new Output(order, result);
    }

    /**
     * @return Returns the {@link Drone} initiation start time.
     */
    public long getBaseTickElapsed() {
        return baseTickElapsed;
    }
}
