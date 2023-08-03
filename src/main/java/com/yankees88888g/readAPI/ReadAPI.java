package com.yankees88888g.readAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.nations.Nation;
import com.yankees88888g.APIObjects.residents.Resident;
import com.yankees88888g.APIObjects.towns.Town;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class ReadAPI {
    /*public static void main(String[] args) {
        try {
            readAPI("residents", "paramaribo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public static Object readAPI(String type, String data) throws IOException {
        switch (type) {
            case "res":
                return readResident(data);
            case "town":
                return readTown(data);
            case "nation":
                return readNation(data);
            case "residents" :
                return readResidents();
            case "towns" :
                return readTowns();
            case "nations" :
                return readNations();

        }
        return null;
    }
    private static Resident readResident(String username) throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/residents/" + username);

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        Gson g = new Gson();
        Resident resident = g.fromJson(jsonObject, Resident.class);
        System.out.println(resident.name);
        return resident;
    }

    private static Town readTown(String name) throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/towns/" + name);

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        Gson g = new Gson();
        Town town = g.fromJson(jsonObject, Town.class);
        System.out.println(town.name);
        return town;
    }


    private static Nation readNation(String name) throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/nations/" + name);

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
        Gson g = new Gson();
        Nation nation = g.fromJson(jsonObject, Nation.class);
        System.out.println(nation.name);
        return nation;
    }

    private static List<String> readResidents() throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/lists/residents/");

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonArray jsonObject = new Gson().fromJson(reader, JsonArray.class);
        Gson g = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        List<String> residents = g.fromJson(jsonObject, listType);
        return residents;
    }

    private static List<String> readTowns() throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/lists/towns/");

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonArray jsonObject = new Gson().fromJson(reader, JsonArray.class);
        Gson g = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        List<String> towns = g.fromJson(jsonObject, listType);
        return towns;
    }

    private static List<String> readNations() throws IOException {
        URL url = new URL("https://api.earthmc.net/v2/aurora/lists/nations/");

        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonArray jsonObject = new Gson().fromJson(reader, JsonArray.class);
        Gson g = new Gson();
        Type listType = new TypeToken<List<String>>(){}.getType();
        List<String> nations = g.fromJson(jsonObject, listType);
        return nations;
    }
}
