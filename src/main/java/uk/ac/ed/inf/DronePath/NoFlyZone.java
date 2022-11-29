package uk.ac.ed.inf.DronePath;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;

public class NoFlyZone {
    private String name;
    private double[][] coordinates;

    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") double[][] coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public static NoFlyZone[] getNoFlyZones(RestClient server) {
        NoFlyZone[] noFlyZones = null;
        noFlyZones = (NoFlyZone[]) server.deserialize("noFlyZones", NoFlyZone[].class);
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
//        return Polygon.fromLngLats(Arrays.asList(Arrays.asList(result)));
    }
}
