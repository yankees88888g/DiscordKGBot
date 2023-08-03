package com.yankees88888g.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.readAPI.GetPlayersData;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Cache {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createCache() throws IOException {
        FileWriter writer = new FileWriter(getFile());
        HashMap<String, Coordinates> coordinatesList = new HashMap<>();

        for (Coordinates coordinates : GetPlayersData.getPlayersData().values()) {
            System.out.println(coordinates.underground);
            if (!coordinates.underground)
                coordinatesList.put(coordinates.name, coordinates);
        }
        writer.write(gson.toJson(coordinatesList));
        writer.close();
    }

    public static void updateCache() throws IOException {
        Type playerListType = new TypeToken<HashMap<String, Coordinates>>() {}.getType();

        HashMap<String, Coordinates> coordinatesList = gson.fromJson(new FileReader(getFile()), playerListType);
        System.out.println(coordinatesList.get("yankees88888g").x);

        FileWriter writer = new FileWriter(getFile());

        HashMap<String, Coordinates> playersCoords = GetPlayersData.getPlayersData();

        for (Coordinates coordinates : playersCoords.values()) {
            if (!coordinates.underground) {
                coordinatesList.remove(coordinates.name);
                coordinatesList.put(coordinates.name, coordinates);
            }
        }
        writer.write(gson.toJson(coordinatesList));
        writer.close();
    }

    public static Coordinates getFromCache(String name) throws FileNotFoundException {
        Type playerListType = new TypeToken<HashMap<String, Coordinates>>() {}.getType();

        HashMap<String, Coordinates> coordinatesList = gson.fromJson(new FileReader(getFile()), playerListType);
        return coordinatesList.get(name);
    }

    private static File getFile(){
        return new File("cache.json");
    }
}
