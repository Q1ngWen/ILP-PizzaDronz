package uk.ac.ed.inf.Orders;

import junit.framework.TestCase;
import org.junit.Before;
import uk.ac.ed.inf.Restaurants.MenuItem;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.List;

public class OrderValidatorUnitTest extends TestCase {
    private OrderValidator validator;
    private List<Order> orders;
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
        String pizzaItem1 = "Super Cheese";
        String pizzaItem2 = "All Shrooms";
        String pizzaItem3 = "Margarita";
        String pizzaItem4 = "Calzone";
        String pizzaItem5 = "Meat Lover";
        String pizzaItem6 = "Proper Pizza";
        String pizzaItem7 = "Christmas Special";

        MenuItem menuItem1 = new MenuItem(pizzaItem1, 1500);
        MenuItem menuItem2 = new MenuItem(pizzaItem2, 1300);
        MenuItem menuItem3 = new MenuItem(pizzaItem3, 1000);
        MenuItem menuItem4 = new MenuItem(pizzaItem4, 2000);
        MenuItem menuItem5 = new MenuItem(pizzaItem5, 1800);
        MenuItem menuItem6 = new MenuItem(pizzaItem6, 1000);
        MenuItem[] menu1 = {menuItem1, menuItem2};
        MenuItem[] menu2 = {menuItem3, menuItem4};
        MenuItem[] menu3 = {menuItem5, menuItem6};
        Restaurant restaurant1 = new Restaurant("Dominoz", -3.188900, 55.944000, menu1);
        Restaurant restaurant2 = new Restaurant("Papa Johns", -3.182400, 55.946600, menu2);
        Restaurant restaurant3 = new Restaurant("Pepe's Pizza Place", -3.186500, 55.942900, menu3);
        restaurants = new Restaurant[]{restaurant1, restaurant2, restaurant3};
        validator = new OrderValidator(restaurants);

        String[] pizzaOrder1 = {pizzaItem1};
        String[] pizzaOrder2 = {pizzaItem3, pizzaItem4, pizzaItem3, pizzaItem4};
        String[] pizzaOrder3 = {};
        String[] pizzaOrder4 = {pizzaItem6, pizzaItem2, pizzaItem3, pizzaItem4, pizzaItem5};
        String[] pizzaOrder5 = {pizzaItem6, pizzaItem7};

        order1 = new Order("1AFFE082", "2023-01-01", "Gilberto Handshoe",
                "2402902", "04/28", "922", 1600, pizzaOrder1);
        order2 = new Order("12340B01", "2023-03-29", "Vernice Kaza",
                "4355175523891417", "07/12", "550", 6000, pizzaOrder2);
        order3 = new Order("2240F9BC", "2023-02-28", "Ayato Kamisato",
                "5555 55555%57460", "02/23", "1324", 0, pizzaOrder3);
        order4 = new Order("6526E742", "2022-12-24", "Thoma Ugamari",
                "4111111111111111111", "02/23", "7@9", 6200, pizzaOrder4);
        order5 = new Order("3EDE207C", "2022-11-24", "Bei Dou",
                "4111111111111114", "02/23", "1", 2900, pizzaOrder5);

