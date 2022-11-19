package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;

/**
 * {@link Restaurant} allows system to find location of the restaurant and its menus for users to view and order.
 */

public class Restaurant {
    private String name;
    private double longitude;
    private double latitude;
    private MenuItem[] menu;

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
        try {
            URL restaurantsUrl = new URL(server.getBaseUrl() + "restaurants");
            restaurants = new ObjectMapper().readValue(restaurantsUrl, Restaurant[].class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return restaurants;
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
}
