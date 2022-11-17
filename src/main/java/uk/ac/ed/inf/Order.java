package uk.ac.ed.inf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link Order} contains the details of customer's orders, including the list of pizzas to be delivered.
 */
public class Order {
    private String orderNo;
    private String orderDate;
    private String customer;
    private String creditCardNumber;
    private String creditCardExpiry;
    private String cvv;
    private int priceTotalInPence;
    private String[] orderItems;

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
    public Order(String orderNo, String orderDate, String customer, String creditCardNumber,
                 String creditCardExpiry, String cvv, int priceTotalInPence, String[] orderItems) {
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
        Map<String, String> restaurantMenuName = new HashMap<String, String>();
        Map<String, Integer> restaurantMenuPrice = new HashMap<String, Integer>();

        // extracting restaurants with their respective menus and menus with their respective item's prices
        for (int i = 0; i < restaurants.length; i++) {
            Menu[] menuList = restaurants[i].getMenu();
            for (int j = 0; j < menuList.length; j++) {
                restaurantMenuName.put(menuList[j].name().toLowerCase(), restaurants[i].getName().toLowerCase());
                restaurantMenuPrice.put(menuList[j].name().toLowerCase(), menuList[j].priceInPence());
            }
        }

        // checking for valid pizza combinations
        Set<String> restaurantValidation = new HashSet<String>();
        for (int i = 0; i < orderedPizza.length; i++) {
            restaurantValidation.add(restaurantMenuName.get(orderedPizza[i].toLowerCase()));
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
        for (int i = 0; i < orderedPizza.length; i++) {
            deliveryCost += restaurantMenuPrice.get(orderedPizza[i].toLowerCase());
        }
        return deliveryCost;
    }
}
