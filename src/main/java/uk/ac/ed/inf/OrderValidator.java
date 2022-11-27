package uk.ac.ed.inf;

import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderValidator {
    private Order[] nonValidatedOrders;
    private OrderOutcome status;

    private Map<String, String> restaurantMenuName;
    private Map<String, Integer> restaurantMenuPrice;


    public OrderValidator() {
    }

    public boolean isValidCardNumber(String cardNumString) {
        // credit card numbers are composed of 8-19 digits link: https://en.wikipedia.org/wiki/Payment_card_number
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

        // validating the check digits of the credit card number
        int checkDigit = (10 - sum % 10) % 10;
        if (checkDigit == cardNo[cardNo.length - 1]) {
            return true;
        } else {
            this.status = OrderOutcome.INVALID_CARD_NUMBER;
            return false;
        }
    }

    public boolean isValidCardExpiry(String cardExpiryString, String orderDateString) {
        if (cardExpiryString == null || orderDateString == null) {
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

    public boolean isValidPizzaCount(String[] orderItems) {
        if (orderItems == null || orderItems.length < 1 || orderItems.length > 4) {
            this.status = OrderOutcome.INVALID_PIZZA_COUNT;
            return false;
        } else return true;
    }

    public boolean isValidPizzaCombination(String[] orderItems, Restaurant[] restaurants) {
        if (orderItems == null || restaurants == null) {
            this.status = OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS;
            return false;
        }

        // checking for valid pizza combinations
        Set<String> restaurantValidation = new HashSet<>();
        for (String s : orderItems) {
            restaurantValidation.add(restaurantMenuName.get(s.toLowerCase()));
        }
        if (restaurantValidation.size() != 1) {
            this.status = OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS;
            return false;
//            try {
//                throw new Exception(String.valueOf(OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
        } else return true;
    }

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

    public Order[] getNonValidatedOrders(RestClient server) {
        Order[] orders = null;
        orders = (Order[]) server.deserialize("orders", Order[].class);
        this.nonValidatedOrders = orders;
        return this.nonValidatedOrders;
    }

    public Order[] getAllValidOrders(Restaurant[] restaurants, Order[] orders) {
        ArrayList<Order> validatedOrders = new ArrayList<>();
        for (int i = 0; i < orders.length; i++) {
            Order order = orders[i];
            if (!isValidCardNumber(order.getCreditCardNumber())) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            if (!isValidCardExpiry(order.getCreditCardExpiry(), order.getOrderDate())) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            if (!isValidCvv(order.getCvv())) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            if (!isValidPizzaCount(order.getOrderItems())) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            if (!isValidPizzaItem(order.getOrderItems())) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            if (!isValidPizzaCombination(order.getOrderItems(), restaurants)) {
                System.err.println("Order " + order.getOrderNo() + " is invalid: " + status);
            }
            validatedOrders.add(order);
        }
        return validatedOrders.toArray(new Order[0]);
    }

    public Order[] getSpecificDayValidOrders(Restaurant[] restaurants, String date) {
        ArrayList<Order> validOrders = new ArrayList<>();

        return validOrders.toArray(new Order[0]);
    }

    public OrderOutcome getStatus() {
        return status;
    }

    // helper function that converts a string of integers to an array of integers
    public int[] strToInt(String s) {
        if (s == null) this.status = OrderOutcome.INVALID;

        int size = s.length();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return result;
    }

    public void setUpValidator(Restaurant[] restaurants) {
        if (restaurants == null) this.status = OrderOutcome.INVALID;

        restaurantMenuName = new HashMap<>();
        restaurantMenuPrice = new HashMap<>();

        // extracting restaurants with their respective menus and menus with their respective item's prices
        for (Restaurant restaurant : restaurants) {
            MenuItem[] menuList = restaurant.getMenu();
            for (MenuItem menuItem : menuList) {
                restaurantMenuName.put(menuItem.name().toLowerCase(), restaurant.getName().toLowerCase());
                restaurantMenuPrice.put(menuItem.name().toLowerCase(), menuItem.priceInPence());
            }
        }
    }
}
