package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * {@link CentralAreaData} helps fetch the central area corner coordinates and allows {@link  LngLat} access
 * for calculations with a Singleton access pattern.
 */

public class CentralAreaData {

    // static variable reference of type CentralAreaData
    private static CentralAreaData central_area_instance = null;

    // Declaring the corner coordinates of the central area (in anti-clockwise)
    private CentralDataResponse[] coordinates;

    // Private constructor restricted to this class (Singleton access pattern)
    private CentralAreaData() {
        try {
            String baseUrl = "https://ilp-rest.azurewebsites.net/";
            String echoBasis = "";
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            // call the test endpoint and pass in some test data which will be echoed
            URL url = new URL(baseUrl + "centralArea/" + echoBasis);

            // the Jackson JSON library provides helper methods which can directly
            // take a URL, perform the GET request convert the result to the specified class
            coordinates = new ObjectMapper().readValue(url, CentralDataResponse[].class);

            // error checking - if the JSON data is not correct, we throw an exception
            if (!coordinates[0].name().endsWith(echoBasis)) {
                throw new RuntimeException("Wrong echo returned");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows other classes to call an instance of the original {@link CentralAreaData}
     * @return An instance of the {@link CentralAreaData}.
     */
    public static CentralAreaData getCentral_area_instance() {
        if (central_area_instance == null) {
            central_area_instance = new CentralAreaData();
        }
        return central_area_instance;
    }

    /**
     * @return Array of central area corner coordinates.
     */
    public CentralDataResponse[] getCoordinates() {
        return coordinates;
    }
}
