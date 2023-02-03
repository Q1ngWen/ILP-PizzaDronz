package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.Before;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.List;

public class OrderValidationIntegrationTest extends TestCase {
    private final String url = "https://ilp-rest.azurewebsites.net/";
    private RestClient server;
    private OrderValidator validator;
    private Restaurant[] restaurants;

    @Before
    public void setUp() {
        server = new RestClient(url, "2023-10-10");
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator(restaurants);
    }

    // test suite 1: checking number of valid and invalid orders fetched
    public void testOrdersFetched() {
        // test case 1: correct number of valid and invalid orders fetched and validated
        List<Order> orders = validator.getOrders(server, "2023-01-01");
        int valid = 0;
        int invalid = 0;
        for (int i = 0; i < orders.size(); i++) {
            Order current = orders.get(i);
            if (validator.isValidOrder(restaurants, current)) {
                valid += 1;
            } else {
                invalid += 1;
            }
        }
        assertEquals(40, valid);
        assertEquals(7, invalid);
    }
}
