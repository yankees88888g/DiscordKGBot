package com.yankees88888g.readAPI;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.APIObjects.OnlinePlayers;
import com.yankees88888g.BotActions;
import com.yankees88888g.discordUsers.ManageData;
/*import io.github.emcw.core.EMCMap;
import io.github.emcw.core.EMCWrapper;
import io.github.emcw.entities.Nation;
import io.github.emcw.entities.Player;
import io.github.emcw.exceptions.MissingEntryException;*/
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingPlayers {

    public static void trackPlayers(JDA jda) {
        HashMap<String, Coordinates> playersCoordinates = GetPlayersData.getPlayersData();

        File directory = new File("discordUsers/");

        if (!directory.isDirectory()) {
            System.out.println("The specified path is not a directory.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            // Process each file in the directory
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        StringBuilder stringBuilder = new StringBuilder("");
                        if(!ManageData.readToggles(file, "toggleableTracking")) return;
                        List<String> tracking = ManageData.readData(file, "track");
                        if(tracking != null) {
                            for (String s : tracking) {
                                if (playersCoordinates.containsKey(s)) {
                                    Coordinates coordinates = playersCoordinates.get(s);
                                    if (!coordinates.underground) {
                                        stringBuilder.append(coordinates.name + " is at x= " + coordinates.x + " y= " + coordinates.y + " z= " + coordinates.z + "\n");
                                    } else {
                                        stringBuilder.append(coordinates.name + " is underground" + "\n");
                                    }
                                } else {
                                    stringBuilder.append(s + " is offline" + "\n");
                                }
                            }
                            System.out.println(stringBuilder);
                            BotActions.sendDMUpdates(jda, ManageData.getId(file), stringBuilder.toString(), file, ManageData.readToggles(file, "toggleableEditing"), "track");
                        }
                    } catch (IOException e) {throw new RuntimeException(e);}

                }
            }
        } else {
            System.out.println("Error reading the directory.");
        }


    }


    /*static EMCWrapper emc = new EMCWrapper();
    static EMCMap aurora;



    public static void main(String[] args) throws MissingEntryException {
        aurora = emc.getAurora();
        //Nation exampleNation = aurora.Nations.single("cuba");
        for(int i = 0; i < 100; i++) {
            Map<String, Player> playerMap = aurora.Players.get("yankees88888g");
            Player player = playerMap.get("yankees88888g");
            Coordinates coordinates = new Coordinates(player.getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.underground());
            System.out.println(coordinates.z);
            //System.out.println(Aurora.Players.all());
        }
    }*/
}
