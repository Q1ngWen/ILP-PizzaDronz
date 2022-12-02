package uk.ac.ed.inf.DronePath;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;


/**
 * {@link NoFlyZone} consists of populated areas in central Edinbrugh that the drone must avoid flying over.
 */
public class NoFlyZone {
    private String name;
    private double[][] coordinates;

    /**
     * @param name        {@link String} name of the {@link NoFlyZone}.
     * @param coordinates Nested list of the coordinates of all {@link NoFlyZone}.
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
        NoFlyZone[] noFlyZones = null;
        noFlyZones = (NoFlyZone[]) server.deserialize("noFlyZones", NoFlyZone[].class);
        return noFlyZones;
    }

    /**
     * @return {@link String} name of the {@link NoFlyZone}.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns a list of {@link LngLat} coordinates of all {@link NoFlyZone}.
     */
    public LngLat[] getCoordinates() {
        LngLat[] result = new LngLat[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = new LngLat(coordinates[i][0], coordinates[i][1]);
        }
        return result;
    }
}
