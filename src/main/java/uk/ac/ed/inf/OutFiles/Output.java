package uk.ac.ed.inf.OutFiles;

import uk.ac.ed.inf.DronePath.Drone;
import uk.ac.ed.inf.DronePath.FlightPath;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Deliveries;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Output} contains all the data necessary to produce the output files desired.
 */
public class Output {
    private Order order;
    private List<PathNode> flightPath;

    /**
     * @param order      current {@link Order} and it's details.
     * @param flightPath List of {@link PathNode} making up the path travelled when the order is processed.
     */
    public Output(Order order, List<PathNode> flightPath) {
        this.order = order;
        this.flightPath = flightPath;
    }

    // -- functions that convert the output information into the desired class for outfiles--

    /**
     * @param outputList List of {@link Output} from the orders processed in a day.
     * @return List of {@link Deliveries} to be converted and writen to a file.
     */
    public static List<Deliveries> getDeliveries(List<Output> outputList) {
        List<Deliveries> deliveries = new ArrayList<>();
        for (Output output : outputList) {
            Order order = output.getOrder();
            Deliveries orderDelivery = new Deliveries(order.getOrderNo(), order.getOutcome().toString(),
                    order.getPriceTotalInPence());
            deliveries.add(orderDelivery);
        }
        return deliveries;
    }

    /**
     * @param outputList      List of {@link Output} from the orders processed in a day.
     * @param baseTickElapsed The {@link Drone} initiation start time.
     * @return List of {@link FlightPath} to be converted and writen to a file.
     */
    public static List<FlightPath> getFlightPaths(List<Output> outputList, long baseTickElapsed) {
        List<FlightPath> flightPaths = new ArrayList<>();
        int deliveredOrders = 0;
        for (Output output : outputList) {
            Order order = output.getOrder();

            // if order wasn't delivered, no flight path to be added so continue to next iteration
            if (order.getOutcome() != OrderOutcome.DELIVERED) continue;
            deliveredOrders += 1;

            // initialise the path travelled and the turning point ( point close to restaurant)
            List<PathNode> path = output.getFlightPath();
            int turnPoint = path.size() / 2;
            for (int i = 0; i < turnPoint - 1; i++) {
                LngLat from = path.get(i).getValue();
                LngLat to = path.get(i + 1).getValue();
                FlightPath flightPath = new FlightPath(
                        order.getOrderNo(), from.lng(), from.lat(), from.getDirectionTo(to),
                        to.lng(), to.lat(), System.nanoTime() - baseTickElapsed);
                flightPaths.add(flightPath);
            }

            // add the drone hovering on the spot to pick up the order
            LngLat restaurant = path.get(turnPoint).getValue();
            FlightPath pickUp = new FlightPath(order.getOrderNo(), restaurant.lng(), restaurant.lat(), 0,
                    restaurant.lng(), restaurant.lat(), System.nanoTime() - baseTickElapsed);
            flightPaths.add(pickUp);

            for (int i = turnPoint; i < path.size() - 1; i++) {
                LngLat from = path.get(i).getValue();
                LngLat to = path.get(i + 1).getValue();
                FlightPath flightPath = new FlightPath(
                        order.getOrderNo(), from.lng(), from.lat(), from.getDirectionTo(to),
                        to.lng(), to.lat(), System.nanoTime() - baseTickElapsed);
                flightPaths.add(flightPath);
            }
            LngLat appleton = path.get(0).getValue();
            FlightPath dropOff = new FlightPath(order.getOrderNo(), appleton.lng(), appleton.lat(), 0,
                    appleton.lng(), appleton.lat(), System.nanoTime() - baseTickElapsed);
            flightPaths.add(dropOff);
        }
        System.out.println("Total orders delivered: " + deliveredOrders);
        return flightPaths;
    }

    // getters

    /**
     * @return List of {@link FlightPath}
     */
    public List<PathNode> getFlightPath() {
        return this.flightPath;
    }

    /**
     * @return {@link Order} details of current Output.
     */
    public Order getOrder() {
        return this.order;
    }
}
