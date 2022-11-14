package com.example.uberapp_tim9.ride_history;

import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.model.Ride;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriverRideHistoryMockupData {

    public static List<Ride> getRides() {
        ArrayList<Ride> rides = new ArrayList<Ride>();
        Ride r1 = new Ride(1, LocalDateTime.of(2022, Month.AUGUST, 20, 15, 13),
                LocalDateTime.of(2022, Month.AUGUST, 20, 15, 45),
                Arrays.asList(new Passenger[]{new Passenger(), new Passenger()}));

        Ride r2 = new Ride(2, LocalDateTime.of(2022, Month.AUGUST, 25, 18, 04),
                LocalDateTime.of(2022, Month.AUGUST, 25, 18, 15),
                Arrays.asList(new Passenger[]{new Passenger()}));

        Ride r3 = new Ride(1, LocalDateTime.of(2022, Month.AUGUST, 29, 20, 14),
                LocalDateTime.of(2022, Month.AUGUST, 29, 20, 51),
                Arrays.asList(new Passenger[]{new Passenger(), new Passenger(), new Passenger()}));

        rides.add(r1);
        rides.add(r2);
        rides.add(r3);

        return rides;
    }


    public static List<Review> getRideReviews() {
        ArrayList<Review> reviews = new ArrayList<Review>();
        Review r1 = new Review(5, "Plitak");
        Review r2 = new Review(5, "Potok");
        reviews.add(r1);
        reviews.add(r2);
        return reviews;
    }

    public static List<Passenger> getPassengers() {
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        Passenger p1 = new Passenger("Marko", "Marković");
        Passenger p2 = new Passenger("Mika", "Mikić");
        passengers.add(p1);
        passengers.add(p2);
        return passengers;
    }


    public static Ride getRideStats() {
        return new Ride(1500, Arrays.asList(new Path[]{new Path(2), new Path(5)}));
    }

}
