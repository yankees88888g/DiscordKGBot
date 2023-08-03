package com.yankees88888g;

import com.google.gson.Gson;
import com.yankees88888g.APIObjects.nations.Nation;
import com.yankees88888g.APIObjects.residents.Resident;
import com.yankees88888g.APIObjects.towns.Town;
import com.yankees88888g.discordUsers.Link;
import com.yankees88888g.discordUsers.ManageData;
import com.yankees88888g.readAPI.GetPlayersData;
import com.yankees88888g.readAPI.ProtectingPlayers;
import com.yankees88888g.readAPI.ReadAPI;
import com.yankees88888g.readAPI.TrackingPlayers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("bot.properties"));

        JDA jda = JDABuilder.createLight(properties.getProperty("token"), Collections.emptyList())
                .addEventListeners(new Main())
                .setActivity(Activity.playing("Supporting the Revolution"))
                .setStatus(OnlineStatus.ONLINE)
                .build();
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                //Commands.slash("res", "Calculate res"),
                Commands.slash("link", "Link with Minecraft")
                        .addOption(OptionType.STRING, "username", "username"),
                Commands.slash("unlink", "Unlink with Minecraft"),
                Commands.slash("track", "track a player")
                        .addOption(OptionType.STRING, "username", "username"),
                Commands.slash("protect", "protect a player")
                        .addOption(OptionType.STRING, "username", "username"),
                Commands.slash("untrack", "stop tracking a player")
                        .addOption(OptionType.STRING, "username", "username"),
                Commands.slash("unprotect", "stop protecting a player")
                        .addOption(OptionType.STRING, "username", "username"),
                Commands.slash("tracklist", "list tracking"),
                Commands.slash("protectlist", "list protecting"),
                Commands.slash("toggletracking", "toggle tracking"),
                Commands.slash("toggleprotecting", "toggle protecting"),
                Commands.slash("togglemessageediting", "toggle message editing")
                ).queue();
        jda.awaitReady();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);


        int delayInSeconds = 30; //adjustable

        ScheduledFuture<?> trackingFuture = scheduler.scheduleAtFixedRate(() -> TrackingPlayers.trackPlayers(jda), 0, delayInSeconds, TimeUnit.SECONDS);
        ScheduledFuture<?> protectingFuture = scheduler.scheduleAtFixedRate(() -> ProtectingPlayers.protectPlayers(jda), 0, delayInSeconds, TimeUnit.SECONDS);
        //getAllData();
    }




    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // make sure we handle the right command
        switch (event.getName()) {
            case "ping":
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                        .flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                        ).queue(); // Queue both reply and edit
                break;
            case "people":
                event.reply("Fight for the workers").queue();
                break;
            case "link":
                try {
                    Link.link(event.getUser(), event.getOption("username").getAsString(),
                            BotActions.sendDMUpdates(event.getJDA(), event.getUser().getId(), "ok", null, null, ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //event.reply("hi").queue();
                break;
            case "unlink":
                Link.unlink(event.getUser());
                break;
            case "track":
                try {
                    ManageData.editData(event.getUser(), Link.getFile(event.getUser()), "track", event.getOption("username").getAsString(), null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "tracklist":
                try {
                    event.reply(replyingAList(ManageData.readData(Link.getFile(event.getUser()), "track"))).queue();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "protectlist":
                try {
                    event.reply(replyingAList(ManageData.readData(Link.getFile(event.getUser()), "protect"))).queue();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "protect":
                try {
                     ManageData.editData(event.getUser(), Link.getFile(event.getUser()), "protect", event.getOption("username").getAsString(), null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "untrack":
                try {
                    ManageData.deleteData(event.getUser(), Link.getFile(event.getUser()), "track", event.getOption("username").getAsString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "unprotect":
                try {
                    ManageData.deleteData(event.getUser(), Link.getFile(event.getUser()), "protect", event.getOption("username").getAsString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "toggletracking":
                try {
                    ManageData.editData(event.getUser(), Link.getFile(event.getUser()), "toggleTracking", null, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "toggleprotecting":
                try {
                    ManageData.editData(event.getUser(), Link.getFile(event.getUser()), "toggleProtecting", null, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "togglemessageediting" :
                User user = event.getUser();
                File usersFile = Link.getFile(user);
                try {
                    ManageData.editData(user, usersFile, "toggleMessageEditing", null, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    ManageData.editData(user, usersFile, "toggleEditableMessage", null,
                            BotActions.sendDMUpdates(event.getJDA(), user.getId(), "ok", null, false,"null"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
    private static String replyingAList(List<String> list){
        if(list == null){
            return "null";
        } else {
            StringBuilder string = new StringBuilder();
            for(int i = 0; i <= list.size()-1; i++){
                string.append(list.get(i) + "\n");
            }
            System.out.println(string);
            return string.toString();
        }
    }



    private static void getAllData() throws IOException {

        HashMap<String, Resident> residentHashMap = new HashMap<>();
        HashMap<String, Town> townHashMap = new HashMap<>();
        HashMap<String, Nation> nationHashMap = new HashMap<>();


        List<String> residents = (List<String>) ReadAPI.readAPI("residents", null);
        for (String s : residents) {
            try {
                Resident resident = (Resident) ReadAPI.readAPI("res", s);
                residentHashMap.put(resident.name, resident);
                Thread.sleep(125);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(residentHashMap.get("yankees88888g").stats.balance);
        List<String> towns = (List<String>) ReadAPI.readAPI("towns", null);
        for (String s : towns) {
            try {
                Town town = (Town) ReadAPI.readAPI("town", s);
                townHashMap.put(town.name, town);
                Thread.sleep(125);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> nations = (List<String>) ReadAPI.readAPI("nations", null);
        for (String s : nations) {
            try {
                Nation nation = (Nation) ReadAPI.readAPI("nation", s);
                nationHashMap.put(nation.name, nation);
                Thread.sleep(125);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Gson gson = new Gson();

        // Convert the list of objects to a JSON string
        String jsonStringResidents = gson.toJson(residentHashMap);
        String jsonStringTowns = gson.toJson(townHashMap);
        String jsonStringNations = gson.toJson(nationHashMap);

        try (FileWriter fileWriter = new FileWriter("outputResidents.json")) {
            fileWriter.write(jsonStringResidents);
        }
        try (FileWriter fileWriter = new FileWriter("outputTowns.json")) {
            fileWriter.write(jsonStringTowns);
        }
        try (FileWriter fileWriter = new FileWriter("outputNations.json")) {
            fileWriter.write(jsonStringNations);
        }
    }
}