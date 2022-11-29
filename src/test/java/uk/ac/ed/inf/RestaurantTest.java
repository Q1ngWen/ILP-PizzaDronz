package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.Before;
import uk.ac.ed.inf.Restaurants.MenuItem;
import uk.ac.ed.inf.Restaurants.Restaurant;

public class RestaurantTest extends TestCase {

    private String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private RestClient server = new RestClient(baseUrl);
    private Restaurant[] restaurants;
    private uk.ac.ed.inf.Restaurants.MenuItem pizza1;
    private uk.ac.ed.inf.Restaurants.MenuItem pizza2;
    private uk.ac.ed.inf.Restaurants.MenuItem pizza3;
    private uk.ac.ed.inf.Restaurants.MenuItem pizza4;
    private uk.ac.ed.inf.Restaurants.MenuItem pizza5;

    private uk.ac.ed.inf.Restaurants.MenuItem[] menu1;
    private uk.ac.ed.inf.Restaurants.MenuItem[] menu2;


    @Before
    public void setup(){
        assertNotNull(menu1);
        assertNotNull(menu2);

        pizza1 = new uk.ac.ed.inf.Restaurants.MenuItem("Quad Cheese Deluxe", 1500);
        pizza2 = new uk.ac.ed.inf.Restaurants.MenuItem("Hawaiian Delight", 1000);
        pizza3 = new uk.ac.ed.inf.Restaurants.MenuItem("Barbeque Chicken", 1700);
        pizza4 = new uk.ac.ed.inf.Restaurants.MenuItem("Margarita", 1000);
        pizza5 = new uk.ac.ed.inf.Restaurants.MenuItem("Calzone", 1400);

        menu1 = new uk.ac.ed.inf.Restaurants.MenuItem[3];
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