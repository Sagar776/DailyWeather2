package com.sagar.dailyweather.data;

import org.json.JSONObject;

/**
 * Created by SAGAR on 22-03-2018.
 */

public class Units implements JSONPopulator {

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature=data.optString("temperature");

    }
}
