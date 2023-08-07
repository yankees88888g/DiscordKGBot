package com.yankees88888g;

import com.yankees88888g.APIObjects.residents.Resident;
import com.yankees88888g.APIObjects.towns.Town;
import com.yankees88888g.readAPI.ReadAPI;
import io.github.emcw.map.Towns;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MayorActivity {
    public static void mayorActivity(JDA jda, String channelId) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> towns = ReadAPI.readAPI("towns", null);

        List<Resident> townMayorData = new ArrayList<>();
        for (String s : towns) {
            System.out.println(s);
            Town town = ReadAPI.readAPI("town", s);
            Resident mayorData = ReadAPI.readAPI("res", town.mayor);
            townMayorData.add(mayorData);
        }
        for (Resident townMayorDatum : townMayorData) {
            BotActions.sendMessages(channelId, jda, "\n<t:" + townMayorDatum.timestamps.lastOnline/1000 + ":F> Mayor: " + townMayorDatum.name + " Town: " + townMayorDatum.town);
        }
        System.out.println(stringBuilder);
    }
}
