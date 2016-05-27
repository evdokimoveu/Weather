package com.evdokimoveu.weather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int CODE_REQUEST = 1;
    private ImageButton setting;
    private String cityId;
    private String cityName;
    private String iconName;

    private ImageView icon;
    private TextView cityNameView;
    private TextView tempCurrent;
    private TextView tempMinMax;
    private TextView weatherDescription;

    private Map<String, String> description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo == null){
            Toast.makeText(this, "Ошибка подключения1", Toast.LENGTH_LONG).show();
        }

        description = new HashMap<>();
        String des[]= getResources().getStringArray(R.array.descr);
        for(int i = 0; i < des.length; i++){
            String s[] = des[i].split("#");
            description.put(s[0], s[1]);
        }

        setting = (ImageButton) findViewById(R.id.settingImgButton);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });
        icon = (ImageView) findViewById(R.id.icon_name);
        cityNameView = (TextView) findViewById(R.id.text_current_city);
        tempCurrent = (TextView) findViewById(R.id.temp_value);
        tempMinMax = (TextView) findViewById(R.id.temp_min_max);
        weatherDescription = (TextView) findViewById(R.id.weather_value);

        cityId = "700569";
        cityName = "Николаев";

        if(savedInstanceState != null){
            cityId = savedInstanceState.getString("id");
            cityName = savedInstanceState.getString("name");
        }

        cityNameView.setText(cityName);
        startLoader(cityId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", cityId);
        outState.putString("name", cityName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_REQUEST){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                cityId = bundle.getString("cityId");
                cityName = bundle.getString("cityName");
                cityNameView.setText(cityName);
                startLoader(cityId);
            }
        }
    }

    private void startLoader(String id){
        String request = WeatherConfig.URL_WEATHER + "?id="+id+"&APPID="+WeatherConfig.KEY+"&"+WeatherConfig.CELSIUS;
        LoadCurrentWeather weather = new LoadCurrentWeather(request);
        weather.execute();
    }

    private class LoadCurrentWeather extends AsyncTask<String, Void, String>{

        private String resultJson = "";
        private String urlRequest;

        public LoadCurrentWeather(String urlRequest) {
            this.urlRequest = urlRequest;
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buffer = new StringBuffer();

            try {
                URL url = new URL(urlRequest);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject object = new JSONObject(strJson);
                JSONArray array = object.getJSONArray("weather");
                for(int i = 0; i < array.length(); i++){
                    JSONObject weatherObject = array.getJSONObject(i);
                    String str = description.get(weatherObject.getString("description"));
                    weatherDescription.setText(str);
                    iconName = weatherObject.getString("icon");
                }

                JSONObject mainTemp = object.getJSONObject("main");
                String strTemp = mainTemp.getString("temp") + (char) 0x00B0;
                tempCurrent.setText(strTemp);
                String strTempMinMax = mainTemp.getString("temp_max") + (char) 0x00B0 + " ..... "+ mainTemp.getString("temp_min") + (char) 0x00B0;
                tempMinMax.setText(strTempMinMax);
                Picasso.with(MainActivity.this).load(WeatherConfig.URL_ICON + iconName + ".png").into(icon);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
