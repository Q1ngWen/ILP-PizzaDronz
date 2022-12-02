package uk.ac.ed.inf;

import uk.ac.ed.inf.DronePath.Drone;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.OutFiles.FileWriting;
import uk.ac.ed.inf.OutFiles.Output;
import java.util.List;

/**
 * The main class that is the entry point for executing the drone system.
 */
public class App {

    /**
     * Java main method that allows system to be called.
     * @param args 2 arguments that are input into the command line when system is called
     *             parameter 1: date in YYYY-MM-DD format
     *             Parameter 2: Base address of the ILP REST service. Eg. https://ilp-rest.azurewebsites.net/
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("You must supply two parameters.");
            System.err.println("Parameter 1: date in YYYY-MM-DD format");
            System.err.println("Parameter 2: Base address of the ILP REST service \n " +
                    "Eg. https://ilp-rest.azurewebsites.net/");
            System.exit(1);
        }
        String date = args[0];
        String baseUrl = args[1];

        RestClient server = new RestClient(baseUrl, date);
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
