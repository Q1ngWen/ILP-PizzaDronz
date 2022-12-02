package uk.ac.ed.inf.Orders;

import uk.ac.ed.inf.App;
import uk.ac.ed.inf.Restaurants.MenuItem;
import uk.ac.ed.inf.RestClient;
import uk.ac.ed.inf.Restaurants.Restaurant;

import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *{@link OrderValidator} helps check {@link Order} and validate their details.
 */
public class OrderValidator {
    private ArrayList<Order> orders;
    private OrderOutcome status;

    private Map<String, Restaurant> restaurantMenuName;
    private Map<String, Integer> restaurantMenuPrice;

    /**
     *
     * @param restaurants List of {@link Restaurant} from the ILP REST Server.
     */
    public OrderValidator(Restaurant[] restaurants) {
        if (restaurants == null) this.status = OrderOutcome.INVALID;

        restaurantMenuName = new HashMap<>();
        restaurantMenuPrice = new HashMap<>();

        // extracting restaurants with their respective menus and menus with their respective item's prices
        for (Restaurant restaurant : restaurants) {
            MenuItem[] menuList = restaurant.getMenu();
            for (MenuItem menuItem : menuList) {
                restaurantMenuName.put(menuItem.name().toLowerCase(), restaurant);
                restaurantMenuPrice.put(menuItem.name().toLowerCase(), menuItem.priceInPence());
            }
        }
    }

    /**
     *
     * @param cardNumString {@link String} of numbers making up the customers credit card number.
     * @return Returns true if the string is composed of only 16 digits for MC and Visa_16 and passes the Luhn algorithm
     * @see <a href="https://www.groundlabs.com/blog/anatomy-of-a-credit-card/">Luhn Algorithm explained</a>
     * @see <a href="https://www.bankrate.com/finance/credit-cards/what-do-the-numbers-on-your-credit-card-mean/#same">
     *     Number of digits for MasterCards and Visa_16s</a>
     */
    public boolean isValidCardNumber(String cardNumString) {
        // card should only have 16 digits and no other alphanumeric symbols
        String regex = "^[0-9]{16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cardNumString);
        if (cardNumString == null || !m.matches()) {
            this.status = OrderOutcome.INVALID_CARD_NUMBER;
            return false;
        }

        int[] cardNo = strToInt(cardNumString);

        // Luhn algorithm to calculate the check digit
        int sum = 0;
        int parity = (cardNo.length - 1) % 2;
        for (int i = 0; i < cardNo.length - 1; i++) {
            if (i % 2 == parity) {
                sum += cardNo[i];
            } else if (cardNo[i] > 4) {
                sum += 2 * cardNo[i] - 9;
            } else {
                sum += 2 * cardNo[i];
            }
        }

        // validating the calculated check digit against the card's actual check digit, which is the last digit in
        // a credit card's number
        int checkDigit = (10 - sum % 10) % 10;
        if (checkDigit == cardNo[cardNo.length - 1]) {
            return true;
        } else {
            this.status = OrderOutcome.INVALID_CARD_NUMBER;
            return false;
        }
    }

    /**
     *
     * @param cardExpiryString {@link String} of credit card's expiry date in MM/YY format.
     * @param orderDateString {@link String} date of orders to be viewed and delivered.
     * @return Returns true if the expiry date provided is in YY/MM format and within bounds of a normal year, and if
     * it doesnt contain additional special symbols.
     */
    public boolean isValidCardExpiry(String cardExpiryString, String orderDateString) {
        if (cardExpiryString == null) {
            this.status = OrderOutcome.INVALID_EXPIRY_DATE;
            return false;
        }

        String[] temp = cardExpiryString.split("/");
        int yy = Integer.parseInt("20" + temp[1]);
        int mm = Integer.parseInt(temp[0]);
        LocalDate orderDate = LocalDate.parse(orderDateString);

        // if the expiry date provided is not in YY/MM format or month is out of bounds
        if (temp.length != 2 || 0 > mm || mm > 12) {
            this.status = OrderOutcome.INVALID_EXPIRY_DATE;
            return false;
        }

        // if the expiry date is past order's date
        LocalDate cardExpiry = LocalDate.of(yy, mm, 1);
        int dd = cardExpiry.lengthOfMonth();
        cardExpiry = LocalDate.of(yy, mm, dd);
        if (cardExpiry.isAfter(orderDate) || cardExpiry.equals(orderDate)) {
            return true;
        } else {
            this.status = OrderOutcome.INVALID_EXPIRY_DATE;
            return false;
        }
    }

    /**
     *
     * @param cvvString {@link String} numbers making up a credit card's verification value.
     * @return Returns true if the {@link String} only contains numbers.
     */
    public boolean isValidCvv(String cvvString) {
        // regex to check for valid cvv numbers
        String regex = "^[0-9]{3}$";
        Pattern p = Pattern.compile(regex);
        if (cvvString == null || cvvString.length() != 3) {
            this.status = OrderOutcome.INVALID_CVV;
            return false;
        }
        Matcher m = p.matcher(cvvString);
        return m.matches();
    }

