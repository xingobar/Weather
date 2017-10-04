package com.example.administrator.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null)
        {
            WeatherFragment weatherFragment = new WeatherFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container,weatherFragment)
                    .commit();
        }

    }
}
