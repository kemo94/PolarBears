package com.example.kemos.polarbears.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;


public class WeatherOperations {

    private DataBaseWrapper dbHelper;

    private SQLiteDatabase database;

    public WeatherOperations(Context context) {

        dbHelper = new DataBaseWrapper(context);

    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

    }
    public void close() {

        dbHelper.close();

    }

    public WeatherItem getWeather(double lon , double lat ) {
        Cursor cursor  = database.rawQuery("select * from " + DataBaseWrapper.WEATHER_TABLE
                +"  where " + DataBaseWrapper.WEATHER_LATITUDE + "=" + lat
                + " and " + DataBaseWrapper.WEATHER_LONGITUDE + "=" + lon
                ,null);
        cursor.moveToFirst();

        return  parseWeather(cursor);
    }
    public void updateWeather(double lon , double lat , double temp ,double windSpeed ,
                              double windDirection , double humidity , double thickness , String date) {
         database.execSQL("update  " + DataBaseWrapper.WEATHER_TABLE
                +"  set " + DataBaseWrapper.WEATHER_TEMP + "=" + temp
                +"  , " + DataBaseWrapper.WEATHER_WIND_SPEED + "=" + windSpeed
                +"  , " + DataBaseWrapper.WEATHER_WIND_DIRECTION + "=" + windDirection
                +"  , " + DataBaseWrapper.WEATHER_HUM + "=" + humidity
                +"  , " + DataBaseWrapper.WEATHER_THICKNESS + "=" + thickness
                +"  , " + DataBaseWrapper.WEATHER_DATE + "=" + date
                +"  where " + DataBaseWrapper.WEATHER_LATITUDE + "=" + lat
                + " and " + DataBaseWrapper.WEATHER_LONGITUDE + "=" + lon );

    }


    public long addWeather( double longitude , double latitude , double temp , double windSpeed ,
                            double windDirection , double humidity , double thickness, String date) {
        ContentValues values = new ContentValues();

        values.put(DataBaseWrapper.WEATHER_LONGITUDE, longitude);
        values.put(DataBaseWrapper.WEATHER_LATITUDE, latitude);
        values.put(DataBaseWrapper.WEATHER_TEMP, temp);
        values.put(DataBaseWrapper.WEATHER_DATE, date);
        values.put(DataBaseWrapper.WEATHER_WIND_SPEED, windSpeed);
        values.put(DataBaseWrapper.WEATHER_WIND_DIRECTION, windDirection);
        values.put(DataBaseWrapper.WEATHER_HUM, humidity);
        values.put(DataBaseWrapper.WEATHER_THICKNESS, thickness);

        return database.insert(DataBaseWrapper.WEATHER_TABLE, null, values);

    }
    private WeatherItem parseWeather(Cursor cursor) {

        WeatherItem WeatherItem = new WeatherItem();

        WeatherItem.setLongitude(cursor.getFloat(0));
        WeatherItem.setLatitude(cursor.getFloat(1));
        WeatherItem.setTemp(cursor.getFloat(2));
        WeatherItem.setWindSpeed(cursor.getFloat(3));
        WeatherItem.setWindDirection(cursor.getFloat(4));
        WeatherItem.setHumidity(cursor.getFloat(5));
        WeatherItem.setThickness(cursor.getFloat(6));
        WeatherItem.setDate(cursor.getString(7));


        Log.d("temp db uu ----", WeatherItem.getTemp() + "");
        Log.d("date db uu--- ", WeatherItem.getDate() + "");
        Log.d("lon db uu--- ", WeatherItem.getLongitude() + "");
        return WeatherItem;

    }

}
