package com.yankees88888g.Cache;

import io.github.emcw.entities.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerTime extends Player {
    public long time;
    public PlayerTime(@NotNull Player player, long time) {
        super(player);
        this.time = time;
    }

    public long getTimeMillisecounds() {
        return time;
    }
    public long getUnix() {return time/1000;}

    public void setTime(long time) {
        this.time = time;
    }
}
