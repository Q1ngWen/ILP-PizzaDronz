package uk.ac.ed.inf.OutFiles;

import uk.ac.ed.inf.DronePath.FlightPath;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Deliveries;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Output {
    private Order order;
    private long ticksElasped;
    private List<PathNode> flightPath;

    public Output(Order order, long ticksElasped, List<PathNode> flightPath) {
        this.order = order;
        this.ticksElasped = ticksElasped;
        this.flightPath = flightPath;
    }

    // Functions for information of files to be produced
    public static List<Deliveries> getDeliveries(List<Output> outputList) {
        List<Deliveries> deliveries = new ArrayList<>();
        for (Output output : outputList) {
            Order order = output.getOrder();
            Deliveries orderDelivery = new Deliveries(order.getOrderNo(), order.getOutcome(), order.getPriceTotalInPence());
            deliveries.add(orderDelivery);
        }
        return deliveries;
    }

    public static List<FlightPath> getFlightPaths(List<Output> outputList) {
        List<FlightPath> flightPaths = new ArrayList<>();
        int deliveredOrders = 0;
        for (Output output : outputList) {
            Order order = output.getOrder();
            if (order.getOutcome() != OrderOutcome.DELIVERED) continue;
            deliveredOrders +=1;
            List<PathNode> path = output.getFlightPath();
            for (int i = 0; i < path.size() - 1; i++) {
                LngLat from = path.get(i).getValue();
                LngLat to = path.get(i + 1).getValue();
//                if (path.get(i).getAngleToPrevious() == null) {
//                    FlightPath flightPath = new FlightPath(
//                            order.getOrderNo(), from.lng(), from.lat(), 0,
//                            to.lng(), to.lat(), 0);
//                    flightPaths.add(flightPath);
//                }
                FlightPath flightPath = new FlightPath(
                        order.getOrderNo(), from.lng(), from.lat(), path.get(i).getAngleToPrevious(),
                        to.lng(), to.lat(), (int) output.getTicksElasped());
                flightPaths.add(flightPath);
            }
        }
        System.out.println("Total orders delivered: " + deliveredOrders);
        return flightPaths;
    }

    // getters

    public List<PathNode> getFlightPath() {
        return this.flightPath;
    }

    public long getTicksElasped() {
        return this.ticksElasped;
    }

    public Order getOrder() {
        return this.order;
    }
}
