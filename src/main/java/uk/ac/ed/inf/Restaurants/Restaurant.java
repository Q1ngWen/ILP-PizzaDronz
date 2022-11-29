package uk.ac.ed.inf.Restaurants;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.ac.ed.inf.DronePath.LngLat;
import uk.ac.ed.inf.RestClient;

/**
 * {@link Restaurant} allows system to find location of the restaurant and its menus for users to view and order.
 */

public class Restaurant {
    private final String name;
    private final double longitude;
    private final double latitude;
    private final MenuItem[] menu;

    /**
     * @param name The name of the restaurant.
     * @param longitude The longitude of the restaurant, measured in degrees.
     * @param latitude The latitude of the restaurant, measured in degrees.
     * @param menu The menu with an array pizzas for sale in the restaurant.
     */
    public Restaurant(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude,
                      @JsonProperty("latitude") double latitude, @JsonProperty("menu") MenuItem[] menu) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.menu = menu;
    }

    /**
     * @param server The home page of the website (to fetch data from).
     * @return Array of restaurants collaborating with the drone service.
     */
    public static Restaurant[] getRestaurantFromRestServer(RestClient server) {
        Restaurant[] restaurants = null;
        restaurants = (Restaurant[])server.deserialize("restaurants", Restaurant[].class);
        return restaurants;
//        try {
//            URL restaurantsUrl = new URL(server.getBaseUrl() + "restaurants");
//            restaurants = new ObjectMapper().readValue(restaurantsUrl, Restaurant[].class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//        return restaurants;
    }

    /**
     * @return An array of {@link MenuItem} containing all items for sale.
     */
    public MenuItem[] getMenu() {
        return menu;
    }

    /**
     * @return The name of the restaurant.
     */
    public String getName() {
        return name;
    }

    public LngLat getCoordinate() {
        return new LngLat(longitude, latitude);
    }
}
