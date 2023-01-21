package uk.ac.ed.inf;

import junit.framework.TestCase;
import static org.junit.Assert.assertThrows;

public class RestClientTest extends TestCase {
    private String url;
    private String date;
    private Throwable exception;
    private String errorMessage;
    public void setUp() throws Exception {
        super.setUp();
        date = "2023-01-01";
        errorMessage = "Url provided is invalid: ";
    }

    // test case 1: checking the validation of the base URL for the string input
    // url validity: https://www.w3.org/TR/2011/WD-html5-20110525/urls.html
    public void testUrlValidation() {
        // test condition 1: input is a valid URI
        url = "https://ilp-rest.azurewebsites.net/centralarea";
        assertNotNull(new RestClient(url, date));
        url = "https://ilp-rest.azurewebsites.net/orders/2023-01-21";
        assertNotNull(new RestClient(url, date));

        // test condition 2: input is an empty string
        url = "";
//        new RestClient(url, date).expectSystemExit()
//        exception = assertThrows(IllegalArgumentException.class, () -> {
//            new RestClient(url, date);
//        });
//        assertEquals(errorMessage + url + "/", exception.getMessage());

        // test condition 3: input contains white spaces around the URL

        // test condition 4: input is an invalid URI

        // test condition 5: input is a valid IRI reference
    }

    // test case: checking if the server reached is available
    public void testServerAvailability() {

    }

    // test case: checking if the data fetched from the REST API is valid
    public void testDeserialize() {
    }
}