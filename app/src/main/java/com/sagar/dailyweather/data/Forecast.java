package com.sagar.dailyweather.data;

import com.sagar.dailyweather.services.WeatherServiceCallback;

import org.json.JSONObject;

/**
 * Created by SAGAR on 23-03-2018.
 */





public class Forecast implements JSONPopulator {

    private int fcode;
    private int ftemp,high,low;
    private  String ftext;

    public int getFcode() {
        return fcode;
    }

    public int getFtemp() {
        return ftemp;
    }

    public String getFtext() {
        return ftext;
    }

    public void populate(JSONObject data){

        fcode=data.optInt("code");
        high=data.optInt("high");
        low=data.optInt("low");
        ftemp=(high+low)/2;
    }
}
