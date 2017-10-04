package com.example.administrator.weather;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/10/4.
 */

public class RemoteFetchWeatherInfo {

    private static final String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";;

    public static JSONObject getWeatherInfoJSON(Context context,String city)
    {
        try{
            URL url = new URL(String.format(WEATHER_API,city));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key",context.getString(R.string.open_weather_maps_app_id)); // set http header
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder json = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null)
            {
                json.append(line).append("\n");
            }
            bufferedReader.close();
            JSONObject weatherJSON = new JSONObject(json.toString()); // convert string to json

            if(weatherJSON.getInt("cod") != 200) // check whether the request was successful or not
            {
                return null;
            }
            return weatherJSON;

        }catch(Exception exception)
        {
            return null;
        }
    }
}
