package com.yankees88888g.discordUsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yankees88888g.discordUsers.objects.DiscordUser;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class ManageData {
    public static void createData(User user, File usersFile, String username, List<String> messageIDs) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(messageIDs);
        FileWriter fileWriter = new FileWriter(usersFile);
        fileWriter.write(g.toJson(new DiscordUser(username, user.getId(), null, null,  messageIDs,true, true, true)));
        fileWriter.close();
    }


    public static void editData(User user, File usersFile, String type, String player, List<String> ids) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);
        switch (type){
            case "track":
                FileWriter trackingWriter = new FileWriter(usersFile);
                if(discordUser.tracking == null){
                    List<String> newTrackingList = new ArrayList<>();
                    newTrackingList.add(player);
                    trackingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), newTrackingList, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                } else {
                    discordUser.tracking.add(player);
                    trackingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                }
                trackingWriter.close();
                break;
            case "protect":
                FileWriter protectingWriter = new FileWriter(usersFile);
                if (discordUser.protecting == null){
                    List<String> newProtectingList = new ArrayList<>();
                    newProtectingList.add(player);
                    protectingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, newProtectingList, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                } else {
                    discordUser.protecting.add(player);
                    protectingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                }
                protectingWriter.close();
                break;
            case "toggleTracking":
                FileWriter toggleTrackingWriter = new FileWriter(usersFile);
                toggleTrackingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, !discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                toggleTrackingWriter.close();
                break;
            case "toggleProtecting":
                FileWriter toggleProtectingWriter = new FileWriter(usersFile);
                toggleProtectingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, !discordUser.toggleableProtecting, discordUser.editMessage)));
                toggleProtectingWriter.close();
                break;
            case "toggleMessageEditing":
                FileWriter toggleMessageWriter = new FileWriter(usersFile);
                toggleMessageWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, !discordUser.editMessage)));
                toggleMessageWriter.close();
                break;
            case "toggleEditableMessage":
                FileWriter toggleEditableWriter = new FileWriter(usersFile);
                toggleEditableWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, ids, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                toggleEditableWriter.close();
                break;
        }
    }

    public static List<String> readData(File usersFile, String type) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);
        switch (type) {
            case "track":
                return discordUser.tracking;
            case "protect":
                return discordUser.protecting;
        }
        return null;
    }

    public static Boolean readToggles(File usersFile, String type) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);
        return switch (type) {
            case "toggleableTracking" -> discordUser.toggleableTracking;
            case "toggleableProtecting" -> discordUser.toggleableProtecting;
            case "toggleableEditing" -> discordUser.editMessage;
            default -> null;
        };
    }
    public static String getEditableMessage(File usersFile, String type) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);
        switch (type) {
            case "track":
            return discordUser.editableMessage.get(0);
            case "protect":
            return discordUser.editableMessage.get(1);
        }
        return null;
    }
    public static String getId (File usersFile) throws IOException {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);

        return discordUser.discordID;
    }
    public static void deleteData(User user, File usersFile, String type, String player) throws IOException {
        Gson g = new Gson();
        DiscordUser discordUser = g.fromJson(new Gson().fromJson(new String(Files.readAllBytes(Paths.get(usersFile.getPath())), StandardCharsets.UTF_8), JsonObject.class), DiscordUser.class);

        switch (type) {
            case "track":
                FileWriter trackingWriter = new FileWriter(usersFile);
                discordUser.tracking.remove(player);
                trackingWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                trackingWriter.close();
                break;
            case "protect":
                FileWriter protectWriter = new FileWriter(usersFile);
                discordUser.protecting.remove(player);
                protectWriter.write(g.toJson(new DiscordUser(discordUser.username, user.getId(), discordUser.tracking, discordUser.protecting, discordUser.editableMessage, discordUser.toggleableTracking, discordUser.toggleableProtecting, discordUser.editMessage)));
                protectWriter.close();
                break;
        }
    }
}
