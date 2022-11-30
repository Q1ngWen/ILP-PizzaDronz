package uk.ac.ed.inf;

import uk.ac.ed.inf.DronePath.Drone;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Order;

import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Clock clock = Clock.systemDefaultZone();
        long start = clock.millis();
        System.out.println(start);

//        String date = args[0];
//        String baseUrl = args[1];
        String date = "2023-01-01";
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        System.out.println(date);
        System.out.println(baseUrl);

        RestClient server = new RestClient<>(baseUrl);
        Drone drone = new Drone(server, clock);
        HashMap<Order, List<PathNode>> deliveryOrders = drone.deliverOrders("2023-03-01");
        List<PathNode> totalFlightPath = drone.getTotalFlightPath();
        System.out.println(clock.millis());
        System.out.println();
        FileWriting fileWriting = new FileWriting(totalFlightPath, deliveryOrders, date);

        String droneGeoJson= fileWriting.getDronePathJson();
        String flightpathJson = fileWriting.getFlightPathJson();
        String deliveriesJson = fileWriting.getDeliveriesJson();
        fileWriting.writeToFile(droneGeoJson, "drone");
        fileWriting.writeToFile(flightpathJson, "flightpath");
        fileWriting.writeToFile(deliveriesJson, "deliveries");

        long end = clock.millis();
        System.out.println(end);
        System.out.println("total time elapsed: " + (end-start));
    }
}
