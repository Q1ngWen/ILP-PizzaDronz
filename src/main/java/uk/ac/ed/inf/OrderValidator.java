package uk.ac.ed.inf;

public class OrderValidator {
    private Order[] nonValidatedOrders;
    private Order[] validatedOrders;
    private Restaurant[] restaurants;
    private OrderOutcome status;

    public OrderValidator() {};

    public boolean isValidCardNumber(String cardNumString) {
        // credit card numbers are composed of 8-19 digits link: https://en.wikipedia.org/wiki/Payment_card_number
        if (cardNumString.length() != 16) {
            this.status = OrderOutcome.INVALID_CARD_NUMBER;
            return false;
        }

        int[] cardNo = strToInt(cardNumString);

        // Luhn algorithm check
        int sum = 0;
        int parity = (cardNo.length-1) % 2;
        for (int i = 0; i < cardNo.length-1; i++) {
            if (i % 2 == parity) {
                sum += cardNo[i];
            } else if (cardNo[i] > 4) {
                sum += 2*cardNo[i] - 9;
            } else {
                sum += 2*cardNo[i];
            }
        }
        int checkDigit = (10- sum%10) % 10;
        if (checkDigit == cardNo[cardNo.length-1]){
            return true;
        } else {
            this.status = OrderOutcome.INVALID_CARD_NUMBER;
            return false;
        }
    }



    public Order[] getNonValidatedOrders(RestClient server) {
        Order[] orders = null;
        orders = (Order[]) server.deserialize("orders", Order[].class);
        this.nonValidatedOrders = orders;
        return this.nonValidatedOrders;
    }

    public Order[] getValidatedOrders() {
        return validatedOrders;
    }

    public OrderOutcome getStatus() {
        return status;
    }

    // helper function that converts a string of integers to an array of integers
    public int[] strToInt(String s) {
        int size = s.length();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }
        return result;
    }
}
