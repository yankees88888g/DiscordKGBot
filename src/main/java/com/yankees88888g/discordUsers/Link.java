package com.yankees88888g.discordUsers;

import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Link {
    public static String link(User user, String username, List<String> messageID) throws IOException {
        System.out.println(username);
        File usersFile = getFile(user);
        usersFile.createNewFile();
        ManageData.createData(user, usersFile, username, messageID);
        return username;
    }

    public static void unlink(User user) {
        //getFile(user).renameTo(new File("discordUsers/archived" + user.getId() + ".json"));
        getFile(user).delete();
    }

    public static File getFile(User user){
        return new File("discordUsers/" + user.getId() + ".json");
    }
    private static boolean checkIfUsernameIsAlreadyBeingUsed(){
        return false;
    }
}
