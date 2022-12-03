package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.Before;
import uk.ac.ed.inf.Orders.Order;
import uk.ac.ed.inf.Orders.OrderValidator;
import uk.ac.ed.inf.Restaurants.Restaurant;

public class OrderValidatorTest extends TestCase {
    private final String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private RestClient server;
    private OrderValidator validator;
    private Order[] nonValidatedOrders;
    private Order[] validatedOrders;
    private Restaurant[] restaurants;
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private Order order5;
    private Order[] orderList1;
    private Order[] orderList2;


    @Before
    public void setUp() {
        server = new RestClient(baseUrl, "2023-01-01");
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        validator = new OrderValidator(restaurants);
        nonValidatedOrders = validator.getOrders(server).toArray(new Order[0]);

        String pizzaItem1 = "Super Cheese";
        String pizzaItem2 = "All Shrooms";
        String pizzaItem3 = "Margarita";
        String pizzaItem4 = "Calzone";
        String pizzaItem5 = "Meat Lover";
        String pizzaItem6 = "Proper Pizza";
        String pizzaItem7 = "Christmas Special";

        String[] pizzaOrder1 = {pizzaItem1};
        String[] pizzaOrder2 = {pizzaItem3, pizzaItem4, pizzaItem3, pizzaItem4};
        String[] pizzaOrder3 = {};
        String[] pizzaOrder4 = {pizzaItem6, pizzaItem2, pizzaItem3, pizzaItem4, pizzaItem5};
        String[] pizzaOrder5 = {pizzaItem6, pizzaItem7};

        order1 = new Order("1AFFE082", "2023-01-01", "Gilberto Handshoe",
                "2402902", "04/28", "922", 1500, pizzaOrder1);
        order2 = new Order("12340B01", "2023-03-29", "Vernice Kaza",
                "4355175523891417", "07/12", "550", 5300, pizzaOrder2);
        order3 = new Order("2240F9BC", "2023-02-28", "Ayato Kamisato",
                "5555 55555%57460", "02/23", "1324", 0, pizzaOrder3);
        order4 = new Order("6526E742", "2022-12-24", "Thoma Ugamari",
                "4111111111111111111", "02/23", "7@9", 2500, pizzaOrder4);
        order5 = new Order("3EDE207C", "2022-11-24", "Bei Dou",
                "4111111111111114", "02/23", "1", 2500, pizzaOrder5);

        orderList1 = new Order[]{order1, order2};
    }

    public void testIsValidCardNumber() {
        // valid ccNum
        assertTrue(validator.isValidCardNumber(order2.getCreditCardNumber()));
        // ccNum less than 16 digits (not visa_16 or mc)
        assertFalse(validator.isValidCardNumber(order1.getCreditCardNumber()));
        // ccNum has symbols other than digits in the card number
        assertFalse(validator.isValidCardNumber(order3.getCreditCardNumber()));
        // ccNum longer than 16 digits
        assertFalse(validator.isValidCardNumber(order4.getCreditCardNumber()));
        // ccNum check digit fail Luhn algorithm
        assertFalse(validator.isValidCardNumber(order5.getCreditCardNumber()));
    }

    public void testIsValidCardExpiry() {
        // valid expiry date
        assertTrue(validator.isValidCardExpiry(order1.getCreditCardExpiry(), order1.getOrderDate()));
        // expiry date before date of order
        assertFalse(validator.isValidCardExpiry(order2.getCreditCardExpiry(), order2.getOrderDate()));
        // expiry date on the month of order (valid)
        assertTrue(validator.isValidCardExpiry(order3.getCreditCardExpiry(), order3.getOrderDate()));
    }

    public void testIsValidCvv() {
        String invalidCvv1 = "1234";
        String invalidCvv2 = "1@3";
        // valid cvv
        assertTrue(validator.isValidCvv(order1.getCvv()));
        assertTrue(validator.isValidCvv(order2.getCvv()));
        // cvv has more than 3 digits
        assertFalse(validator.isValidCvv(order3.getCvv()));
        // cvv has less than 3 digits
        assertFalse(validator.isValidCvv(order5.getCvv()));
        // cvv has symbols
        assertFalse(validator.isValidCvv(order4.getCvv()));
    }

    public void testIsValidTotalPrice() {
        // valid total price
        assertTrue(validator.isValidTotalPrice(order1.getPriceTotalInPence(), order1.getOrderItems()));
        // given total price is lower than actual
        assertFalse(validator.isValidTotalPrice(order2.getPriceTotalInPence(), order2.getOrderItems()));
        // given total price is higher than actual
        assertFalse(validator.isValidTotalPrice(1511, order1.getOrderItems()));
        // given total price is invalid
        assertFalse(validator.isValidTotalPrice(order3.getPriceTotalInPence(), order3.getOrderItems()));

    }

    public void testIsValidPizzaCount() {
        // valid number of pizzas ordered
        assertTrue(validator.isValidPizzaCount(order1.getOrderItems()));
        assertTrue(validator.isValidPizzaCount(order2.getOrderItems()));
        // less than 1 pizza ordered
        assertFalse(validator.isValidPizzaCount(order3.getOrderItems()));
        // more than 4 pizzas ordered
        assertFalse(validator.isValidPizzaCount(order4.getOrderItems()));
    }

    public void testIsValidPizzaCombination() {
        // valid pizza combinations
        assertTrue(validator.isValidPizzaCombination(order1.getOrderItems(), restaurants));
        assertTrue(validator.isValidPizzaCombination(order2.getOrderItems(), restaurants));
        // empty pizza orders
        assertFalse(validator.isValidPizzaCombination(order3.getOrderItems(), restaurants));
        // invalid pizza combinations
        assertFalse(validator.isValidPizzaCombination(order4.getOrderItems(), restaurants));
    }

    public void testIsValidPizzaItem() {
        // valid pizzas ordered
        assertTrue(validator.isValidPizzaItem(order1.getOrderItems()));
        assertTrue(validator.isValidPizzaItem(order2.getOrderItems()));
        assertTrue(validator.isValidPizzaItem(order3.getOrderItems()));
        // invalid pizza ordered
        assertFalse(validator.isValidPizzaItem(order5.getOrderItems()));
    }

    public void testGetNonValidatedOrders() {
//        Order order1 = nonValidatedOrders[0];
//        Order order2 = nonValidatedOrders[1];
//        Order order3 = nonValidatedOrders[2];

        assertNotNull(nonValidatedOrders);
        assertEquals(7050, nonValidatedOrders.length);
    }

    public void testGetAllValidOrders() {
//        assertNotNull(validator.getAllValidOrders(restaurants, nonValidatedOrders));
//        assertNull(validator.getAllValidOrders(restaurants, orderList1));
    }

    public void testGetSpecificDayValidOrders() {
        Order[] result = validator.getOrders(server).toArray(new Order[0]);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i].getOrderNo());
            System.out.println(result[i].getOrderDate());
        }
    }

    public void testTestGetStatus() {
    }

    public void testStrToInt() {
        String validCardNum1 = "79927398713";
        int[] result1 = {7, 9, 9, 2, 7, 3, 9, 8, 7, 1, 3};
        assertEquals(result1[0], validator.strToInt(validCardNum1)[0]);
    }

    public void testSetUpValidator() {
    }
}