package com.dreampany.framework.data.api.weather.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.dreampany.framework.data.model.Base;
import com.google.common.base.Objects;

/**
 * Created by air on 29/12/17.
 */

@Entity(indices = {
        @Index(value = {"id"}, unique = true)}/*,
        primaryKeys = {"id", "provider"}*/
)
public class Weather extends Base {

    @PrimaryKey
    @NonNull
    private String id;
    private String city;
    private String country;
    private String region;
    private String stationId;
    private String station;
    private double latitude;
    private double longitude;
    private long sunrise;
    private long sunset;
    private long population;
    private String moonAge;
    private String percentIlluminated;
    private String moonPhaseDescription;
    private String hemisphere;

    private String code;
    private String condition;
    private String description;
    private String icon;
    private float pressure;
    private int humidity;
    private float visibility;
    private int pressureTrend;
    private float feelsLike;
    private float UV;
    private float dewPoint;
    private String heatIndex;
    private String solarRadiation;
    private float pressureSeaLevel;
    private float pressureGroundLevel;

    private float temperature;
    private float minTemperature;
    private float maxTemperature;

    private float speed;
    private float degree;
    private float chill;
    private float gust;

    private String rainTime;
    private float rainAmount;
    private float rainChance;

    private String snowTime;
    private float snowAmount;

    private int cloudPercentage;

    @Embedded
    private Unit unit;

    public Weather() {

    }

    @Ignore
    private Weather(Parcel in) {
        super(in);
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof Weather) {
            Weather item = (Weather) inObject;
            return id.equals(item.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public void setMoonAge(String moonAge) {
        this.moonAge = moonAge;
    }

    public void setPercentIlluminated(String percentIlluminated) {
        this.percentIlluminated = percentIlluminated;
    }

    public void setMoonPhaseDescription(String moonPhaseDescription) {
        this.moonPhaseDescription = moonPhaseDescription;
    }

    public void setHemisphere(String hemisphere) {
        this.hemisphere = hemisphere;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public void setPressureTrend(int pressureTrend) {
        this.pressureTrend = pressureTrend;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setUV(float UV) {
        this.UV = UV;
    }

    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public void setHeatIndex(String heatIndex) {
        this.heatIndex = heatIndex;
    }

    public void setSolarRadiation(String solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public void setPressureSeaLevel(float pressureSeaLevel) {
        this.pressureSeaLevel = pressureSeaLevel;
    }

    public void setPressureGroundLevel(float pressureGroundLevel) {
        this.pressureGroundLevel = pressureGroundLevel;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public void setChill(float chill) {
        this.chill = chill;
    }

    public void setGust(float gust) {
        this.gust = gust;
    }

    public void setRainTime(String rainTime) {
        this.rainTime = rainTime;
    }

    public void setRainAmount(float rainAmount) {
        this.rainAmount = rainAmount;
    }

    public void setRainChance(float rainChance) {
        this.rainChance = rainChance;
    }

    public void setSnowTime(String snowTime) {
        this.snowTime = snowTime;
    }

    public void setSnowAmount(float snowAmount) {
        this.snowAmount = snowAmount;
    }

    public void setCloudPercentage(int cloudPercentage) {
        this.cloudPercentage = cloudPercentage;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStation() {
        return station;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public long getPopulation() {
        return population;
    }

    public String getMoonAge() {
        return moonAge;
    }

    public String getPercentIlluminated() {
        return percentIlluminated;
    }

    public String getMoonPhaseDescription() {
        return moonPhaseDescription;
    }

    public String getHemisphere() {
        return hemisphere;
    }

    public String getCode() {
        return code;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public float getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public int getPressureTrend() {
        return pressureTrend;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public float getUV() {
        return UV;
    }

    public float getDewPoint() {
        return dewPoint;
    }

    public String getHeatIndex() {
        return heatIndex;
    }

    public String getSolarRadiation() {
        return solarRadiation;
    }

    public float getPressureSeaLevel() {
        return pressureSeaLevel;
    }

    public float getPressureGroundLevel() {
        return pressureGroundLevel;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDegree() {
        return degree;
    }

    public float getChill() {
        return chill;
    }

    public float getGust() {
        return gust;
    }

    public String getRainTime() {
        return rainTime;
    }

    public float getRainAmount() {
        return rainAmount;
    }

    public float getRainChance() {
        return rainChance;
    }

    public String getSnowTime() {
        return snowTime;
    }

    public float getSnowAmount() {
        return snowAmount;
    }

    public int getCloudPercentage() {
        return cloudPercentage;
    }

    public Unit getUnit() {
        return unit;
    }
}
