package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.Before;

import java.awt.*;

public class RestaurantTest extends TestCase {

    private String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private RestClient server = new RestClient(baseUrl);
    private Restaurant[] restaurants;
    private MenuItem pizza1;
    private MenuItem pizza2;
    private MenuItem pizza3;
    private MenuItem pizza4;
    private MenuItem pizza5;

    private MenuItem[] menu1;
    private MenuItem[] menu2;


    @Before
    public void setup(){
        assertNotNull(menu1);
        assertNotNull(menu2);

        pizza1 = new MenuItem("Quad Cheese Deluxe", 1500);
        pizza2 = new MenuItem("Hawaiian Delight", 1000);
        pizza3 = new MenuItem("Barbeque Chicken", 1700);
        pizza4 = new MenuItem("Margarita", 1000);
        pizza5 = new MenuItem("Calzone", 1400);

        menu1 = new MenuItem[3];
        menu1[0] = pizza1;
        menu1[1] = pizza2;
        menu1[2] = pizza3;
        menu2 = new MenuItem[2];
        menu2[0] = pizza4;
        menu2[1] = pizza5;

        Restaurant restaurant1 = new Restaurant("Papa Johns", -3.1900, 55.9450, menu1);
        Restaurant restaurant2 = new Restaurant("Chica's Comfort Dinner", -3.1940, 55.9460, menu2);
    }

    public void testGetRestaurantFromRestServer_return_restaurants_when_valid() {
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        assertNotNull(restaurants);
        assertEquals(4, restaurants.length);
    }

    public void testGetMenu() {
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        assertNotNull(restaurants[0].getMenu());
//        assertEquals(menu2, restaurants[0].getMenu());
        assertNotNull(restaurants[1].getMenu());
//        assertNotSame(menu1, restaurants[1].getMenu());
    }

    public void testTestGetName() {
        restaurants = Restaurant.getRestaurantFromRestServer(server);
        assertNotNull(restaurants[2].getName());
        assertEquals("Domino's Pizza - Edinburgh - Southside", restaurants[2].getName());
        assertNotNull(restaurants[3].getName());
        assertEquals("Sodeberg Pavillion", restaurants[3].getName());
    }
}