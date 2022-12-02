package uk.ac.ed.inf.OutFiles;

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
    private String date;

    public FileWriting(List<PathNode> totalFlightPath, String date) {
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

    // parses the list of objects to geoJson format
    public <T> String serialise(List<T> list) {
        Gson gson = new Gson();
        String result = "[";
        for (T l : list) {
            result += gson.toJson(l) + ",";
        }
        return result + "]";
    }

    public void writeToFile(String json, String filename) {
        File file = new File("./resultfiles/" + filename + "-" + date);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("File " + filename + "-" + date + " was not able to be created.");
            e.printStackTrace();
            System.exit(5);
        }

    }
}
