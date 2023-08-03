package com.yankees88888g.readAPI;

import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.BotActions;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.discordUsers.ManageData;
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
                        StringBuilder stringBuilder = new StringBuilder();
                        if(!ManageData.readToggles(file, "toggleableTracking")) return;

                        List<String> tracking = ManageData.readData(file, "track");

                        if(tracking != null) {
                            for (String s : tracking) {
                                if (playersCoordinates.containsKey(s)) {
                                    Coordinates coordinates = playersCoordinates.get(s);
                                    if (!coordinates.underground) {
                                        stringBuilder.append(coordinates.name).append(" is at x= ")
                                                .append(coordinates.x).append(" y= ")
                                                .append(coordinates.y).append(" z= ")
                                                .append(coordinates.z).append("<t:").append(coordinates.unixTime).append(":R>").append("\n").append("\n");
                                    } else {
                                        if (ManageData.readToggles(file, "unknownLocationUpdates")){
                                            stringBuilder.append(coordinates.name)
                                                .append(" is underground")
                                                .append("\n");
                                        } else {
                                            Coordinates cachedCoordinates = Cache.getFromCache(coordinates.name);
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