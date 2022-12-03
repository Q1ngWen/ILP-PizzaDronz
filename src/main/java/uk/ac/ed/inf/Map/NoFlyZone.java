package uk.ac.ed.inf.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;


/**
 * {@link NoFlyZone} consists of populated areas in central Edinburgh that the drone must avoid flying over.
 */
public class NoFlyZone {
    private final String name;
    private final double[][] coordinates;

    /**
     * @param name        {@link String} name of the {@link NoFlyZone}.
     * @param coordinates Nested list of the coordinates of the {@link NoFlyZone}.
     */
    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") double[][] coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    /**
     * @param server {@link RestClient} allows data to be retrieved from the ILP REST Server.
     * @return Returns a list of {@link NoFlyZone}.
     */
    public static NoFlyZone[] getNoFlyZones(RestClient server) {
        NoFlyZone[] noFlyZones;
        noFlyZones = server.deserialize("noFlyZones", NoFlyZone[].class);
        return noFlyZones;
    }

    /**
     * @return Returns a list of {@link LngLat} coordinates of the {@link NoFlyZone}.
     */
    public LngLat[] getCoordinates() {
        LngLat[] result = new LngLat[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = new LngLat(coordinates[i][0], coordinates[i][1]);
        }
        return result;
    }
}
