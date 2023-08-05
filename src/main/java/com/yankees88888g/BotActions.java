package com.yankees88888g;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.yankees88888g.discordUsers.ManageData.getEditableMessage;

public class BotActions {
    public static List<String> sendDMUpdates(JDA jda, String userId, String message, File file, Boolean editMessage, String type) throws IOException {
        System.out.println(message);
        if(file != null) {
            //boolean editMessage = ManageData.readToggles(file, "toggleableEditing");
            if (!editMessage) {
                jda.retrieveUserById(userId).queue(recipientUser -> {
                    System.out.println(recipientUser);
                    recipientUser.openPrivateChannel().queue((privateChannel) -> {
                        privateChannel.sendMessage(message).queue();
                    });
                });
            } else {
                try {
                    User user = jda.retrieveUserById(userId).complete();
                    PrivateChannel channel = user.openPrivateChannel().complete();

                    System.out.println(channel.getName());
                    channel.retrieveMessageById(getEditableMessage(file, type)).complete().editMessage(message).complete();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        } else {
            List<String> ids = new ArrayList<>();
            ids.add(jda.retrieveUserById(userId).complete().openPrivateChannel().complete().sendMessage(message).complete().getId());
            ids.add(jda.retrieveUserById(userId).complete().openPrivateChannel().complete().sendMessage(message).complete().getId());
            return ids;
        }
        return null;
    }
}