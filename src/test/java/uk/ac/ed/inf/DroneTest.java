package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DroneTest extends TestCase {
    private RestClient server = new RestClient<>("https://ilp-rest.azurewebsites.net/");
    private Drone drone = new Drone(new LngLat(-3.190, 55.96));
    private Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(server);

    public void testDeliverOrders() {
        RestClient server = new RestClient<>("https://ilp-rest.azurewebsites.net/");
        Drone drone = new Drone(new LngLat(-3.186874, 55.944494));
        List<PathNode> path = drone.deliverOrders(server, "2023-01-01");
        List<Point> finalPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            LngLat coordinate = path.get(i).getValue();
            finalPath.add(Point.fromLngLat(coordinate.lng(), coordinate.lat()));
        }

        //         -- parsing the path to geoJSON format --
//        Point appleton = Point.fromLngLat(new LngLat(-3.186874, 55.944494));
//        Point end = Point.fromLngLat(vegan.lng(), vegan.lat());
        LineString linePath = LineString.fromLngLats(finalPath);
//        Geometry g1 = (Geometry) start;
//        Geometry g2 = (Geometry) end;
        Geometry g3 = (Geometry) linePath;
        Feature[] features = new Feature[3];
//        features[0] = Feature.fromGeometry(g1);
//        features[1] = Feature.fromGeometry(g2);
        features[2] = Feature.fromGeometry(g3);
        FeatureCollection fc = FeatureCollection.fromFeatures(features);
        System.out.println(fc.toJson());
    }

    public void testTestDeliverOrders() {
    }

    public void testGetSortedOrders() {
        String pizzaItem1 = "Super Cheese";
        String pizzaItem2 = "All Shrooms";
        String pizzaItem3 = "Margarita";
        String pizzaItem4 = "Vegan Delight";
        String pizzaItem5 = "Meat Lover";
        String pizzaItem6 = "Proper Pizza";
        String pizzaItem7 = "Pineapple & Ham & Cheese";

        String[] pizzaOrder1 = {pizzaItem4};
        String[] pizzaOrder2 = {pizzaItem5, pizzaItem4, pizzaItem5, pizzaItem4};
        String[] pizzaOrder6 = {pizzaItem4, pizzaItem5};
        String[] pizzaOrder3 = {pizzaItem6};
        String[] pizzaOrder4 = {pizzaItem6, pizzaItem7, pizzaItem7};
        String[] pizzaOrder5 = {pizzaItem6, pizzaItem7};

        Order order1 = new Order("1", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 1200, pizzaOrder1);
        Order order2 = new Order("2", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 5100, pizzaOrder2);
        Order order3 = new Order("3", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 1500, pizzaOrder3);
        Order order4 = new Order("4", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 3300, pizzaOrder4);
        Order order5 = new Order("5", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 2400, pizzaOrder5);
        Order order6 = new Order("6", "2023-01-01", "Gilberto Handshoe",
                "2720038402742337", "04/28", "922", 2600, pizzaOrder6);
        Order[] orders = {order6, order4, order2, order5, order3, order1};
//        PriorityQueue<Order> sorted = drone.getSortedOrders(restaurants, orders, );



    }

    public void testIsValidFlightPath() {
    }
}