package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private OrderOutcome status;

    /**
     * @param orderNo The Unique alphanumeric {@link String} for each order made.
     * @param orderDate The date of the order.
     * @param customer Name of the customer.
     * @param creditCardNumber Number of credit card (for order payment).
     * @param creditCardExpiry Expiry date of customer's credit card.
     * @param cvv Credit card verification value.
     * @param priceTotalInPence Total price of all pizzas ordered, in pence.
     * @param orderItems Array of pizzas ordered.
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
     * @param restaurants Array of all restaurants involved in the order's service.
     * @param orderedPizza Array of pizzas ordered.
     * @return Total cost of entire service, including total pizza costs and deliver fees.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String[] orderedPizza) {
        Map<String, String> restaurantMenuName = new HashMap<>();
        Map<String, Integer> restaurantMenuPrice = new HashMap<>();

        // extracting restaurants with their respective menus and menus with their respective item's prices
        for (Restaurant restaurant : restaurants) {
            MenuItem[] menuList = restaurant.getMenu();
            for (MenuItem menuItem : menuList) {
                restaurantMenuName.put(menuItem.name().toLowerCase(), restaurant.getName().toLowerCase());
                restaurantMenuPrice.put(menuItem.name().toLowerCase(), menuItem.priceInPence());
            }
        }

        // checking for valid pizza combinations
        Set<String> restaurantValidation = new HashSet<>();
        for (String s : orderedPizza) {
            restaurantValidation.add(restaurantMenuName.get(s.toLowerCase()));
        }
        if (restaurantValidation.size() != 1) {
            try {
                throw new Exception(String.valueOf(OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // checking for valid number of pizzas
        if (orderedPizza.length < 1 || 4 < orderedPizza.length) {
            try {
                throw new Exception(String.valueOf(OrderOutcome.INVALID_PIZZA_COUNT));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // calculating the final total delivery cost
        int deliveryCost = 100;
        for (String s : orderedPizza) {
            deliveryCost += restaurantMenuPrice.get(s.toLowerCase());
        }
        return deliveryCost;
    }


    // getters
    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCustomer() {
        return customer;
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
