package uk.ac.ed.inf;

/**
 * Individual item for sale in a {@link Restaurant}.
 * @param name Name of the pizza for sale.
 * @param priceInPence Price of pizza for sale in pence.
 */

public record Menu(String name, int priceInPence) {
}
