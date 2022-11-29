package uk.ac.ed.inf;

import com.mapbox.geojson.Point;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.DronePath.PathNode;
import uk.ac.ed.inf.Orders.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fileWriting {
    private List<PathNode> totalFlightPath;
    private HashMap<Order, List<PathNode>> deliveryPath;
    private String date;

    public fileWriting(List<PathNode> totalFlightPath, HashMap<Order, List<PathNode>> deliveryPath, String date){
        this.deliveryPath = deliveryPath;
        this.totalFlightPath = totalFlightPath;
    }

    public String getDronePathJson() {
        List<Point> finalPath = new ArrayList<>();
        for (int i = 0; i < totalFlightPath.size(); i++) {
            LngLat coordinate = totalFlightPath.get(i).getValue();
            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
        }
        return "";
    }
}
