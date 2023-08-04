package com.yankees88888g.readAPI;

import com.yankees88888g.APIObjects.Coordinates;
import com.yankees88888g.BotActions;
import com.yankees88888g.Math;
import com.yankees88888g.discordUsers.ManageData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProtectingPlayers {

    public static void protectPlayers(JDA jda, EMCMap map) {
        protectPlayers(jda, 250, map);
    }

    public static void protectPlayers(JDA jda, Integer radius, EMCMap map) {
        File directory = new File("discordUsers/");
        if (!directory.isDirectory()) {
            System.out.println("The specified path is not a directory.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            Map<String, Player> players = GetPlayersData.getPlayersData(map);

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
                                if (players.containsKey(s)) {
                                    //Location location = players.get(s).getLocation();
                                    stringBuilder.append(findAllPlayersDistancesFromAPoint(
                                            players, players.get(s), radius
                                    ));
                                } else {
                                    stringBuilder.append("The player you are protecting, ")
                                            .append(s).append(", is offline\n");
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
    private static String findAllPlayersDistancesFromAPoint(Map<String, Player> playersCoordinates, Player protectingPlayer, int protectionRadius) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Player> i : playersCoordinates.entrySet()) {
            Location location = i.getValue().getLocation();
            if (i.getValue().aboveGround()) {
                if (!Objects.equals(i.getValue().getName(), protectingPlayer.getName())) {
                    int distance = Math.findShortestDistance(location.getX(), location.getZ(), protectingPlayer.getLocation().getX(), protectingPlayer.getLocation().getZ());
                    if (distance < protectionRadius)
                        stringBuilder.append(i.getValue().getName())
                                .append(" is ")
                                .append(distance)
                                .append(" blocks away from ")
                                .append(protectingPlayer.getName())
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
                .append(protectingPlayer.getName())
                .toString();
    }
}