package com.example.administrator.weather;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/4.
 */

public class WeatherFragment extends Fragment {

    TextView cityField;
    TextView updatedField;
    TextView detailedField;
    TextView weatherIcon;
    TextView temperatureField;
    Handler handler;
    Typeface weatherFont;

    public WeatherFragment() {
        this.handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        cityField = (TextView)view.findViewById(R.id.city);
        updatedField = (TextView)view.findViewById(R.id.updated);
        detailedField = (TextView)view.findViewById(R.id.detail);
        weatherIcon = (TextView)view.findViewById(R.id.weather_icon);
        temperatureField = (TextView)view.findViewById(R.id.degree);

        weatherIcon.setTypeface(weatherFont);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/weather.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity()); // 呼叫 weather api 去抓取天氣的資訊
    }

    private void updateWeatherData(final String city)
    {
        new Thread()
        {
            @Override
            public void run() {
                final JSONObject json = RemoteFetchWeatherInfo.getWeatherInfoJSON(getActivity(),city);
                if(json == null)
                {
                    // main thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),R.string.place_not_found, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    // main thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json)
    {
        try{
            cityField.setText(json.getString("name").toUpperCase(Locale.TAIWAN) +
                             "," + json.getJSONObject("sys").getString("country"));

            JSONObject detailed = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            detailedField.setText(
                    detailed.getString("description").toUpperCase(Locale.TAIWAN)
                    + "\n" + "Humidity : " + main.getString("humidity") + "%"
                    + "\n" + "Pressure : " + main.getString("pressure") + "hPa"
            );

            temperatureField.setText(String.format("%.2f",main.getDouble("temp")) + " ℃");
            DateFormat df = DateFormat.getDateInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000)); // multiply by 1000 , since java is  expecting millseconds
            updatedField.setText("Last updated  " + updatedOn);
            setWeatherICON(detailed.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000, // multiply by 1000 , since java is  expecting millseconds
                    json.getJSONObject("sys").getLong("sunset") * 1000); // multiply by 1000 , since java is  expecting millseconds
        }catch(Exception exception)
        {
            Log.e("Weather Error : " , exception.getMessage());
        }
    }

    private void setWeatherICON(int actualId,long sunrise,long sunset)
    {
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800)
        {
            long currentTime = new Date().getTime(); // get millseconds
            if(currentTime >= sunrise && currentTime < sunset)
            {
                icon = getActivity().getString(R.string.weather_sunny);
            }else{
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        }else{
            switch (id)
            {
                case 2 :
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city)
    {
        updateWeatherData(city);
    }
}
