package com.evdokimoveu.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimoveu.weather.Model.City;

import java.util.ArrayList;

public class CityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<City> cities;

    public CityListAdapter(Context context, ArrayList<City> cities) {
        this.cities = cities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(cities.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.city_list, null);
            TextView name= (TextView) view.findViewById(R.id.city_name_item);
            name.setText(cities.get(position).getName());
        }
        return view;
    }
}
