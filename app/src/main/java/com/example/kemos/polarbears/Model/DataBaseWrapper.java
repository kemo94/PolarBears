package com.example.kemos.polarbears.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseWrapper extends SQLiteOpenHelper {

    public static final String WEATHER_TABLE = "WEATHER";
    public static final String WEATHER_LONGITUDE = "longitude";
    public static final String WEATHER_LATITUDE = "latitude";
    public static final String WEATHER_TEMP = "temp";
    public static final String WEATHER_DATE = "date";
    public static final String WEATHER_THICKNESS = "thickness";
    public static final String WEATHER_WIND_SPEED = "wind_speed";
    public static final String WEATHER_WIND_DIRECTION = "wind_direction";
    public static final String WEATHER_HUM = "hum";
    private static final String DATABASE_NAME = "WEATHER2.db";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table " + WEATHER_TABLE

            + "("  + WEATHER_LONGITUDE + " float , "
            + WEATHER_LATITUDE + " float , "
            + WEATHER_TEMP + " float, "
            + WEATHER_WIND_SPEED + " float, "
            + WEATHER_WIND_DIRECTION + " float, "
            + WEATHER_HUM + " float, "
            + WEATHER_THICKNESS + " float, "
            + WEATHER_DATE + " text ,"
            + " constraint w_id primary key( " + WEATHER_LONGITUDE + " , " + WEATHER_LATITUDE +" )   );" ;

    public DataBaseWrapper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);

        onCreate(db);

    }

}
