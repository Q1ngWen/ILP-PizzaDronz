package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.DronePath.*;
import uk.ac.ed.inf.OutFiles.FileWriting;
import uk.ac.ed.inf.OutFiles.Output;

import java.util.List;

public class PerformanceTest extends TestCase {
    private final String baseUrl = "https://ilp-rest.azurewebsites.net/";

    // test case: verifying response time of system is under 60 seconds
    public void testResponseTime() {
        String date = "2023-04-04";

        // system's execution start time is recorded
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

        // system's execution end time is recorded
        long finish = System.currentTimeMillis();
        // system's total execution time is calculated
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);
        assertTrue(timeElapsed <= 60000);
    }
}
