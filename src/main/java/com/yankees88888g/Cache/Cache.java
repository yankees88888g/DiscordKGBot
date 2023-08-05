package com.yankees88888g.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.readAPI.GetPlayersData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Player;
import io.github.emcw.utils.GsonUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static Type playerListType = new TypeToken<Map<String, PlayerTime>>() {}.getType();

    public static void createCache(EMCMap map) throws IOException {
        Map<String, PlayerTime> playerMap = new HashMap<>();

        for (Player player : GetPlayersData.getPlayersData(map).values()) {
            if (player.getLocation() != null)
                if (player.getLocation().valid())
                    playerMap.put(player.getName(), new PlayerTime(player, System.currentTimeMillis() / 1000));
        }
        System.out.println(GsonUtil.serialize(playerMap));
        writeToFile(getFile().getPath(), GsonUtil.serialize(playerMap));
        System.out.println(playerMap.get("yankees88888g").getLocation().getX());
    }

    public static void updateCache(EMCMap map) throws IOException {

        Map<String, PlayerTime> playerMap = GsonUtil.deserialize(getFileContents(getFile()), playerListType);
        System.out.println(playerMap.get("yankees88888g").getLocation().getX());

        FileWriter writer = new FileWriter(getFile());

        Map<String, Player> players = GetPlayersData.getPlayersData(map);

        for (Player player : players.values()) {
            if (player.aboveGround()) {
                players.remove(player.getName());
                playerMap.put(player.getName(), new PlayerTime(player, System.currentTimeMillis() / 1000));
            }
        }
        writer.write(GsonUtil.serialize(playerMap));
        writer.close();
    }

    public static Coordinates getFromCache(String name) throws FileNotFoundException {

        HashMap<String, Coordinates> coordinatesList = gson.fromJson(new FileReader(getFile()), playerListType);
        return coordinatesList.get(name);
    }

    private static File getFile(){
        return new File("cache.json");
    }
    public static void writeToFile(String name, String data) {
        try (FileWriter writer = new FileWriter(name)) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getFileContents(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
