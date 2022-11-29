package uk.ac.ed.inf.Orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Restaurants.MenuItem;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.util.*;

/**
 * {@link Order} contains the details of customer's orders, including the list of pizzas to be delivered.
 */
public class Order {
    private final String orderNo;
    private final String orderDate;
    private final String customer;
    private final String creditCardNumber;
    private final String creditCardExpiry;
    private final String cvv;
    private final int priceTotalInPence;
    private final String[] orderItems;
    private OrderOutcome outcome;

    /**
     * @param orderNo           The Unique alphanumeric {@link String} for each order made.
     * @param orderDate         The date of the order.
     * @param customer          Name of the customer.
     * @param creditCardNumber  Number of credit card (for order payment).
     * @param creditCardExpiry  Expiry date of customer's credit card.
     * @param cvv               Credit card verification value.
     * @param priceTotalInPence Total price of all pizzas ordered, in pence.
     * @param orderItems        Array of pizzas ordered.
     */
    public Order(@JsonProperty("orderNo") String orderNo, @JsonProperty("orderDate") String orderDate, @JsonProperty("customer") String customer,
                 @JsonProperty("creditCardNumber") String creditCardNumber, @JsonProperty("creditCardExpiry") String creditCardExpiry, @JsonProperty("cvv") String cvv,
                 @JsonProperty("priceTotalInPence") int priceTotalInPence, @JsonProperty("orderItems") String[] orderItems) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
    }

    // setters
    public void setStatus(OrderOutcome outcome) {
        this.outcome = outcome;
    }

    // getters
    // get the coordinates of the restaurant the orders are from
    public Restaurant getOrdersRestaurant(Restaurant[] restaurants) {
        HashMap<String, Restaurant> menuRestaurant = new HashMap<>();
        for (Restaurant r : restaurants) {
            MenuItem[] menuList = r.getMenu();
            for (MenuItem menu : menuList) {
                menuRestaurant.put(menu.name(), r);
            }
        }

        for (String order : orderItems) {
            if (menuRestaurant.containsKey(order)) {
                return menuRestaurant.get(order);
            }
        }
        return null;
    }

    public OrderOutcome getOutcome() {
        return outcome;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }

    public String getCvv() {
        return cvv;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    public String[] getOrderItems() {
        return orderItems;
    }

}
