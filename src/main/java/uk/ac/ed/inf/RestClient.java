package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;


/**
 * This Class acts as a REST API to interact with the REST Server from the {@link URL} and {@link String} date provided.
 */
public class RestClient {
    private URL baseUrl;

    public RestClient(String baseUrl) {
        try {
            //
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Url provided is invalid: " + baseUrl);
            System.exit(2);
        }
    }

    /**
     * Constructor for {@link RestClient} class that validates the inputs and terminates the system if invalid.
     *
     * @param baseUrl Base address of the ILP REST service input through the {@link App} class.
     * @param date    Date of orders to be viewed and delivered input through the {@link App} class.
     * @see RestClient#RestClient(String)
     */
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

    /**
     * Converts a JSON string into a Java Object.
     *
     * @param endpoint REST-Service Endpoints to dynamically retrieve data
     * @param tClass   General object class to store retrieved data.
     * @param <T>      Generic type class that is parameterised over different types.
     * @return Returns a list of data classes.
     */
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
