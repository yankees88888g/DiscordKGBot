package com.yankees88888g.readAPI;

import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.BotActions;
import com.yankees88888g.Math;
import com.yankees88888g.discordUsers.ManageData;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProtectingPlayers {

    public static void protectPlayers(JDA jda) {
        protectPlayers(jda, 250);
    }

    public static void protectPlayers(JDA jda, Integer radius) {
        File directory = new File("discordUsers/");
        if (!directory.isDirectory()) {
            System.out.println("The specified path is not a directory.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            HashMap<String, Coordinates> playersCoordinates = GetPlayersData.getPlayersData();

            // Process each file in the directory
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        if (!ManageData.readToggles(file, "toggleableProtecting")) return;
                        List<String> protecting = ManageData.readData(file, "protect");
                        if (protecting != null) {
                            String id = ManageData.getId(file);
                            StringBuilder stringBuilder = new StringBuilder();

                            for (String s : protecting) {
                                if (playersCoordinates.containsKey(s)) {
                                    Coordinates coordinates = playersCoordinates.get(s);
                                    stringBuilder.append(findAllPlayersDistancesFromAPoint(
                                            playersCoordinates, coordinates, radius
                                    ));
                                } else {
                                    stringBuilder.append("The player you are protecting, ")
                                            .append(s).append(", is offline");
                                }
                            }

                            System.out.println(ManageData.readToggles(file, "toggleableEditing"));
                            BotActions.sendDMUpdates(jda, id, stringBuilder.toString(), file, ManageData.readToggles(file, "toggleableEditing"), "protect");
                        }
                    } catch (IOException e) { throw new RuntimeException(e); }
                }
            }
        } else {
            System.out.println("Error reading the directory.");
        }
    }

    @NotNull
    private static String findAllPlayersDistancesFromAPoint(HashMap<String, Coordinates> playersCoordinates, Coordinates protectingCoordinates, int protectionRadius) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Coordinates> i : playersCoordinates.entrySet()) {
            Coordinates coordinates = i.getValue();
            if (!coordinates.underground) {
                if (!Objects.equals(coordinates.name, protectingCoordinates.name)) {
                    int distance = Math.findShortestDistance(coordinates.x, coordinates.z, protectingCoordinates.x, protectingCoordinates.z);
                    if (distance < protectionRadius)
                        stringBuilder.append(coordinates.name)
                                .append(" is ")
                                .append(distance)
                                .append(" blocks away from ")
                                .append(protectingCoordinates.name)
                                .append("\n");
                }
            }
        }

        String listStr = stringBuilder.toString();
        if (!listStr.equals(""))
            return listStr;

        return stringBuilder.append("No player is within ")
                .append(protectionRadius)
                .append(" blocks of ")
                .append(protectingCoordinates.name)
                .toString();
    }
}