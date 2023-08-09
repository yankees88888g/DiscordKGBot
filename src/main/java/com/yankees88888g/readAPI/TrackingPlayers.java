package com.yankees88888g.readAPI;

import com.google.gson.reflect.TypeToken;
import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.BotActions;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.Cache.PlayerTime;
import com.yankees88888g.discordUsers.ManageData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.core.EMCWrapper;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import io.github.emcw.exceptions.MissingEntryException;
import io.github.emcw.utils.GsonUtil;
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingPlayers {
    static EMCWrapper emc = new EMCWrapper();

    public static void trackPlayers(JDA jda, EMCMap map) {
        Map<String, PlayerTime> players = GetPlayersData.getCache();
        Map<String, PlayerTime> onlinePlayersData = GetPlayersData.getPlayersData(map);

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
                        StringBuilder stringBuilder = new StringBuilder();
                        if(!ManageData.readToggles(file, "toggleableTracking")) return;

                        List<String> tracking = ManageData.readData(file, "track");
                        if(tracking != null) {
                            for (String s : tracking) {
                                if (onlinePlayersData.containsKey(s)) {
                                    stringBuilder.append(getLocationDataResponse(onlinePlayersData.get(s)));
                                } else if (players.containsKey(s)) {
                                    if (!players.get(s).underground()) {
                                        stringBuilder.append(getLocationDataResponse(players.get(s)));
                                        /*stringBuilder.append(players.get(s).getName()).append(" was last visualable at x= ")
                                                .append(location.getX()).append(" y= ")
                                                .append(location.getY()).append(" z= ")
                                                .append(location.getZ()).append(" <t:").append(players.get(s).getUnix()).append(":R>").append("\n");*/
                                    }
                                } else {
                                    stringBuilder.append(s).append(" is offline").append("\n");
                                }
                            }
                            BotActions.sendDMUpdates(jda, ManageData.getId(file), stringBuilder.toString(), file, ManageData.readToggles(file, "toggleableEditing"), "track");
                        }
                    } catch (IOException e) { throw new RuntimeException(e); }

                }
            }
        } else {
            System.out.println("Error reading the directory.");
        }


    }

    public static String getLocationDataResponse(PlayerTime player) {
        Location location = player.getLocation();
        return player.getName() + " was last visualable at x= " + location.getX() + " y= " + location.getY() + " z= " + location.getZ() + " <t:" + player.getUnix() + ":R>" + "\n";
    }
}