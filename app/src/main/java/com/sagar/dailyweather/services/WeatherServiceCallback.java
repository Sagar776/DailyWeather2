package com.sagar.dailyweather.services;

import com.sagar.dailyweather.data.Channel;

/**
 * Created by SAGAR on 22-03-2018.
 */

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);

}
