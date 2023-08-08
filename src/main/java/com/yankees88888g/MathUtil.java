package com.yankees88888g;

import static java.lang.Math.*;

public class MathUtil {
    //Credit to Owen3H for this function
    public static int findShortestDistance(int x1, int z1, int x2, int z2) {
        return toIntExact(round(sqrt(pow(x2 - x1, 2) + pow(z2 - z1, 2))));
    }

    public static boolean isUnixTimeOlderThan40Days(long targetUnixTime) {
        long fortyDaysAgoUnixTime = System.currentTimeMillis() / 1000L - (40L * 24L * 60L * 60L); // 40 days ago in Unix time
        return targetUnixTime < fortyDaysAgoUnixTime;
    }

    public static long getHowLongTillFallEst(long unixTime) {
        return System.currentTimeMillis() / 1000L + Math.abs(System.currentTimeMillis() / 1000L - (42L * 24L * 60L * 60L) - unixTime);
    }
}