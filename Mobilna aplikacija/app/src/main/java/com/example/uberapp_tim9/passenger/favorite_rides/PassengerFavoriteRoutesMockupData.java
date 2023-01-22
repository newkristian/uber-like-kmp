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
        ArrayList<FavoritePathDTO> paths = new ArrayList<>();

        Map<String, Location> locations1 = new HashMap<>();
        locations1.put("departure", new Location(45.235866, 19.807387, "Djordja Mikeša 2"));
        locations1.put("destination", new Location(45.247309, 19.796717, "Andje Rankovic 2"));

        Map<String, Location> locations2 = new HashMap<>();
        locations2.put("departure", new Location(45.259711, 19.809787, "Veselina Maslese 62"));
        locations2.put("destination", new Location(45.261421, 19.823026, "Jovana Hranilovica 2"));

        Map<String, Location> locations3 = new HashMap<>();
        locations3.put("departure", new Location(45.265435, 19.847805, "Bele njive 24"));
        locations3.put("destination", new Location(45.255521, 19.845071, "Njegoseva 2"));

        Map<String, Location> locations4 = new HashMap<>();
        locations4.put("departure", new Location(45.249241, 19.852152, "Stevana Musica 20"));
        locations4.put("destination", new Location(45.242509, 19.844632, "Boska Buhe 10A"));

        paths.add(new FavoritePathDTO(1, "Kuća do posla", locations1, "Standard", false, false));
        paths.add(new FavoritePathDTO(2, "Posao do kuće", locations2, "Luksuzno", false, true));
        paths.add(new FavoritePathDTO(3, "Kuća do škole", locations3, "Kombi", true, false));
        paths.add(new FavoritePathDTO(4, "Škola do kuće", locations4, "Standard", true, true));

        return paths;
    }
}
