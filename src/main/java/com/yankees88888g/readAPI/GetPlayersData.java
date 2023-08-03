package com.yankees88888g.readAPI;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class GetPlayersData {
    public static HashMap<String, Coordinates> getPlayersData(){
        URL url;
        try {
            url = new URL("https://emctoolkit.vercel.app/api/aurora/onlineplayers");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gson g = new Gson();
        Type listType = new TypeToken<List<Coordinates>>(){}.getType();
        List<Coordinates> coordinatesOfPlayersList = g.fromJson(reader, listType);
        HashMap<String, Coordinates> coordinatesMap = new HashMap<>();
        for (Coordinates coordinates : coordinatesOfPlayersList) {
            coordinates.unixTime = System.currentTimeMillis() / 1000;
            coordinatesMap.put(coordinates.name, coordinates);
        }
        return coordinatesMap;
    }
}
