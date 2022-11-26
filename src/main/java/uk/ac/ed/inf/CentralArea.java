package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CentralArea {
    private String name;
    private double longitude;
    private double latitude;
    private CentralArea[] centralAreas;
    private LngLat[] coordinates;

    public CentralArea() {
    }

    public CentralArea(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude, @JsonProperty("latitude") double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setCentralAreaCoordinates(RestClient server) {
        centralAreas = (CentralArea[]) server.deserialize("centralArea", CentralArea[].class);
        coordinates = new LngLat[centralAreas.length];
        for (int i = 0; i < centralAreas.length; i++) {
            coordinates[i] = new LngLat(centralAreas[i].getLongitude(), centralAreas[i].getLatitude());
        }
    }

    public CentralArea[] getCentralArea() {
        return centralAreas;
    }

    // getters
    public LngLat[] getCoordinates() {
        return coordinates;
    }

//        try {
//            URL centralAreaUrl = new URL(server.getBaseUrl() + "/centralArea");
//            coordinates = new ObjectMapper().readValue(centralAreaUrl, CentralArea[].class);
//        } catch (MalformedURLException | StreamReadException e) {
//            e.printStackTrace();
//            System.exit(1);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//        return coordinates;


    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }
}
