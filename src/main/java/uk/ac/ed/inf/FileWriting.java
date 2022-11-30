package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.DronePath.CompassDirection;
import uk.ac.ed.inf.DronePath.FlightPath;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Deliveries;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderOutcome;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FileWriting {
    private List<PathNode> totalFlightPath;
    private HashMap<Order, List<PathNode>> deliveryPath;
    private String date;

    public FileWriting(List<PathNode> totalFlightPath, HashMap<Order, List<PathNode>> deliveryPath, String date) {
        this.deliveryPath = deliveryPath;
        this.totalFlightPath = totalFlightPath;
        this.date = date;
    }


    public String getDronePathJson() {
        // converting the path to geoJSON Points
        List<Point> finalPath = new ArrayList<>();
        for (int i = 0; i < totalFlightPath.size(); i++) {
            LngLat coordinate = totalFlightPath.get(i).getValue();
            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
        }

        // parsing the path to geoJSON format
        LineString linePath = LineString.fromLngLats(finalPath);
        Geometry geometry = (Geometry) linePath;
        Feature feature = Feature.fromGeometry(linePath);
        FeatureCollection fc = FeatureCollection.fromFeature(feature);
        String result = fc.toJson();
        return result;
    }

    public String getDeliveriesJson() {
        // converting the delivery path to list of deliveries
        List<Deliveries> deliveries = new ArrayList<>();
        Set<Order> orderList = deliveryPath.keySet();
        for (Order order : orderList) {
            Deliveries orderDelivery = new Deliveries(order.getOrderNo(), order.getOutcome(), order.getPriceTotalInPence());
            deliveries.add(orderDelivery);
        }

        // parsing the path to geoJSON format
        Gson gson = new Gson();
        String result = "[";
        for (Deliveries d : deliveries) {
            result += gson.toJson(d) + ",";
        }
        return result+"]";
    }

    public String getFlightPathJson() {
        // converting the path generated for each orders to a list of flightpaths
        List<FlightPath> flightPaths = new ArrayList<>();
        Set<Order> orderList = deliveryPath.keySet();
        int deliveredOrders = 0;
        for (Order order : orderList) {
            if (order.getOutcome() != OrderOutcome.DELIVERED) continue;
            deliveredOrders +=1;
            List<PathNode> path = new ArrayList<>();
            path = deliveryPath.get(order);
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
                        to.lng(), to.lat(), 0);
                flightPaths.add(flightPath);
            }
        }
        System.out.println(deliveredOrders);
        System.out.println(orderList.size());

        // parsing the path to geoJSON format
        Gson gson = new Gson();
        String result = "[";
        for (FlightPath p : flightPaths) {
            result += gson.toJson(p) +",";
        }
        return result+"]";
    }

    public void writeToFile(String json, String filename) {
        File file = new File(filename + "-" + date);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("File " + filename + "-" + date + " was not able to be created.");
            e.printStackTrace();
        }

    }
}
