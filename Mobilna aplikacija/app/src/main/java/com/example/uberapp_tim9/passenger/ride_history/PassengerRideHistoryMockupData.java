package com.example.uberapp_tim9.passenger.ride_history;

import com.example.uberapp_tim9.model.Driver;
import com.example.uberapp_tim9.model.Passenger;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.Review;
import com.example.uberapp_tim9.model.Ride;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassengerRideHistoryMockupData {
    public static List<Ride> getRides() {
        ArrayList<Ride> rides = new ArrayList<Ride>();

        Ride r1 = new Ride(1, LocalDateTime.of(2022, Month.AUGUST, 30, 16, 48),
                LocalDateTime.of(2022, Month.AUGUST, 30, 16, 59),
                430,
                Arrays.asList(new Path[]{new Path(0.96)}),
                Arrays.asList(new Passenger[]{new Passenger()}));

        Ride r2 = new Ride(2, LocalDateTime.of(2022, Month.AUGUST, 29, 20, 14),
                LocalDateTime.of(2022, Month.AUGUST, 29, 20, 51),
                350,
                Arrays.asList(new Path[]{new Path(1), new Path(0.2), new Path(0.5)}),
                Arrays.asList(new Passenger[]{new Passenger(), new Passenger(),new Passenger()}));

        Ride r3 = new Ride(3, LocalDateTime.of(2022, Month.AUGUST, 25, 18, 04),
                LocalDateTime.of(2022, Month.AUGUST, 25, 18, 15),
                1150,
                Arrays.asList(new Path[]{new Path(2.4)}),
                Arrays.asList(new Passenger[]{new Passenger()}));

        Ride r4 = new Ride(4, LocalDateTime.of(2022, Month.AUGUST, 20, 15, 13),
                LocalDateTime.of(2022, Month.AUGUST, 20, 15, 45),
                850,
                Arrays.asList(new Path[]{new Path(0.4), new Path(0.3)}),
                Arrays.asList(new Passenger[]{new Passenger(), new Passenger()}));

        rides.add(r1);
        rides.add(r2);
        rides.add(r3);
        rides.add(r4);

        return rides;
    }


    public static List<Review> getRideReviews() {
        ArrayList<Review> reviews = new ArrayList<Review>();
        Review r1 = new Review(5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", true);
        Review r2 = new Review(5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", false);
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

    public static Driver getDriver() {
        Driver driver = new Driver("Mare", "Marić");
        return driver;
    }

    public static Ride getRideStats() {
        return new Ride(1500, Arrays.asList(new Path[]{new Path(2), new Path(5)}));
    }
}
