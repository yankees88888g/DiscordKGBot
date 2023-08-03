package com.yankees88888g;

import static java.lang.Math.*;

public class Math {
    //Credit to Owen3H for this function
    public static int findShortestDistance(int x1, int z1, int x2, int z2) {
        return toIntExact(round(sqrt(pow(x2 - x1, 2) + pow(z2 - z1, 2))));
    }
}