package com.yankees88888g.readAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    static Gson gson = new Gson();
    static Type listType = new TypeToken<List<String>>(){}.getType();
    static String domain = "https://api.earthmc.net/v2/aurora/";

    public static Object readAPI(String type, String data) throws IOException {
        return switch (type) {
            case "res" -> readResident(data);
            case "town" -> readTown(data);
            case "nation" -> readNation(data);
            case "residents" -> readResidents();
            case "towns" -> readTowns();
            case "nations" -> readNations();
            default -> null;
        };
    }

    private static Resident readResident(String name) throws IOException {
        return asObject(parse("residents/" + name, JsonObject.class), Resident.class);
    }

    private static Town readTown(String name) throws IOException {
        return asObject(parse("towns/" + name, JsonObject.class), Town.class);
    }

    private static Nation readNation(String name) throws IOException {
        return asObject(parse("nations/" + name, JsonObject.class), Nation.class);
    }

    private static List<String> readResidents() throws IOException {
        return asList(parse("lists/residents/", JsonArray.class));
    }

    private static List<String> readTowns() throws IOException {
        return asList(parse("lists/towns/", JsonArray.class));
    }

    private static List<String> readNations() throws IOException {
        return asList(parse("lists/nations", JsonArray.class));
    }

    private static JsonElement parse(String endpoint, Class<? extends JsonElement> clazz) throws IOException {
        URL url = new URL(domain + endpoint);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return gson.fromJson(reader, clazz);
    }

    private static List<String> asList(JsonElement obj) {
        return gson.fromJson(obj, listType);
    }

    private static <T> T asObject(JsonElement obj, Class<T> customClass) {
        return gson.fromJson(obj, customClass);
    }
}