    /**
     *
     * @param givenPrice {@link Integer} pence of the total price of the order, including the 100 pence delivery cost.
     * @param orderedItems List of {@link String} of all items ordered.
     * @return Returns true if the cost of the ordered items and delivery is equivalent to the given price.
     */
    public boolean isValidTotalPrice(int givenPrice, String[] orderedItems) {
        if (givenPrice == 0 || orderedItems == null) {
            this.status = OrderOutcome.INVALID_TOTAL;
            return false;
        }
        int totalPrice = 100;
        for (String s : orderedItems) {
            totalPrice += restaurantMenuPrice.get(s.toLowerCase());
        }
        if (givenPrice != totalPrice) {
            this.status = OrderOutcome.INVALID_TOTAL;
            return false;
        }
        return true;
    }

    /**
     *
     * @param orderItems List of {@link String} of all items ordered.
     * @return Returns true if there's a minimum of one or a maximum or four items ordered
     */
    public boolean isValidPizzaCount(String[] orderItems) {
        if (orderItems == null || orderItems.length < 1 || orderItems.length > 4) {
            this.status = OrderOutcome.INVALID_PIZZA_COUNT;
            return false;
        } else return true;
    }

    /**
     *
     * @param orderItems List of {@link String} of all items ordered.
     * @param restaurants List of {@link Restaurant} from the ILP REST Server.
     * @return Returns true if the pizzas ordered in the order items are from the same restaurant.
     */
    public boolean isValidPizzaCombination(String[] orderItems, Restaurant[] restaurants) {
        if (orderItems == null || restaurants == null) {
            this.status = OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS;
            return false;
        }

        // checking for valid pizza combinations
        Set<String> restaurantValidation = new HashSet<>();
        for (String s : orderItems) {
            restaurantValidation.add(restaurantMenuName.get(s.toLowerCase()).getName());
        }
        if (restaurantValidation.size() != 1) {
            this.status = OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS;
            return false;
        } else return true;
    }

    /**
     *
     * @param orderItems List of {@link String} of all items ordered.
     * @return Returns true if all the pizzas in order items exist in a {@link Restaurant}'s menu.
     */
    public boolean isValidPizzaItem(String[] orderItems) {
        if (orderItems == null) {
            this.status = OrderOutcome.INVALID_PIZZA_NOT_DEFINED;
            return false;
        }
        List<String> allMenuItems = new ArrayList<>(restaurantMenuName.keySet());
        HashSet<String> uniqueOrder = new HashSet<>(List.of(orderItems));
        allMenuItems.forEach(s -> s.toLowerCase());
        int found = 0;
        for (String order : uniqueOrder) {
            if (allMenuItems.contains(order.toLowerCase())) {
                found += 1;
            }
        }
        if (found != uniqueOrder.size()) {
            this.status = OrderOutcome.INVALID_PIZZA_NOT_DEFINED;
            return false;
        }
        return true;
    }

    /**
     *
     * @param server {@link RestClient} allows data to be retrieved from the ILP REST Server.
     * @return List of {@link Order} retrieved from the ILP REST Server.
     */
    public List<Order> getOrders(RestClient server) {
        Order[] temp = null;
        temp = (Order[]) server.deserialize("orders", Order[].class);
        orders = Stream.of(temp).collect(Collectors.toCollection(ArrayList::new));
        return this.orders;
    }

    /**
     *
     * @param server {@link RestClient} allows data to be retrieved from the ILP REST Server.
     * @param date Date of orders to be viewed and delivered.
     * @return List of {@link Order} on the specific date, retrieved from the ILP REST Server.
     */
    public List<Order> getOrders(RestClient server, String date) {
        Order[] temp = null;
        temp = (Order[]) server.deserialize("orders/" + date, Order[].class);
        orders = Stream.of(temp).collect(Collectors.toCollection(ArrayList::new));
        return this.orders;
    }

    /**
     *
     * @param restaurants List of {@link Restaurant} from the ILP REST Server.
     * @param order current {@link Order}
     * @return Returns true if the current order passes all validation checks on each individual attribute.
     */
    public boolean isValidOrder(Restaurant[] restaurants, Order order) {
        if (!isValidCardNumber(order.getCreditCardNumber())) {
            order.setOutcome(OrderOutcome.INVALID_CARD_NUMBER);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        if (!isValidCardExpiry(order.getCreditCardExpiry(), order.getOrderDate())) {
            order.setOutcome(OrderOutcome.INVALID_EXPIRY_DATE);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        if (!isValidCvv(order.getCvv())) {
            order.setOutcome(OrderOutcome.INVALID_CVV);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        if (!isValidPizzaCount(order.getOrderItems())) {
            order.setOutcome(OrderOutcome.INVALID_PIZZA_COUNT);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        if (!isValidPizzaItem(order.getOrderItems())) {
            order.setOutcome(OrderOutcome.INVALID_PIZZA_NOT_DEFINED);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        if (!isValidPizzaCombination(order.getOrderItems(), restaurants)) {
            order.setOutcome(OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS);
            System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            return false;
        }
        order.setOutcome(OrderOutcome.VALID_BUT_NOT_DELIVERED);
        return true;
    }

    /**
     *
     * @param s {@link String} of {@link Integer}
     * @return Converts it into a array of {@link Integer}.
     */
    public int[] strToInt(String s) {
        if (s == null) this.status = OrderOutcome.INVALID;

        int size = s.length();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return result;
    }

    // helper function that checks for the restaurant of the order

//    public Restaurant getOrdersRestaurant(String[] orderItems) {
//        for (String order : orderItems) {
//            if (restaurantMenuName.containsKey(order)) {
//                return restaurantMenuName.get(order);
//            }
//        }
//        return null;
//    }
}
