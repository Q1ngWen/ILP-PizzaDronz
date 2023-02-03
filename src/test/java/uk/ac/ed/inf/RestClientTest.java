package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.After;
import uk.ac.ed.inf.Map.CentralArea;
import uk.ac.ed.inf.Map.LngLat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RestClientTest extends TestCase {
    private String url;
    private String date;
    private String errorMessage;
    private RestClient server;
    private final ByteArrayOutputStream errorContent = new ByteArrayOutputStream();
    private final PrintStream originalError = System.err;

    public void setUp() throws Exception {
        super.setUp();
        date = "2023-01-01";
        errorMessage = "Url provided is invalid: ";
        System.setErr(new PrintStream(errorContent));
    }

    // test suite 1: checking the validation of the base URL for the string input
    // url validity: https://www.w3.org/TR/2011/WD-html5-20110525/urls.html
    public void testUrlValidation() {
        // test case 1: input is a valid URI
        url = "https://ilp-rest.azurewebsites.net/centralarea";
        assertNotNull(new RestClient(url, date));
        assertFalse(errorContent.toString().contains(errorMessage + url));
        url = "https://ilp-rest.azurewebsites.net/orders/2023-01-21";
        assertNotNull(new RestClient(url, date));
        assertFalse(errorContent.toString().contains(errorMessage + url));

        // test case 2: input is an empty string
        url = "";
        new RestClient(url, date);
        assertTrue(errorContent.toString().contains(errorMessage + url));

        // test case 3: input contains white spaces around the URL
        url = "   https://ilp-rest.azurewebsites.net/centralarea  ";
        assertNotNull(new RestClient(url, date));
        assertFalse(errorContent.toString().contains(errorMessage + url));

        // test case 4: input is an invalid URI
        url = "google/com.com/https://";
        new RestClient(url, date);
        assertTrue(errorContent.toString().contains(errorMessage + url));

        // test case 5: input is a valid IRI reference
        url = "https://ilp-rest.azurewebsites.net/bounding-box.geojson";
        assertNotNull(new RestClient(url, date));
        assertFalse(errorContent.toString().contains(errorMessage + url));
    }

    // test suite 2: checking the data fetched from the REST server, assuming the url has been validated
    public void testDeserialize() {
        // test case 1: server was responsive and data can be fetched from the server
        url = "https://ilp-rest.azurewebsites.net/";
        server = new RestClient(url, date);
        CentralArea centralArea = new CentralArea();
        centralArea.setCentralAreaCoordinates(server);
        LngLat[] centralAreaPoints = centralArea.getCoordinates();

        assertEquals(centralAreaPoints[0].lng(), -3.192473);
        assertEquals(centralAreaPoints[0].lat(), 55.946233);
        assertEquals(centralAreaPoints[2].lng(), -3.184319);
        assertEquals(centralAreaPoints[2].lat(), 55.942617);

        // test case 2: server was unresponsive
        url = "https://stefanbirkner.github.com/";
        server = new RestClient(url, date);
        String[] unresponsive = server.deserialize("system-rules/", String[].class);
        assertTrue(errorContent.toString().contains("Server was unresponsive"));

        // test case 3: no data could be fetched from the URL
        url = "https://www.google.com";
        server = new RestClient(url, date);
        String[] fetchError = server.deserialize("system-rules/", String[].class);
        assertTrue(errorContent.toString().contains("URL entered was invalid"));
    }

    @After
    public void restoreStreams() {
        System.setErr(originalError);
    }
}