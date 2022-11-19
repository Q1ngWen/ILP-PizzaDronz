package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mapbox.geojson.*;

import java.awt.*;
import java.net.URL;

public class NoFlyZone {
    private String name;
    private double[][] coordinates;

    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") double[][] coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public static NoFlyZone[] getNoFlyZones(RestClient server) {
        NoFlyZone[] noFlyZones = null;
        try {
            URL noFlyZonesUrl = new URL(server.getBaseUrl() + "noFlyZones");
            noFlyZones = new ObjectMapper().readValue(noFlyZonesUrl, NoFlyZone[].class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return noFlyZones;
    }

    public String getName() {
        return name;
    }

    public LngLat[] getCoordinates() {
        LngLat[] result = new LngLat[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = new LngLat(coordinates[i][0], coordinates[i][1]);
        }
        return result;
//        return Polygon.fromLngLats(result);
    }

//    public Polygon test() {
////        Point coordinate = Point.fromLngLat(-1,1);
//
//    }
}
