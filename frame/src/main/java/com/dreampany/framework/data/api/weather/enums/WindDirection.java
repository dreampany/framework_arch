package com.dreampany.framework.data.api.weather.enums;

/**
 * Created by air on 29/12/17.
 */

public enum WindDirection {

    NORTH,
    NORTH_NORTH_EAST,
    NORTH_EAST,
    EAST_NORTH_EAST,
    EAST,
    EAST_SOUTH_EAST,
    SOUTH_EAST,
    SOUTH_SOUTH_EAST,
    SOUTH,
    SOUTH_SOUTH_WEST,
    SOUTH_WEST,
    WEST_SOUTH_WEST,
    WEST,
    WEST_NORTH_WEST,
    NORTH_WEST,
    NORTH_NORTH_WEST;

    public static WindDirection getDir(int deg) {
        int degPositive = deg;
        if (deg < 0) {
            degPositive += (-deg / 360 + 1) * 360;
        }
        int degNormalized = degPositive % 360;
        int degRotated = degNormalized + (360 / 16 / 2);
        int sector = degRotated / (360 / 16);
        switch (sector) {
            case 0:
                return WindDirection.NORTH;
            case 1:
                return WindDirection.NORTH_NORTH_EAST;
            case 2:
                return WindDirection.NORTH_EAST;
            case 3:
                return WindDirection.EAST_NORTH_EAST;
            case 4:
                return WindDirection.EAST;
            case 5:
                return WindDirection.EAST_SOUTH_EAST;
            case 6:
                return WindDirection.SOUTH_EAST;
            case 7:
                return WindDirection.SOUTH_SOUTH_EAST;
            case 8:
                return WindDirection.SOUTH;
            case 9:
                return WindDirection.SOUTH_SOUTH_WEST;
            case 10:
                return WindDirection.SOUTH_WEST;
            case 11:
                return WindDirection.WEST_SOUTH_WEST;
            case 12:
                return WindDirection.WEST;
            case 13:
                return WindDirection.WEST_NORTH_WEST;
            case 14:
                return WindDirection.NORTH_WEST;
            case 15:
                return WindDirection.NORTH_NORTH_WEST;
            case 16:
                return WindDirection.NORTH;
        }
        return WindDirection.NORTH;
    }
}
