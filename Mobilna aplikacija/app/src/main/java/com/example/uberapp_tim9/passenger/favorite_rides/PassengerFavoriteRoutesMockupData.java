package com.example.uberapp_tim9.passenger.favorite_rides;

import com.example.uberapp_tim9.model.Location;
import com.example.uberapp_tim9.model.Path;
import com.example.uberapp_tim9.model.dtos.FavoritePathDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerFavoriteRoutesMockupData {
    public static List<FavoritePathDTO> getPaths() {
        ArrayList<FavoritePathDTO> paths = new ArrayList<FavoritePathDTO>();

        Map<String, Location> locations = new HashMap<String, Location>();
        locations.put("departure", new Location(44.7866, 20.4489, "Bulevar Mihajla Pupina 2"));
        locations.put("destination", new Location(44.8025, 20.4651, "Bulevar Cara Lazara 23"));

        paths.add(new FavoritePathDTO(1, "Kuća do posla", locations, "Standard", false, false));
        paths.add(new FavoritePathDTO(2, "Posao do kuće", locations, "Luksuzno", false, true));
        paths.add(new FavoritePathDTO(3, "Kuća do škole", locations, "Kombi", true, false));
        paths.add(new FavoritePathDTO(4, "Škola do kuće", locations, "Standard", true, true));

        return paths;
    }
}
