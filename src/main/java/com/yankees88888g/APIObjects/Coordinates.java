package com.yankees88888g.APIObjects;

public class Coordinates {
    public String name;
    public String nickname;
    public int x;
    public int y;
    public int z;
    public boolean underground;
    public long unixTime;

    public Coordinates(String name, int x, int y, int z, boolean underground, long unixTime) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.underground = underground;
        this.unixTime = unixTime;
    }
}
