package uk.ac.ed.inf.DronePath;

/**
 * {@link FlightPath} helps record the flight path of the drones move-by-move.
 *
 * @param orderNo                      Unique 8 character alphanumeric {@link String} for a pizza order.
 * @param fromLongitude                The longitude of the drone at the start of this move.
 * @param fromLatitude                 The latitude of the drone at the start of this move.
 * @param angle                        The angle of travel of the drone in this move.
 * @param toLongitude                  The longitude of the drone at the end of this move.
 * @param toLatitude                   The latitude of the drone at the end of this move.
 * @param ticksSinceStartOfCalculation
 */
public record FlightPath(String orderNo, double fromLongitude, double fromLatitude, double angle,
                         double toLongitude, double toLatitude, long ticksSinceStartOfCalculation) {
}
