package com.dreampany.framework.data.api.weather.owm;

import com.dreampany.framework.data.api.weather.WeatherConfig;
import com.dreampany.framework.data.api.weather.exception.ApiKeyRequiredException;
import com.dreampany.framework.data.api.weather.exception.WeatherException;
import com.dreampany.framework.data.api.weather.model.City;
import com.dreampany.framework.data.api.weather.model.Unit;
import com.dreampany.framework.data.api.weather.model.Weather;
import com.dreampany.framework.data.api.weather.provider.CodeProvider;
import com.dreampany.framework.data.api.weather.provider.WeatherProvider;
import com.dreampany.framework.data.api.weather.util.Util;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.JsonUtil;
import com.dreampany.framework.data.util.TimeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.List;

/**
 * Created by air on 29/12/17.
 */

public class OWMWeatherProvider implements WeatherProvider {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?mode=json&q=";
    private static final String BASE_URL_ID = "http://api.openweathermap.org/data/2.5/weather?mode=json&id=";
    private static final String GEO_BASE_URL_ID = "http://api.openweathermap.org/data/2.5/weather?mode=json";
    private static final String IMAGE_ICON = "http://openweathermap.org/img/w/%s.png";


    private static final String COORDINATE = "coord";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";

    private static final String SYSTEM = "sys";
    private static final String COUNTRY = "country";
    private static final String SUNRISE = "sunrise";
    private static final String SUNSET = "sunset";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String WEATHER = "weather";
    private static final String DESCRIPTION = "description";
    private static final String MAIN = "main";
    private static final String ICON = "icon";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";
    private static final String PRESSURE_GROUND_LEVEL = "grnd_level";
    private static final String PRESSURE_SEA_LEVEL = "sea_level";
    private static final String TEMPERATURE = "temp";
    private static final String TEMPERATURE_MAX = "temp_max";
    private static final String TEMPERATURE_MIN = "temp_min";
    private static final String WIND = "wind";
    private static final String SPEED = "speed";
    private static final String DEGREE = "deg";
    private static final String GUST = "gust";
    private static final String CLOUDS = "clouds";
    private static final String ALL = "all";
    private static final String RAIN = "rain";
    private static final String HOUR_1 = "1h";
    private static final String HOUR_3 = "3h";
    private static final String SNOW = "snow";
    private static final String DATE_TIME = "dt";


    private WeatherConfig config;
    private CodeProvider codeProvider;
    private Unit unit;
    private JsonParser parser;

    public OWMWeatherProvider() {
        parser = new JsonParser();
    }

    @Override
    public void setConfig(WeatherConfig config) {
        this.config = config;
        unit = Util.createWeatherUnit(config.unitType);
    }

    @Override
    public void setCodeProvider(CodeProvider codeProvider) {
        this.codeProvider = codeProvider;
    }

    @Override
    public String getWeatherUrl(String id) throws ApiKeyRequiredException {
        return BASE_URL_ID + id + "&units=" + (Util.isMetric(config.unitType) ? "metric" : "imperial") + "&lang=" + config.language + createAppId();
    }

    @Override
    public String getWeatherUrl(String city, String country) throws ApiKeyRequiredException {
        return BASE_URL + city + "," + country + "&units=" + (Util.isMetric(config.unitType) ? "metric" : "imperial") + "&lang=" + config.language + createAppId();
    }

    @Override
    public String getWeatherUrl(double latitude, double longitude) throws ApiKeyRequiredException {
        return GEO_BASE_URL_ID + "&lat=" + latitude + "&lon=" + longitude + "&units=" + (Util.isMetric(config.unitType) ? "metric" : "imperial") + "&lang=" + config.language + createAppId();
    }

    @Override
    public Weather getWeather(String data) throws WeatherException {
        try {
            JsonObject json = parser.parse(data).getAsJsonObject();
            if (json == null) {
                return null;
            }
            return getWeather(json);
        } catch (NullPointerException | JsonParseException ignored) {
            throw new WeatherException(ignored);
        }
    }

    @Override
    public List<City> getCities(String data) throws WeatherException {
        return null;
    }

