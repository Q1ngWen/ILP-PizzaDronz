package uk.ac.ed.inf.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.RestClient;

/**
 * {@link CentralArea} class that stores data defining the polygon containing the main area of The University
 * of Edinburgh.
 */
public class CentralArea {
    private String name;
    private double longitude;
    private double latitude;
    private LngLat[] coordinates;

    public CentralArea() {
    }

    /**
     * @param name      {@link String} name of the location defining the {@link CentralArea}.
     * @param longitude The longitude of a {@link LngLat}.
     * @param latitude  The latitude of a {@link LngLat}.
     */
    public CentralArea(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude, @JsonProperty("latitude") double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Function that uses {@link RestClient#deserialize(String, Class)} to retrieve and set
     * a list of {@link CentralArea} coordinates.
     *
     * @param server An instance of {@link RestClient}.
     */
    public void setCentralAreaCoordinates(RestClient server) {
        CentralArea[] centralAreas = server.deserialize("centralArea", CentralArea[].class);
        coordinates = new LngLat[centralAreas.length];
        for (int i = 0; i < centralAreas.length; i++) {
            coordinates[i] = new LngLat(centralAreas[i].getLongitude(), centralAreas[i].getLatitude());
        }
    }


    /**
     * @return Returns the list of the {@link CentralArea} coordinates.
     */
    public LngLat[] getCoordinates() {
        return coordinates;
    }

    /**
     * @return Returns the longitude of a {@link CentralArea} coordinate.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return Returns the latitude of a {@link CentralArea} coordinate.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return Returns the name of a {@link CentralArea} coordinate.
     */
    public String getName() {
        return name;
    }
}
