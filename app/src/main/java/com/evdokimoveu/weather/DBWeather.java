package com.evdokimoveu.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBWeather extends SQLiteOpenHelper {

    public final static String DB_COUNTRY_COD_FIELD = "cod";
    public final static String DB_COUNTRY_NAME_FIELD = "name";
    public final static String DB_CITY_ID_FIELD = "id";
    public final static String DB_CITY_NAME_FIELD = "name";
    public final static String DB_CITY_COUNTRY_COD_FIELD = "country_cod";
    public final static String DB_CITY_FAVORITE_FIELD = "favorite";

    public final static String DATA_BASE_NAME = "weather.db";
    public final static String TABLE_COUNTRY = "country";
    public final static String TABLE_CITY = "city";

    public DBWeather(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CITY +
                " (" +
                DB_CITY_ID_FIELD + " VARCHAR PRIMARY KEY, " +
                DB_CITY_NAME_FIELD + " VARCHAR NOT NULL, " +
                DB_CITY_COUNTRY_COD_FIELD + " VARCHAR NOT NULL, " +
                DB_CITY_FAVORITE_FIELD + " VARCHAR); " +
                "CREATE TABLE " + TABLE_COUNTRY +
                " (" +
                DB_COUNTRY_COD_FIELD + " VARCHAR PRIMARY KEY, " +
                DB_COUNTRY_NAME_FIELD + " VARCHAR PRIMARY KEY); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        onCreate(db);
    }
}