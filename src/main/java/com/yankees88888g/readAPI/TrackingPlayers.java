package com.yankees88888g.readAPI;

import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.BotActions;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.discordUsers.ManageData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.core.EMCWrapper;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import io.github.emcw.exceptions.MissingEntryException;
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingPlayers {
    static EMCWrapper emc = new EMCWrapper();

    public static void trackPlayers(JDA jda, EMCMap map) {
        Map<String, Player> players = GetPlayersData.getPlayersData(map);

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
                                if (players.containsKey(s)) {
                                    Location location = players.get(s).getLocation();
                                    if (players.get(s).aboveGround()) {
                                        stringBuilder.append(players.get(s).getName()).append(" is at x= ")
                                                .append(location.getX()).append(" y= ")
                                                .append(location.getY()).append(" z= ")
                                                .append(location.getZ()).append("<t:");//.append(location.unixTime).append(":R>").append("\n");
                                    } else {
                                        if (ManageData.readToggles(file, "unknownLocationUpdates")){
                                            stringBuilder.append(players.get(s).getName())
                                                .append(" is underground")
                                                .append("\n");
                                        } else {
                                            Coordinates cachedCoordinates = Cache.getFromCache(players.get(s).getName());
                                            stringBuilder.append(cachedCoordinates.name).append(" was last visualable at x= ")
                                                    .append(cachedCoordinates.x).append(" y= ")
                                                    .append(cachedCoordinates.y).append(" z= ")
                                                    .append(cachedCoordinates.z).append("<t:").append(cachedCoordinates.unixTime).append(":R>").append("\n");
                                        }
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






    /*public static void mawin(EMCMap emc) throws MissingEntryException {
        //Nation exampleNation = aurora.Nations.single("cuba");
        for(int i = 0; i < 100; i++) {
            Map<String, Player> playerMap = map.Players.get("yankees88888g");
            Player player = playerMap.get("yankees88888g");
            Coordinates coordinates = new Coordinates(player.getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.underground(),System.currentTimeMillis() / 1000);
            System.out.println(coordinates.z);
            //System.out.println(Aurora.Players.all());
        }
    }*/
}