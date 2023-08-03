package com.yankees88888g.discordUsers.objects;

import java.util.List;

public class DiscordUser {

    public String username;
    public String discordID;
    public List<String> editableMessage;
    public List<String> tracking;
    public List<String> protecting;
    public boolean toggleableTracking;
    public boolean toggleableProtecting;
    public boolean editMessage;

    public DiscordUser(String username, String discordID, List<String> tracking, List<String> protecting, List<String> editableMessage, boolean toggleableTracking, boolean toggleableProtecting, boolean editMessage) {
        this.username = username;
        this.discordID = discordID;
        this.tracking = tracking;
        this.protecting = protecting;
        this.toggleableTracking = toggleableTracking;
        this.toggleableProtecting = toggleableProtecting;
        this.editableMessage = editableMessage;
        this.editMessage = editMessage;
    }
}