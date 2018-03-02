package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.api.weather.model.Weather;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);

    @Query("select count(*) from weather")
    int count();

    @Query("select * from weather")
    List<Weather> getAll();

    @Query("select * from weather order by time desc limit 1")
    Weather getLastWeather();

    @Query("select * from weather where id = :id and time >= :time order by time desc limit 1")
    Weather getWeather(String id, long time);

    @Query("select * from weather where city = :city and country = :country and time >= :time order by time desc limit 1")
    Weather getWeather(String city, String country, long time);

    @Query("select * from weather where latitude = :latitude and longitude = :longitude order by time desc limit 1")
    Weather getWeather(double latitude, double longitude);

    @Query("select * from weather where latitude = :latitude and longitude = :longitude and time >= :time order by time desc limit 1")
    Weather getWeather(double latitude, double longitude, long time);

    @Update
    void update(Weather weather);

    @Delete
    void delete(Weather weather);
}
