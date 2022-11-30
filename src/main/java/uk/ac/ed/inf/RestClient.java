package uk.ac.ed.inf;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class RestClient<T> {

    private static final String DEFAULT_ENDPOINT = "https://ilp-rest.azurewebsites.net/";
    private static RestClient<RestClient> dataInstance = null;
    private URL baseUrl;
    private String urlInstance;

    public RestClient(String baseUrl) {
        if (urlValidator(baseUrl)) {
            try {
                this.baseUrl = new URL(urlInstance);
            } catch (MalformedURLException e) {
                System.err.println("Url provided is invalid: " + baseUrl);
                System.exit(2);
            }
        }
    }

    public <T> T deserialize(String endpoint, Class<T> tClass) {
        URL finalUrl = null;
        T response = null;

        try {
            if (urlValidator(baseUrl.toString() + endpoint)) {
                finalUrl = new URL(urlInstance);
            }
        } catch (MalformedURLException e) {
            System.err.println("Url is invalid: " + urlInstance);
            System.exit(2);
        }

        try {
            response = new ObjectMapper().readValue(finalUrl, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private boolean urlValidator(String url){
        try {
            new URL(url).toURI();
            if (url.equals("") || url == null) {
                url = DEFAULT_ENDPOINT;
            }
            if (!url.endsWith("/")) {
                url += "/";
            }
            this.urlInstance = url;
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    public void download(String endpoint, String filename) {
        URL finalUrl = null;

        try {
            if (urlValidator(baseUrl.toString() + endpoint)) {
                finalUrl = new URL(urlInstance);
            }
        } catch (MalformedURLException e) {
            System.err.println("URL is invalid: " + urlInstance);
            System.exit(2);
        }

        try (BufferedInputStream in = new BufferedInputStream(finalUrl.openStream());
                FileOutputStream fileOutputStream =
                    new FileOutputStream(endpoint, false)) {
            byte[] dataBuffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            System.out.println("File was written: " + endpoint);
        } catch (IOException e) {
            System.err.format("Error loading file: %s from %s -> %s", endpoint, finalUrl, e);
        }
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
