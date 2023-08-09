package com.yankees88888g.readAPI;

import com.yankees88888g.BotActions;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.Cache.PlayerTime;
import com.yankees88888g.Util.EmbedUtil;
import com.yankees88888g.Util.LinkUtil;
import com.yankees88888g.discordUsers.ManageData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.core.EMCWrapper;
import io.github.emcw.entities.Location;
import io.github.emcw.utils.GsonUtil;
import net.dv8tion.jda.api.JDA;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public static String getLocationDataResponse(PlayerTime player) throws MalformedURLException {
        Location location = player.getLocation();
        return player.getName() + " was last visualable at x= " + location.getX() + " y= " + location.getY() + " z= " + location.getZ() + " <t:" + player.getUnix() + ":R>" + "\n";
    }
}
