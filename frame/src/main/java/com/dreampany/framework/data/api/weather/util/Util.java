package com.dreampany.framework.data.api.weather.util;

import com.dreampany.framework.data.api.weather.enums.UnitType;
import com.dreampany.framework.data.api.weather.model.Unit;

/**
 * Created by air on 29/12/17.
 */

public class Util {

    public static Unit createWeatherUnit(UnitType unitType) {
        Unit unit = new Unit();

        if (unitType == null)
            return unit;

        if (isMetric(unitType)) {
            unit.speedUnit = "m/s";
            unit.tempUnit = "°C";
        } else {
            unit.speedUnit = "mph";
            unit.tempUnit = "°F";
        }

        unit.pressureUnit = "hPa";

        return unit;
    }

    public static boolean isMetric(UnitType unit) {
        return unit.equals(UnitType.METRIC);
    }

    public static float toCelcius(float temp) {
        return (int) Math.round((temp - 32) / 1.8);
    }

    public static int toFar(float temp) {
        return (int) ((temp - 273.15) * 1.8 + 32);
    }

    public static int toKMH(float val) {
        return (int) Math.round(val * 0.2778);
    }
}
