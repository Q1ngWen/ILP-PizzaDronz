package uk.ac.ed.inf;

public class mainTest {
    public static void main(String[] args){
        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        RestClient server = RestClient.getRestClientInstance(baseUrl);
        CentralArea[] centralAreas = new CentralArea().getCoordinates(server);
        LngLat cood1 = new LngLat(-3.192473, 55.946383);
        LngLat cood2 = new LngLat(-3.012837192, 55.9913912873);
//        System.out.println(cood1.inCentralArea(server));
//        System.out.println(cood2.inCentralArea(server));
//        System.out.println(cood1.distanceTo(cood2));
//        System.out.println(cood1.closeTo(cood2));
//        System.out.println(cood1.nextPosition(CompassDirection.NORTH));
////        printing central area coordinates
//        for (int i = 0; i < centralAreas.length; i++) {
//            System.out.println("Location: " + centralAreas[i].getName());
//            System.out.println("(" + centralAreas[i].getLongitude() + ", " + centralAreas[i].getLatitude() + ")");
//        }

        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(server);
        for (int i = 0; i < restaurants.length; i++) {
            System.out.println(restaurants[i].getName());
            System.out.println(restaurants[i].getMenu());
        }
    }
}
