package com.yankees88888g.readAPI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.Cache.PlayerTime;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPlayersData {
    /*static Gson gson = new Gson();
    static Type listType = new TypeToken<List<Coordinates>>(){}.getType();

    @NotNull
    public static HashMap<String, Coordinates> getPlayersData(){
        URL url;
        InputStreamReader reader;
        try {
            url = new URL("https://emctoolkit.vercel.app/api/aurora/onlineplayers");
            reader = new InputStreamReader(url.openStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Coordinates> coordinatesOfPlayersList = gson.fromJson(reader, listType);
        HashMap<String, Coordinates> coordinatesMap = new HashMap<>();

        for (Coordinates coordinates : coordinatesOfPlayersList) {
            coordinates.unixTime = System.currentTimeMillis() / 1000;
            coordinatesMap.put(coordinates.name, coordinates);
        }

        return coordinatesMap;
    }*/

    public static Map<String, PlayerTime> getPlayersData(EMCMap map) {
        Map<String, Player> online = map.Players.online();
        Map<String, PlayerTime> onlinePlayers = new HashMap<>();

        Set<Map.Entry<String, Player>> entries = online.entrySet();
        for (Map.Entry<String, Player> entry : entries) {
            Player p = entry.getValue();
            if (!p.underground()){
                onlinePlayers.put(entry.getKey(), new PlayerTime(p, System.currentTimeMillis()));
            }
        }

        return onlinePlayers;
    }
}