package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.DronePath.*;
import uk.ac.ed.inf.OutFiles.FileWriting;
import uk.ac.ed.inf.OutFiles.Output;

import java.util.List;

public class AppTest extends TestCase {
    private final String baseUrl = "https://ilp-rest.azurewebsites.net/";

    public void testResponseTime() {
        String date = "2023-04-04";
        long start = System.currentTimeMillis();
        RestClient server = new RestClient(baseUrl, date);
        Drone drone = new Drone(server);
        List<Output> outputList = drone.deliverOrders(date);
        FileWriting fileWriting = new FileWriting(drone.getTotalFlightPath(), date);

        String droneGeoJson = fileWriting.getDronePathJson();
        String flightpathJson = fileWriting.serialise(Output.getFlightPaths(outputList, drone.getBaseTickElapsed()));
        String deliveriesJson = fileWriting.serialise(Output.getDeliveries(outputList));
        fileWriting.writeToFile(droneGeoJson, "drone", "geojson");
        fileWriting.writeToFile(flightpathJson, "flightpath", "json");
        fileWriting.writeToFile(deliveriesJson, "deliveries", "json");

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);
        assertTrue(timeElapsed <= 60000);
    }
}
