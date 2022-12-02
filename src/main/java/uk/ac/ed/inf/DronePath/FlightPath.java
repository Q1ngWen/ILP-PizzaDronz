package uk.ac.ed.inf.DronePath;

public record FlightPath (String orderNo, double fromLongitude, double fromLatitude, double angle,
                          double toLongitude, double toLatitude, long ticksSinceStartOfCalculation) {
}