        orderList1 = new Order[]{order1, order2};
    }

    // test suite 1: validating credit card number
    public void testIsValidCardNumber() {
        // test case 1: credit card number with 16 digits and correct check digit
        assertTrue(validator.isValidCardNumber(order2.getCreditCardNumber()));
        // test case 2: credit card number less than 16 digits (not visa_16 or mc)
        assertFalse(validator.isValidCardNumber(order1.getCreditCardNumber()));
        // test case 3: credit card number has symbols other than digits in the card number
        assertFalse(validator.isValidCardNumber(order3.getCreditCardNumber()));
        // test case 4: credit card number longer than 16 digits
        assertFalse(validator.isValidCardNumber(order4.getCreditCardNumber()));
        // test case 5: credit card number check digit fail Luhn algorithm
        assertFalse(validator.isValidCardNumber(order5.getCreditCardNumber()));
    }

    // test suite 2: validating credit card expiry date
    public void testIsValidCardExpiry() {
        // test case 1: expiry date is after date of order
        assertTrue(validator.isValidCardExpiry(order1.getCreditCardExpiry(), order1.getOrderDate()));
        // test case 2: expiry date is before date of order
        assertFalse(validator.isValidCardExpiry(order2.getCreditCardExpiry(), order2.getOrderDate()));
        // test case 3: expiry date on the month of order (valid)
        assertTrue(validator.isValidCardExpiry(order3.getCreditCardExpiry(), order3.getOrderDate()));
        // test case 4: expiry date is not in valid format
        assertFalse(validator.isValidCardExpiry("-1/24", order4.getOrderDate()));
        assertFalse(validator.isValidCardExpiry("01/2024", order4.getOrderDate()));
    }

    // test suite 3: validating the CVV of the credit card
    public void testIsValidCvv() {
        // test case 1: cvv has 3 digits
        assertTrue(validator.isValidCvv(order1.getCvv()));
        assertTrue(validator.isValidCvv(order2.getCvv()));
        // test case 2: cvv has more than 3 digits
        assertFalse(validator.isValidCvv(order3.getCvv()));
        // test case 3: cvv has less than 3 digits
        assertFalse(validator.isValidCvv(order5.getCvv()));
        // test case 4: cvv has symbols
        assertFalse(validator.isValidCvv(order4.getCvv()));
    }

    // test suite 4: validating the total given price of the pizza orders
    public void testIsValidTotalPrice() {
        // test case 1: total price given is equal to actual calculated total price
        assertTrue(validator.isValidTotalPrice(order1.getPriceTotalInPence(), order1.getOrderItems()));
        // test case 2: total price given is lower than the actual
        assertFalse(validator.isValidTotalPrice(order2.getPriceTotalInPence(), order2.getOrderItems()));
        // test case 3: total price given is higher than actual
        assertFalse(validator.isValidTotalPrice(2000, order1.getOrderItems()));
        // test case 4: total price given is 0
        assertFalse(validator.isValidTotalPrice(order3.getPriceTotalInPence(), order3.getOrderItems()));
    }

    // test suite 5: validating the total number of pizzas ordered is between 1 to 4
    public void testIsValidPizzaCount() {
        // test case 1: 1 pizza ordered
        assertTrue(validator.isValidPizzaCount(order1.getOrderItems()));
        // test case 2: 4 pizzas ordered
        assertTrue(validator.isValidPizzaCount(order2.getOrderItems()));
        // test case 3: between 1 to 4 pizzas ordered
        assertTrue(validator.isValidPizzaCount(order5.getOrderItems()));
        // test case 4: less than 1 pizza ordered
        assertFalse(validator.isValidPizzaCount(order3.getOrderItems()));
        // test case 5: more than 4 pizzas ordered
        assertFalse(validator.isValidPizzaCount(order4.getOrderItems()));
    }

    // test suite 6: validating the pizza item ordered
    public void testIsValidPizzaCombination() {
        // test case 1: all pizzas ordered are from the same singular restaurant
        assertTrue(validator.isValidPizzaCombination(order1.getOrderItems(), restaurants));
        assertTrue(validator.isValidPizzaCombination(order2.getOrderItems(), restaurants));
        // test case 2: empty pizza orders
        assertFalse(validator.isValidPizzaCombination(order3.getOrderItems(), restaurants));
        // test case 3: pizzas ordered are from more than 1 restaurant
        assertFalse(validator.isValidPizzaCombination(order4.getOrderItems(), restaurants));
    }

    // test suite 7: validating the type of pizzas ordered
    public void testIsValidPizzaItem() {
        // test case 1: pizzas ordered exist in any restaurant's menu
        assertTrue(validator.isValidPizzaItem(order1.getOrderItems()));
        assertTrue(validator.isValidPizzaItem(order2.getOrderItems()));
        assertTrue(validator.isValidPizzaItem(order3.getOrderItems()));
        // test case 2: pizzas ordered do not exist in any restaurant's menu
        assertFalse(validator.isValidPizzaItem(order5.getOrderItems()));
    }
}