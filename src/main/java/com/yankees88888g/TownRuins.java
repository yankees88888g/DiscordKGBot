package com.yankees88888g;

import io.github.emcw.core.EMCMap;
import io.github.emcw.entities.Town;
import io.github.emcw.map.Towns;
import net.dv8tion.jda.api.JDA;

import java.util.Map;
import java.util.regex.Pattern;

public class TownRuins {

    public static void getRuinedTowns(JDA jda, String townFlowChannelId, EMCMap map) {
        System.out.println(townFlowChannelId);
        Map<String, Town> townList = map.Towns.all();
        Pattern NPCRegex = Pattern.compile("^NPC[0-9]{1,5}$");

        townList.forEach((townName, town) -> {
            boolean ruined = NPCRegex.matcher(town.getMayor()).matches() || town.getResidents() == null || town.getResidents().isEmpty();
        });
    }
}