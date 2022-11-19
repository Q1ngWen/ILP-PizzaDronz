package uk.ac.ed.inf;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RestClient<T> {

    private static final String DEFAULT_ENDPOINT = "https://ilp-rest.azurewebsites.net/";
    private static RestClient<RestClient> dataInstance = null;
    private URL baseUrl;

    public RestClient(String baseUrl) {
        try {
            this.baseUrl = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T deserialize(String endpoint, Class<T> tClass) {
        URL finalUrl = null;
        T response = null;

        try {
            if (baseUrl.toString() == "" || baseUrl == null) {
                baseUrl = new URL(DEFAULT_ENDPOINT);
            }
            if (!baseUrl.toString().endsWith("/")) {
                baseUrl = new URL(baseUrl.toString() + "/");
            }

            finalUrl = new URL(baseUrl.toString() + endpoint);
        } catch (MalformedURLException e) {
            System.err.println("Url is invalid: " + baseUrl + endpoint);
        }

        try {
            response = new ObjectMapper().readValue(finalUrl, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    // getters

    public URL getBaseUrl() {
        return baseUrl;
    }

//    public static RestClient getRestClientInstance() throws MalformedURLException {
//        synchronized (RestClient.class) {
//            if (dataInstance == null) {
//                dataInstance = new RestClient(baseUrl);
//            }
//        }
//
//        try {
//            if (baseUrl == "" || baseUrl == null) {
//                baseUrl = DEFAULT_ENDPOINT;
//            }
//            if (!baseUrl.endsWith("/")){
//                baseUrl += "/";
//            }
//            dataInstance.baseUrl = baseUrl;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//
//        return dataInstance;
//    }
}
