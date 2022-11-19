package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RestClient {

    private static final String DEFAULT_ENDPOINT = "https://ilp-rest.azurewebsites.net/";
    private static RestClient dataInstance = null;
    private String baseUrl;

    public RestClient() {}

    public static RestClient getRestClientInstance(String baseUrl) {
        synchronized (RestClient.class) {
            if (dataInstance == null) {
                dataInstance = new RestClient();
            }
        }

        try {
            URL test = new URL(baseUrl + "test/hello world");
            if (baseUrl == "" || baseUrl == null) {
                baseUrl = DEFAULT_ENDPOINT;
            }
            if (!baseUrl.endsWith("/")){
                baseUrl += "/";
            }
            dataInstance.baseUrl = baseUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dataInstance;
    }

    // getters

    public String getBaseUrl() {
        return baseUrl;
    }
}
