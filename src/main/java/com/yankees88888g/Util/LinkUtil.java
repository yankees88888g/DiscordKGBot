package com.yankees88888g.Util;

import io.github.emcw.entities.Location;

import java.net.MalformedURLException;
import java.net.URL;

public class LinkUtil {
    public static URL generateLink(int x, int z) throws MalformedURLException {
        return new URL("https://earthmc.net/map/aurora/?worldname=earth&mapname=flat&zoom=5&x=" + x + "&y=" + 64 + "&z=" + z + "#");
    }

    public static URL generateLink(Location location) throws MalformedURLException {
        return new URL("https://earthmc.net/map/aurora/?worldname=earth&mapname=flat&zoom=5&x=" + location.getX() + "&y=" + location.getY() + "&z=" + location.getZ() + "#");
    }
}
