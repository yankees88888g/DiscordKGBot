package com.yankees88888g;

import com.yankees88888g.APIObjects.residents.Resident;
import com.yankees88888g.APIObjects.towns.Town;
import com.yankees88888g.readAPI.ReadAPI;
import net.dv8tion.jda.api.JDA;

import java.io.IOException;
import java.util.*;

import static com.yankees88888g.Util.MathUtil.*;

public class MayorActivity {
    public static void mayorActivity(JDA jda, String channelId) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> towns = ReadAPI.readAPI("towns", null);
        System.out.println(towns.size());
        for (String s : towns) {
            Town town = ReadAPI.readAPI("town", s);
            Resident mayorData = ReadAPI.readAPI("res", town.mayor);
            if (mayorData.timestamps.lastOnline != null) {
                long unixTime = mayorData.timestamps.lastOnline / 1000;
                if (isUnixTimeOlderThan40Days(unixTime)) {
                    stringBuilder.append("\nExpected Fall time <t:").append(getHowLongTillFallEst(unixTime)).append(":F> Mayor: ").append(mayorData.name).append(" Town: **").append(mayorData.town).append("**; Mayor was last on at <t:").append(unixTime).append(":F>");
                    if (stringBuilder.length() > 1500) {
                        BotActions.sendMessages(channelId, jda, stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }
                }
            }
        }
        BotActions.sendMessages(channelId, jda, stringBuilder.toString());
        System.out.println(stringBuilder);
    }
}
