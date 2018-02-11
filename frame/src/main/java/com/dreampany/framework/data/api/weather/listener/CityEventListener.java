package com.dreampany.framework.data.api.weather.listener;

import com.dreampany.framework.data.api.weather.model.City;

import java.util.List;

/**
 * Created by air on 29/12/17.
 */

public interface CityEventListener extends ClientListener {
    void onCityListRetrieved(List<City> cities);

}
