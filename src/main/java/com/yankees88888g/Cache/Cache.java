package com.yankees88888g.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.readAPI.GetPlayersData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static Type playerListType = new TypeToken<HashMap<String, Player>>() {}.getType();

    public static void createCache(EMCMap map) throws IOException {
        FileWriter writer = new FileWriter(getFile());
        Map<String, Player> playerMap = new HashMap<>();

        for (Player player : GetPlayersData.getPlayersData(map).values()) {
            System.out.println(player.aboveGround());
            if (player.aboveGround())
                playerMap.put(player.getName(), player);
        }
        System.out.println(playerMap.get("yankees88888g").getLocation().getX());
        writer.write(gson.toJson(playerMap));
        writer.close();
    }

    public static void updateCache(EMCMap map) throws IOException {


        Map<String, Player> playerMap = gson.fromJson(new FileReader(getFile()), playerListType);
        System.out.println(playerMap.get("yankees88888g").getLocation().getX());

        FileWriter writer = new FileWriter(getFile());

        Map<String, Player> players = GetPlayersData.getPlayersData(map);

        for (Player player : players.values()) {
            if (player.aboveGround()) {
                players.remove(player.getName());
                playerMap.put(player.getName(), player);
            }
        }
        writer.write(gson.toJson(playerMap));
        writer.close();
    }

    public static Coordinates getFromCache(String name) throws FileNotFoundException {

        HashMap<String, Coordinates> coordinatesList = gson.fromJson(new FileReader(getFile()), playerListType);
        return coordinatesList.get(name);
    }

    private static File getFile(){
        return new File("cache.json");
    }
}
