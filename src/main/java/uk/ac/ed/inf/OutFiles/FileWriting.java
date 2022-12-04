package uk.ac.ed.inf.OutFiles;

import com.google.gson.Gson;
import com.mapbox.geojson.*;
import uk.ac.ed.inf.Map.LngLat;
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
    private final List<PathNode> totalFlightPath;
    private final String date;

    /**
     * @param totalFlightPath List of {@link PathNode} containing all paths travelled when delivering orders for the day.
     * @param date            {@link String} date entered when system was accessed.
     */
    public FileWriting(List<PathNode> totalFlightPath, String date) {
        this.totalFlightPath = totalFlightPath;
        this.date = date;
    }

    /**
     * Function serialises total flight path generated into a geoJson string
     *
     * @return {@link String} of GeoJSON map which can be rendered <a href="https://geojson.io">here</a>.
     */
    public String getDronePathJson() {
        // converting the path to geoJSON Points
        List<Point> finalPath = new ArrayList<>();
        for (PathNode pathNode : totalFlightPath) {
            LngLat coordinate = pathNode.getValue();
            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
        }

        // parsing the path to geoJSON format
        LineString linePath = LineString.fromLngLats(finalPath);
        Feature feature = Feature.fromGeometry(linePath);
        FeatureCollection fc = FeatureCollection.fromFeature(feature);
        return fc.toJson();
    }

    /**
     * Function converts any Java Object T into a JSON string
     *
     * @param list List of object T to be parsed.
     * @param <T>  Generic type class that is parameterised over different types.
     * @return List of objects in JSON {@link String} format.
     */
    public <T> String serialise(List<T> list) {
        Gson gson = new Gson();
        StringBuilder result = new StringBuilder("[");
        for (T l : list) {
            result.append(gson.toJson(l)).append(",");
        }
        return result.substring(0, result.length() - 1) + "]";
    }

    /**
     * Function produces a file from the JSON input.
     *
     * @param json     JSON {@link String} to be written to a file.
     * @param filename {@link String} name of the file.
     */
    public void writeToFile(String json, String filename, String fileType) {
        File file = new File("./resultfiles/" + filename + "-" + date + "." + fileType);
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
