package com.yankees88888g.readAPI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class GetPlayersData {
    static Gson gson = new Gson();
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
    }
}