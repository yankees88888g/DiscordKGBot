package com.yankees88888g.discordUsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yankees88888g.discordUsers.objects.DiscordUser;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class ManageData {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createData(User user, File usersFile, String username, List<String> messageIDs) throws IOException {
        System.out.println(messageIDs);

        try (FileWriter writer = new FileWriter(usersFile)) {
            writer.write(gson.toJson(new DiscordUser(
                    username, user.getId(),
                    null, null, messageIDs,
                    true, true, true, false
            )));
        }
    }

    public static String editData(User user, File usersFile, String type, String player, List<String> ids) throws IOException {
        DiscordUser discordUser = deserializeUser(usersFile);
        FileWriter writer = new FileWriter(usersFile);
        String response = null;

        switch (type) {
            case "track" -> {
                if (discordUser.tracking == null) {
                    List<String> newTrackingList = new ArrayList<>();
                    newTrackingList.add(player);
                    writer.write(serializeUser(discordUser.username, user.getId(),
                            newTrackingList, discordUser.protecting, discordUser.editableMessage,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    ));
                } else {
                    discordUser.tracking.add(player);
                    writer.write(serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    ));
                }
                response = "successfully started tracking " + player;
            }
            case "protect" -> {
                if (discordUser.protecting == null) {
                    List<String> newProtectingList = new ArrayList<>();
                    newProtectingList.add(player);
                    writer.write(serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, newProtectingList, discordUser.editableMessage,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    ));

                } else {
                    discordUser.protecting.add(player);
                    writer.write(serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    ));
                }
                response = "successfully started protecting " + player;
            }
            case "toggleTracking" -> {
                writer.write(serializeUser(discordUser.username, user.getId(),
                                discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                                !discordUser.toggleableTracking, discordUser.toggleableProtecting,
                                discordUser.editMessage, discordUser.unknownLocationUpdates
                ));
                if (!discordUser.toggleableTracking) response = "starting tracking players";
                else response = "stopped tracking players";
            }
            case "toggleProtecting" -> {
                writer.write(serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                            discordUser.toggleableTracking, !discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    ));
                if (!discordUser.toggleableTracking) response = "starting protecting players";
                else response = "stopped protecting players";
            }
            case "toggleMessageEditing" -> writer.write(
                    serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            !discordUser.editMessage, discordUser.unknownLocationUpdates
                    )
            );
            case "toggleEditableMessage" -> writer.write(
                    serializeUser(discordUser.username, user.getId(),
                            discordUser.tracking, discordUser.protecting, ids,
                            discordUser.toggleableTracking, discordUser.toggleableProtecting,
                            discordUser.editMessage, discordUser.unknownLocationUpdates
                    )
            );
        }
        writer.close();
        return response;
    }

    public static List<String> readData(File usersFile, String type) throws IOException {
        DiscordUser discordUser = deserializeUser(usersFile);
        return switch (type) {
            case "track" -> discordUser.tracking;
            case "protect" -> discordUser.protecting;
            default -> null;
        };
    }

    public static Boolean readToggles(File usersFile, String type) throws IOException {
        DiscordUser discordUser = deserializeUser(usersFile);
        return switch (type) {
            case "toggleableTracking" -> discordUser.toggleableTracking;
            case "toggleableProtecting" -> discordUser.toggleableProtecting;
            case "toggleableEditing" -> discordUser.editMessage;
            case "unknownLocationUpdates" -> discordUser.unknownLocationUpdates;
            default -> null;
        };
    }

    public static String getEditableMessage(File usersFile, String type) throws IOException {
        DiscordUser discordUser = deserializeUser(usersFile);
        return switch (type) {
            case "track" -> discordUser.editableMessage.get(0);
            case "protect" -> discordUser.editableMessage.get(1);
            default -> null;
        };
    }

    public static String getId(File usersFile) {
        DiscordUser discordUser = deserializeUser(usersFile);
        return discordUser.discordID;
    }

    public static String deleteData(User user, File usersFile, String type, String player) throws IOException {
        DiscordUser discordUser = deserializeUser(usersFile);
        switch (type) {
            case "track" -> discordUser.tracking.remove(player);
            case "protect" -> discordUser.protecting.remove(player);
        }

        try (FileWriter writer = new FileWriter(usersFile)) {
            writer.write(serializeUser(user.getId(), discordUser));
        }
        return "Stopped " + type + "ing " + player;
    }
    public static DiscordUser getUser(@NotNull File usersFile) {
        return deserializeUser(usersFile);
    }

    private static DiscordUser deserializeUser(@NotNull File usersFile) {
        try {
            String data = Files.readString(Paths.get(usersFile.getPath()));
            return gson.fromJson(gson.fromJson(data, JsonObject.class), DiscordUser.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializeUser(String userId, @NotNull DiscordUser discordUser) {
        return serializeUser(
                discordUser.username, userId,
                discordUser.tracking, discordUser.protecting, discordUser.editableMessage,
                discordUser.toggleableTracking, discordUser.toggleableProtecting,
                discordUser.editMessage, discordUser.unknownLocationUpdates
        );
    }

    private static String serializeUser(
            String userId, String username,
            List<String> tracking, List<String> protecting, List<String> editableMsg,
            Boolean toggleableTracking, Boolean toggleableProtecting, Boolean edit, Boolean unknownLocUpdates
    ) {
        return gson.toJson(new DiscordUser(
                username, userId,
                tracking, protecting, editableMsg,
                toggleableTracking, toggleableProtecting, edit, unknownLocUpdates
        ));
    }
}