package com.yankees88888g.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;

import java.awt.*;

public class EmbedUtil {
    public static MessageEmbed embed(PrivateChannel channel, String url, Color color, String title, String description, String shorten) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(color);
        embedBuilder.setTitle("Embedded Link");
        embedBuilder.setDescription("Check out this link:");
        embedBuilder.addField("Link", "[" + shorten + "](" + url + ")", false);
        return embedBuilder.build();
    }
}
