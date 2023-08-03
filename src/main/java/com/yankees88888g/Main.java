package com.yankees88888g;

import com.google.gson.Gson;
import com.yankees88888g.APIObjects.nations.Nation;
import com.yankees88888g.APIObjects.residents.Resident;
import com.yankees88888g.APIObjects.towns.Town;
import com.yankees88888g.Cache.Cache;
import com.yankees88888g.discordUsers.Link;
import com.yankees88888g.discordUsers.ManageData;
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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

        Cache.createCache();
        jda.awaitReady();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        int delayInSeconds = 10; //adjustable

        ScheduledFuture<?> trackingFuture = scheduler.scheduleAtFixedRate(() -> TrackingPlayers.trackPlayers(jda), 0, delayInSeconds, TimeUnit.SECONDS);
        ScheduledFuture<?> protectingFuture = scheduler.scheduleAtFixedRate(() -> ProtectingPlayers.protectPlayers(jda), 0, delayInSeconds, TimeUnit.SECONDS);
        ScheduledFuture<?> cacheFuture = scheduler.scheduleAtFixedRate(() -> {
            try {
                Cache.updateCache();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, delayInSeconds, TimeUnit.SECONDS);

        //getAllData();
    }

    private void editData(SlashCommandInteractionEvent event, String type) {
        editData(event, type, null);
    }

    private void editData(@NotNull SlashCommandInteractionEvent event, String type, List<String> ids) {
        User user = event.getUser();
        File file = Link.getFile(user);

        try {
            OptionMapping option = event.getOption("username");
            String player = option == null ? null : option.getAsString();

            ManageData.editData(user, file, type, player, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replyWithList(SlashCommandInteractionEvent event, String type) {
        try {
            event.reply(replyingAList(ManageData.readData(Link.getFile(event.getUser()), type))).queue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // make sure we handle the right command
        switch (event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();

                // reply or acknowledge
                event.reply("Pong!").setEphemeral(true).flatMap(v ->
                    // then edit original
                    event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                ).queue(); // Queue both reply and edit
            }
            case "people" -> event.reply("Fight for the workers").queue();
            case "link" -> {
                try {
                    Link.link(event.getUser(), event.getOption("username").getAsString(),
                            BotActions.sendDMUpdates(event.getJDA(), event.getUser().getId(), "ok", null, null, ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "unlink" -> Link.unlink(event.getUser());
            case "track" -> editData(event, "track");
            case "protect" -> editData(event, "protect");
            case "tracklist" -> replyWithList(event, "track");
            case "protectlist" -> replyWithList(event, "protect");
            case "untrack" -> {
                try {
                    ManageData.deleteData(event.getUser(), Link.getFile(event.getUser()), "track", event.getOption("username").getAsString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "unprotect" -> {
                try {
                    ManageData.deleteData(event.getUser(), Link.getFile(event.getUser()), "protect", event.getOption("username").getAsString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "toggletracking" -> editData(event, "toggleTracking", null);
            case "toggleprotecting" -> editData(event, "toggleProtecting", null);
            case "togglemessageediting" -> {
                User user = event.getUser();
                editData(event, "toggleMessageEditing", null);

                try {
                    List<String> updates = BotActions.sendDMUpdates(event.getJDA(), user.getId(), "ok", null, false, "null");
                    editData(event, "toggleEditableMessage", updates);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NotNull
    private static String replyingAList(List<String> list) {
        if (list == null) {
            return "null";
        } else {
            StringBuilder string = new StringBuilder();
            int len = list.size() - 1;

            for(int i = 0; i <= len; i++){
                string.append(list.get(i)).append("\n");
            }

            return string.toString();
        }
    }

    private static void getAllData() throws IOException {
        HashMap<String, Resident> residentHashMap = new HashMap<>();
        HashMap<String, Town> townHashMap = new HashMap<>();
        HashMap<String, Nation> nationHashMap = new HashMap<>();

        List<String> residents = ReadAPI.readAPI("residents", null);
        residents.parallelStream().forEach(res -> {
            try {
                Resident resident = ReadAPI.readAPI("res", res);
                residentHashMap.put(resident.name, resident);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<String> towns = ReadAPI.readAPI("towns", null);
        towns.parallelStream().forEach(t -> {
            try {
                Town town = ReadAPI.readAPI("town", t);
                synchronized (townHashMap) {
                    townHashMap.put(town.name, town);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<String> nations = ReadAPI.readAPI("nations", null);
        nations.parallelStream().forEach(n -> {
            try {
                Nation nation = ReadAPI.readAPI("nation", n);
                synchronized (nationHashMap) {
                    nationHashMap.put(nation.name, nation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Gson gson = new Gson();

        // Convert the list of objects to a JSON string
        String jsonStringResidents = gson.toJson(residentHashMap);
        String jsonStringTowns = gson.toJson(townHashMap);
        String jsonStringNations = gson.toJson(nationHashMap);

        writeToFile("outputResidents.json", jsonStringResidents);
        writeToFile("outputTowns.json", jsonStringTowns);
        writeToFile("outputNations.json", jsonStringNations);
    }

    private static void writeToFile(String filePath, String data) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(data);
        }
    }
}