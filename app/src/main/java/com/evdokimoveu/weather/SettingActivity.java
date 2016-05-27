package com.evdokimoveu.weather;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evdokimoveu.weather.Model.City;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView listCity;
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        cities = new ArrayList<>();
        cities.add(new City("700569","Николаев"));
        cities.add(new City("695365","Ровно"));
        cities.add(new City("692194","Сумы"));

        listCity = (ListView) findViewById(R.id.listViewCity);
        CityListAdapter adapter = new CityListAdapter(this, cities);
        listCity.setAdapter(adapter);
        listCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("cityId", cities.get(position).getId());
                returnIntent.putExtra("cityName", cities.get(position).getName());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
