package com.yankees88888g;

import com.yankees88888g.discordUsers.ManageData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BotActions {
    public static List<String> sendDMUpdates(JDA jda, String userId, String message, File file, Boolean editMessage, String type) throws IOException {
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
                    System.out.println(jda.retrieveUserById(userId).complete().openPrivateChannel().complete().getName());
                    jda.retrieveUserById(userId).complete().openPrivateChannel().complete().retrieveMessageById(ManageData.getEditableMessage(file, type)).complete().editMessage(message).queue(
                            success -> System.out.println("Message edited successfully!"),
                            error -> System.out.println("Error editing message: " + error.getMessage())
                    );
                } catch (IOException e) {throw new RuntimeException(e);}
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
