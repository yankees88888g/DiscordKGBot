package com.yankees88888g.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.readAPI.GetPlayersData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Cache {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /*public static void main(String[] args) throws IOException {
        createCache();
    }*/
    public static void createCache() throws IOException {
        File file = new File("cache.json");
        FileWriter writer = new FileWriter(file);
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
        File file = new File("cache.json");
        Type playerListType = new TypeToken<HashMap<String, Coordinates>>() {}.getType();

        HashMap<String, Coordinates> coordinatesList = gson.fromJson(new FileReader("cache.json"), playerListType);

        FileWriter writer = new FileWriter(file);

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
}
