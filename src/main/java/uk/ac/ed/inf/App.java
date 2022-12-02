package uk.ac.ed.inf;

import uk.ac.ed.inf.DronePath.Drone;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.OutFiles.FileWriting;
import uk.ac.ed.inf.OutFiles.Output;
import java.util.List;

public class App {
    public static void main(String[] args) {
//        String date = args[0];
//        String baseUrl = args[1];
        String date = "2023-01-21";
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        System.out.println(date);
        System.out.println(baseUrl);

        RestClient server = new RestClient<>(baseUrl, date);
        Drone drone = new Drone(server);
        List<Output> outputList = drone.deliverOrders(date);
        List<PathNode> totalFlightPath = drone.getTotalFlightPath();
        FileWriting fileWriting = new FileWriting(totalFlightPath, date);

        String droneGeoJson = fileWriting.getDronePathJson();
        String flightpathJson = fileWriting.serialise(Output.getFlightPaths(outputList, drone.getBaseTickElapsed()));
        String deliveriesJson = fileWriting.serialise(Output.getDeliveries(outputList));
        ;
        fileWriting.writeToFile(droneGeoJson, "drone");
        fileWriting.writeToFile(flightpathJson, "flightpath");
        fileWriting.writeToFile(deliveriesJson, "deliveries");
    }
}
