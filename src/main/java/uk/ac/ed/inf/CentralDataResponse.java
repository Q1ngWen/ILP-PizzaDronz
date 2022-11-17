package uk.ac.ed.inf;

/**
 * @param name      The name of the location, given the longitude and latitude.
 * @param longitude The longitude, measured in degrees.
 * @param latitude  The latitude, measured in degrees.
 */

public record CentralDataResponse(String name, double longitude, double latitude) {
}
