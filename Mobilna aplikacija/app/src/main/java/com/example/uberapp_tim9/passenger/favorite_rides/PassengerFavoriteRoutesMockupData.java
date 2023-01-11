package com.example.uberapp_tim9.passenger.favorite_rides;

import com.example.uberapp_tim9.model.Location;
import com.example.uberapp_tim9.model.Path;

import java.util.ArrayList;
import java.util.List;

public class PassengerFavoriteRoutesMockupData {
    public static List<Path> getPaths() {
        ArrayList<Path> paths = new ArrayList<Path>();

        paths.add(new Path(new Location(44.7866, 20.4489, "Bulevar Mihajla Pupina 2"), new Location(44.8025, 20.4651, "Bulevar Cara Lazara 23")));
        paths.add(new Path(new Location(44.7866, 20.4489, "Bulevar Oslobođenja 42"), new Location(44.8025, 20.4651, "Bulevar Cara Lazara 23")));
        paths.add(new Path(new Location(44.7866, 20.4489, "Resavska 33"), new Location(44.8025, 20.4651, "Vojvode Stepe 45")));

        return paths;
    }
}
