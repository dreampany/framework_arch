package com.dreampany.framework.data.api.weather;

import com.dreampany.framework.data.api.weather.enums.UnitType;

/**
 * Created by air on 29/12/17.
 */

public class WeatherConfig {

    public int maxResult = 10;
    public int numDays = 3;

    public String language = "en";
    public String apiKey = "";
    public UnitType unitType = UnitType.METRIC;
}
