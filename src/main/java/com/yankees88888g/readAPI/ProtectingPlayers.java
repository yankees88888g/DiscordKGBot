package com.yankees88888g.readAPI;

import com.yankees88888g.BotActions;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.Cache.PlayerTime;
import com.yankees88888g.Util.MathUtil;
import com.yankees88888g.discordUsers.ManageData;
import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Location;
import io.github.emcw.entities.Player;
import io.github.emcw.utils.GsonUtil;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProtectingPlayers {

    public static void protectPlayers(JDA jda, EMCMap map) {
        protectPlayers(jda, map, 250);
    }

    public static void protectPlayers(JDA jda, EMCMap map, Integer radius) {
        File directory = new File("discordUsers/");
        if (!directory.isDirectory()) {
            System.out.println("The specified path is not a directory.");
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            Map<String, PlayerTime> players = GetPlayersData.getCache();

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
                            BotActions.sendDMUpdates(jda, id, stringBuilder.toString(), file, ManageData.readToggles(file, "toggleableEditing"), "protect");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            System.out.println("Error reading the directory.");
        }
    }

    @NotNull
    private static String findAllPlayersDistancesFromAPoint(
            @NotNull Map<String, PlayerTime> players,
            @NotNull Player protectingPlayer,
            int radius
    ) {
        StringBuilder stringBuilder = new StringBuilder();

        for (PlayerTime currentPlayer : players.values()) {
            String playerName = currentPlayer.getName();
            String protectingPlayerName = protectingPlayer.getName();

            if (!Objects.equals(playerName, protectingPlayerName)) {
                Location location = currentPlayer.getLocation();
                Location protectingPlayerLoc = protectingPlayer.getLocation();

                int distance = MathUtil.findShortestDistance(
                        location.getX(), location.getZ(),
                        protectingPlayerLoc.getX(), protectingPlayerLoc.getZ()
                );
                if (distance < radius) {
                    stringBuilder.append(playerName)
                            .append(" is ")
                            .append(distance)
                            .append(" blocks away from ")
                            .append(protectingPlayerName)
                            .append("\n");
                }
            }
        }

        String listStr = stringBuilder.toString();
        if (!listStr.equals(""))
            return listStr;

        return stringBuilder.append("No player is within ")
                .append(radius)
                .append(" blocks of ")
                .append(protectingPlayer.getName())
                .toString();
    }
}