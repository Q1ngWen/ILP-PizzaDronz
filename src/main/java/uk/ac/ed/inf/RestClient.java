package uk.ac.ed.inf;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;

public class RestClient<T> {
    private URL baseUrl;

    public RestClient(String baseUrl, String date) {
        try {
            // check if the date entered is valid
            try {
                LocalDate.parse(date);
            } catch (Exception e) {
                System.err.println("Date String provided is in the wrong format or invalid: " + date);
                System.err.println("Please provide a date in YYYY-MM-DD format.");
                System.exit(1);
            }

            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            this.baseUrl = new URL(baseUrl);
            URL orders = new URL(baseUrl + "orders/" + date);
        } catch (MalformedURLException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        }
    }

    public <T> T deserialize(String endpoint, Class<T> tClass) {
        URL finalUrl = null;
        T response = null;

        try {
            finalUrl = new URL(baseUrl.toString() + endpoint);
        } catch (MalformedURLException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        }

        try {
            response = new ObjectMapper().readValue(finalUrl, tClass);
        } catch (IOException e) {
            System.err.println("Server was unresponsive or URL entered was invalid, please try again.");
            System.exit(2);
        }
        return response;
    }
}
