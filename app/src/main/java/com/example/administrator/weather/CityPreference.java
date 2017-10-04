package com.example.administrator.weather;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/10/4.
 */

public class CityPreference {

    private SharedPreferences sharedPreferences;

    public CityPreference(Activity activity) {
        sharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity()
    {
        return sharedPreferences.getString("city","Taoyuan");
    }

    public void setCity(String city)
    {
        sharedPreferences.edit().putString("city",city).commit();
    }

}
