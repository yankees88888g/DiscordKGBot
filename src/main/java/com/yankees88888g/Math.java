package com.yankees88888g;

public class Math {
    //Credit to Owen3H for this function
    public static int findShortestDistance(int x1, int z1, int x2, int z2) {
        return java.lang.Math.toIntExact(java.lang.Math.round(java.lang.Math.sqrt(java.lang.Math.pow(x2 - x1, 2) + java.lang.Math.pow(z2 - z1, 2))));
    }
}
