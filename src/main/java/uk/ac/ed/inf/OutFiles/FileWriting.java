package uk.ac.ed.inf.OutFiles;

import com.google.gson.Gson;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link FileWriting} class helps store functions that serialises and writes data produced to proper strings in files.
 */
public class FileWriting {
    private List<PathNode> totalFlightPath;
    private String date;

    /**
     * @param totalFlightPath List of {@link PathNode} containing all paths travelled when delivering orders for the day.
     * @param date            {@link String} date entered when system was accessed.
     */
    public FileWriting(List<PathNode> totalFlightPath, String date) {
        this.totalFlightPath = totalFlightPath;
        this.date = date;
    }

    /**
     * @return {@link String} of GeoJSON map which can be rendered <a href="https://geojson.io">here</a>.
     */
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

    /**
     * @param list List of object T to be parsed.
     * @param <T>  Generic type class that is parameterised over different types.
     * @return List of objects in JSON {@link String} format.
     */
    public <T> String serialise(List<T> list) {
        Gson gson = new Gson();
        String result = "[";
        for (T l : list) {
            result += gson.toJson(l) + ",";
        }
        return result + "]";
    }

    /**
     * Function produces a file from the JSON input.
     *
     * @param json     JSON {@link String} to be writen to a file.
     * @param filename {@link String} name of the file.
     */
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
