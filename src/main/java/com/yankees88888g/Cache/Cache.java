package com.yankees88888g.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.readAPI.GetPlayersData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import io.github.emcw.utils.GsonUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    static Map<String, PlayerTime> players = new ConcurrentHashMap<>();

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Type playerListType = new TypeToken<Map<String, PlayerTime>>() {
    }.getType();

    public static void createCache(EMCMap map) throws IOException {
        Map<String, PlayerTime> playerMap = new HashMap<>();

        for (Player player : GetPlayersData.getPlayersData(map).values()) {
            if (player.getLocation() != null)
                if (!player.getLocation().isDefault())
                    playerMap.put(player.getName(), new PlayerTime(player, System.currentTimeMillis()));
        }
        writeToFile(getFile().getPath(), GsonUtil.serialize(playerMap));

    }

    public static void updateCache(EMCMap map) throws IOException {
        System.out.println("Updating cache");
        Map<String, Player> ops = map.Players.online();

        ops.forEach((key, p) -> {
            if (!p.underground())
                players.put(key, new PlayerTime(p, System.currentTimeMillis()));
            });
        //System.out.println(GsonUtil.serialize(players.get("yankees88888g")));
        writeToFile("cache.json", GsonUtil.serialize(players));
    }


    public static PlayerTime getFromCache(String name) throws FileNotFoundException {

        HashMap<String, PlayerTime> playerTimeHashMap = GsonUtil.deserialize(getFileContents("cache.json"), playerListType);
        return playerTimeHashMap.get(name);
    }

    private static File getFile() {
        return new File("cache.json");
    }

    public static void writeToFile(String name, String data) {
        try (FileWriter writer = new FileWriter(name)) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static String getFileContents(String name) {
        try {
            return Files.readString(Paths.get(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}