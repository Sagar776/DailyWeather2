package com.sagar.dailyweather.data;

import org.json.JSONObject;

/**
 * Created by SAGAR on 22-03-2018.
 */

public class Item implements JSONPopulator {
    private Condition condition;
    private Forecast forecast;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition=new Condition();
        condition.populate(data.optJSONObject("condition"));

        forecast=new Forecast();
        condition.populate(data.optJSONObject("forecast/0"));
    }
}
