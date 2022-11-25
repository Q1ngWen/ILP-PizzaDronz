package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.Before;

public class OrderValidatorTest extends TestCase {
    private final String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private RestClient server;
    private OrderValidator validator;
    private Order[] nonValidatedOrders;
    private Order[] validatedOrders;
    private Restaurant[] restaurants;
    private Order order1;
//    private Order order2;
//    private Order order3;
//    private String[] orderList1;
//    private String[] orderList2;

    @Before
    public void setUp() {
        server = new RestClient<>(baseUrl);
        validator = new OrderValidator();
        nonValidatedOrders = validator.getNonValidatedOrders(server);
        restaurants = Restaurant.getRestaurantFromRestServer(server);

//        assertNotNull(order1);
//        assertNotNull(order2);
//        assertNotNull(order3);
//
//        String orderItem1 = "Super Cheese";
//        String orderItem2 = "All Shrooms";
//        String orderItem3 = "Margarita";
//        String orderItem4 = "Super Cheese";
//        String orderItem5 = "Super Cheese";
//        String orderItem6 = "Super Cheese";
//
//        order1 = new Order("1AFFE082", "2023-01-01", "Gilberto Handshoe", "2402902", "04/28", "922", 2400, orderList1);
    }

    public void testIsValidCardNumber() {
        String validCardNum1 = "4012888888881881";
        String validCardNum2 = "5425233430109903";
        String invalidCardNum1 = "5555555555557462";
        String invalidCardNum2 = "4917484589897107";
        String invalidCardNum3 = "4607925006404153543";
        assertTrue(validator.isValidCardNumber(validCardNum1));
        assertTrue(validator.isValidCardNumber(validCardNum2));
//        assertFalse(validator.isValidCardNumber(invalidCardNum1));
//        assertFalse(validator.isValidCardNumber(invalidCardNum2));
        assertFalse(validator.isValidCardNumber(invalidCardNum3));
    }

    public void testGetNonValidatedOrders() {
//        Order order1 = nonValidatedOrders[0];
//        Order order2 = nonValidatedOrders[1];
//        Order order3 = nonValidatedOrders[2];

        assertNotNull(nonValidatedOrders);
        assertEquals(7050, nonValidatedOrders.length);
    }

    public void testGetValidatedOrders() {
    }

    public void testGetStatus() {
    }

    public void testStrToInt() {
        String validCardNum1 = "79927398713";
        String validCardNum2 = "5425233430109903";
        String invalidCardNum1 = "378282246310005";
        String invalidCardNum2 = "4917484589897107";

        int[] result1 = {7,9,9,2,7,3,9,8,7,1,3};
        assertEquals(result1[0], validator.strToInt(validCardNum1)[0]);
    }
}