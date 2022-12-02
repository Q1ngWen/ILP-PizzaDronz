package uk.ac.ed.inf.Orders;

/**
 * {@link OrderOutcome} represents the status of the delivery after processing the order.
 */
public enum OrderOutcome {
    DELIVERED,
    VALID_BUT_NOT_DELIVERED,
    INVALID_CARD_NUMBER,
    INVALID_EXPIRY_DATE,
    INVALID_CVV,
    INVALID_TOTAL,
    INVALID_PIZZA_NOT_DEFINED,
    INVALID_PIZZA_COUNT,
    INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS,
    INVALID
}
