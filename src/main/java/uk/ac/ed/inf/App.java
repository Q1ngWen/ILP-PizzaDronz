package uk.ac.ed.inf;

import uk.ac.ed.inf.DronePath.Drone;
import uk.ac.ed.inf.DronePath.LngLat;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        System.out.println("I have a fucking headache that wont go away wow :DDD");

//        Scanner in = new Scanner(System.in);
//        String s = in.nextLine();

        String date = args[0];
        String baseUrl = args[1];
        System.out.println(date);
        System.out.println(baseUrl);

        RestClient server = new RestClient<>(baseUrl);
        LngLat appletonTower = new LngLat(-3.186874, 55.944494);
        Drone drone = new Drone(appletonTower);
        drone.deliverOrders(server, date);
    }
}
