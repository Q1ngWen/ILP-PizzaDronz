package uk.ac.ed.inf.OutFiles;

import uk.ac.ed.inf.Orders.OrderOutcome;

/**
 * {@link Deliveries} helps record the deliveries and non-deliveries made by the drone.
 *
 * @param orderNo     Unique 8 character alphanumeric {@link String} for a pizza order.
 * @param outcome     the {@link OrderOutcome} value for this order, as a {@link String};
 * @param costInPence — the total cost of the order as an {@link Integer}, including the standard £1 delivery
 *                    charge
 */
public record Deliveries(String orderNo, String outcome, int costInPence) {

}