    private Weather getWeather(JsonObject json) {
        Weather weather = new Weather();

        JsonObject coordinate = JsonUtil.getObject(json, COORDINATE, null);
        JsonObject system = JsonUtil.getObject(json, SYSTEM, null);
        JsonArray weatherArray = JsonUtil.getArray(json, WEATHER, null);
        JsonObject weatherJson = weatherArray.get(0).getAsJsonObject();
        JsonObject main = JsonUtil.getObject(json, MAIN, null);
        JsonObject wind = JsonUtil.getObject(json, WIND, null);
        JsonObject cloud = JsonUtil.getObject(json, CLOUDS, null);
        JsonObject rain = JsonUtil.getObject(json, RAIN, null);
        JsonObject snow = JsonUtil.getObject(json, SNOW, null);

        String id = JsonUtil.getString(json, ID, null);
        String city = JsonUtil.getString(json, NAME, null);
        long time = JsonUtil.getLong(json, DATE_TIME, 0L); // UTC seconds

        double latitude = JsonUtil.getDouble(coordinate, LATITUDE, 0f);
        double longitude = JsonUtil.getDouble(coordinate, LONGITUDE, 0f);

        String country = JsonUtil.getString(system, COUNTRY, null);
        int sunrise = JsonUtil.getInt(system, SUNRISE, 0);
        int sunset = JsonUtil.getInt(system, SUNSET, 0);

        weather.setId(id);
        weather.setCity(city);
        weather.setCountry(country);
        weather.setLatitude(latitude);
        weather.setLongitude(longitude);
        weather.setSunrise(sunrise);
        weather.setSunset(sunset);
        weather.setTime(TimeUtil.secondToMilli(time));

        if (weatherJson != null) {
            String code = JsonUtil.getString(weatherJson, ID, null);
            String condition = JsonUtil.getString(weatherJson, MAIN, null);
            String description = JsonUtil.getString(weatherJson, DESCRIPTION, null);
            String icon = JsonUtil.getString(weatherJson, ICON, null);
            weather.setCode(code);
            weather.setCondition(condition);
            weather.setDescription(description);
            weather.setIcon(String.format(IMAGE_ICON, icon));
        }

        if (main != null) {
            int humidity = JsonUtil.getInt(main, HUMIDITY, 0);
            float pressure = JsonUtil.getFloat(main, PRESSURE, 0f);
            float pressureGroundLevel = JsonUtil.getFloat(main, PRESSURE_GROUND_LEVEL, 0f);
            float pressureSeaLevel = JsonUtil.getFloat(main, PRESSURE_SEA_LEVEL, 0f);

            float temperature = JsonUtil.getFloat(main, TEMPERATURE, 0f);
            float temperatureMax = JsonUtil.getFloat(main, TEMPERATURE_MAX, 0f);
            float temperatureMin = JsonUtil.getFloat(main, TEMPERATURE_MIN, 0f);

            weather.setHumidity(humidity);
            weather.setPressure(pressure);
            weather.setPressureGroundLevel(pressureGroundLevel);
            weather.setPressureSeaLevel(pressureSeaLevel);
            weather.setTemperature(temperature);
            weather.setMaxTemperature(temperatureMax);
            weather.setMinTemperature(temperatureMin);
        }

        if (wind != null) {
            float speed = JsonUtil.getFloat(wind, SPEED, 0f);
            float degree = JsonUtil.getFloat(wind, DEGREE, 0f);
            float gust = JsonUtil.getFloat(wind, GUST, 0f);

            weather.setSpeed(speed);
            weather.setDegree(degree);
            weather.setGust(gust);
        }

        if (cloud != null) {
            int percentage = JsonUtil.getInt(cloud, ALL, 0);
            weather.setCloudPercentage(percentage);
        }

        if (rain != null) {
            //float rain1h = JsonUtil.getFloat(rain, HOUR_1, 0f);
            float rain3h = JsonUtil.getFloat(rain, HOUR_3, 0f);
            //float rain3h = JsonUtil.getFloat(rain, HOUR_3, 0f);

/*            if (rain1h > 0.0f) {
                weather.rain[0].setAmount(rain1h);
                weather.rain[0].setTime(HOUR_1);
            }*/

            if (rain3h > 0.0f) {
                weather.setRainAmount(rain3h);
                weather.setRainTime(HOUR_3);
            }
        }

        if (snow != null) {
            float snow3h = JsonUtil.getFloat(snow, HOUR_3, 0f);
            weather.setSnowAmount(snow3h);
            weather.setSnowTime(HOUR_3);
        }
        weather.setUnit(unit);
        return weather;
    }

    private String createAppId() {
        if (DataUtil.isEmpty(config.apiKey)) {
            return "";
        }
        return "&appid=" + config.apiKey;
    }
}
