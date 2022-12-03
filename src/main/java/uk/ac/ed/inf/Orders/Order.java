package uk.ac.ed.inf.Orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.Map.LngLat;
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

    /**
     * @param outcome Sets the {@link OrderOutcome} of the current order.
     */
    public void setOutcome(OrderOutcome outcome) {
        this.outcome = outcome;
    }

    /**
     * @param restaurants List of {@link Restaurant} from the ILP REST server.
     * @return Returns the {@link LngLat} coordinates of the order's {@link Restaurant}.
     */
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

    // -- getters --

    /**
     * @return {@link OrderOutcome} of the {@link Order}
     */
    public OrderOutcome getOutcome() {
        return outcome;
    }

    /**
     * @return {@link String} of the unique alphanumeric value for a pizza order.
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @return {@link String} date the order was made.
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @return {@link String} of numbers making up the customers credit card number.
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @return {@link String} of credit card's expiry date in MM/YY format.
     */
    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }

    /**
     * @return {@link String} numbers making up a credit card's verification value.
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @return {@link Integer} pence of the total price of the order, including the 100 pence delivery cost.
     */
    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    /**
     * @return List of {@link String} consisting of the pizza items ordered.
     */
    public String[] getOrderItems() {
        return orderItems;
    }

}
