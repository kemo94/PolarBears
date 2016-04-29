package com.example.kemos.polarbears.Model;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;


/**
 * Created by kemos on 3/28/2016.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, WeatherItem> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
     Context context ;
    double longitude , latitude ;
    WeatherItem weatherData;


    public FetchWeatherTask(Context c, double longitude, double latitude){

        this.context = c ;
        this.longitude = longitude ;
        this.latitude = latitude ;
    }

    public  WeatherItem getWeatherData(){
        return  this.weatherData ;
    }


    public WeatherItem getWeatherDataFromJson(String dataJsonStr )
            throws JSONException {

        final String OWM_MAIN = "main";
        final String OWM_TEMP = "temp";
        final String OWM_WIND = "wind";
        final String OWM_WIND_SPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";
        final String OWM_WIND_HUM = "humidity";


            JSONObject forecastJson = new JSONObject(dataJsonStr);

            WeatherItem weatherItemObject = new WeatherItem();

            JSONObject weatherTemp = forecastJson.getJSONObject(OWM_MAIN);
            float  temp = weatherTemp.getLong(OWM_TEMP);
            float humidity =  weatherTemp.getLong(OWM_WIND_HUM);


            JSONObject weatherWind = forecastJson.getJSONObject(OWM_WIND);
            float  windSpeed = weatherWind.getLong(OWM_WIND_SPEED);
            float  windDirection = weatherWind.getLong(OWM_WIND_DIRECTION);

            Time time = new Time();
            time.setToNow();
            weatherItemObject.setLongitude( longitude);
            weatherItemObject.setLatitude(latitude);
            weatherItemObject.setTemp(temp);
            weatherItemObject.setHumidity(humidity);
            weatherItemObject.setWindSpeed(windSpeed);
            weatherItemObject.setWindDirection(windDirection);
            weatherItemObject.setThickness(12);
            weatherItemObject.setDate(String.valueOf(time.year));


            weatherData = weatherItemObject ;

        return weatherData;

    }
    @Override
    protected WeatherItem doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String dataJsonStr = null;

        String APIKey = "292809690e0a5b88167701de6565fbe1";
        final String longitudeStr = "lon"  ;
        final String latitudeStr = "lat"  ;
        final String APIKey_PARAM = "APPID";
        final String units = "units" ;//metric
        final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
      //  http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&APPID=292809690e0a5b88167701de6565fbe1
        try {

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(longitudeStr, String.valueOf(longitude))
                    .appendQueryParameter(latitudeStr, String.valueOf(latitude))
                    .appendQueryParameter(units, "metric")
                    .appendQueryParameter(APIKey_PARAM, APIKey)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            dataJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast string: " + dataJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
                return getWeatherDataFromJson(dataJsonStr );

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

   @Override
    protected void onPostExecute(WeatherItem result) {
       if ( result != null )
               Log.d("temp" , result.getTemp() + "");

           WeatherOperations weatherOperations ;

       weatherOperations = new WeatherOperations(context);
       try {
           weatherOperations.open();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       if (  weatherOperations.addWeather(
               result.getLongitude() ,
               result.getLatitude(),
               result.getTemp(),
               result.getWindSpeed(),
               result.getWindDirection(),
               result.getHumidity(),
               result.getThickness(),
               result.getDate()) == -1 )

           weatherOperations.updateWeather(result.getLongitude() ,
                   result.getLatitude(),
                   result.getTemp(),
                   result.getWindSpeed(),
                   result.getWindDirection(),
                   result.getHumidity(),
                   result.getThickness(),
                   result.getDate());

           weatherOperations.getWeather(result.getLongitude(),result.getLatitude());
    }
}

