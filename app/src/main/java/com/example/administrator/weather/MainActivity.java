package com.example.administrator.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

    // set menu resource
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.weather,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        Log.d("id", Integer.toString(id));
        Log.d("city id " , Integer.toString(R.id.change_city));
        switch (id)
        {
            case R.id.change_city:
                showOptionDialog();
                break;
        }
        return false;
    }

    private void showOptionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change City");
        final Spinner spinner = new Spinner(this);
        final String[] cityArray = getResources().getStringArray(R.array.city);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cityArray);
        spinner.setAdapter(arrayAdapter); // 設定資料來源
        int position = arrayAdapter.getPosition(new CityPreference(this).getCity()); // 抓取預設值
        spinner.setSelection(position); // 選取預設值
        builder.setView(spinner);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("[selected city]",spinner.getSelectedItem().toString());
                changeCity(spinner.getSelectedItem().toString());
            }
        });
        builder.show();  // 顯示 dialog
    }

    private void changeCity(String city)
    {
        WeatherFragment weatherFragment = (WeatherFragment)getFragmentManager().findFragmentById(R.id.container);
        weatherFragment.changeCity(city);
        new CityPreference(this).setCity(city);
    }
}
