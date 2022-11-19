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
    private CentralArea[] coordinates;

    public CentralArea() {
    }

    public CentralArea(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude, @JsonProperty("latitude") double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // getters
    public CentralArea[] getCoordinates(RestClient server) {
        try {
            URL centralAreaUrl = new URL(server.getBaseUrl() + "/centralArea");
            coordinates = new ObjectMapper().readValue(centralAreaUrl, CentralArea[].class);
        } catch (MalformedURLException | StreamReadException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return coordinates;
    }

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
