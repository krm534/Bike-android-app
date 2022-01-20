package com.example.bike;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bike.Interface.InvalidArrayResponse;
import com.example.bike.Interface.NetworkErrorResponse;
import com.example.bike.Interface.ValidAsyncResponse;
import com.example.bike.Model.Weather;
import com.example.bike.Util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.example.bike.Util.Constants.WEATHER_API_KEY;
import static com.example.bike.Util.Constants.WEATHER_API_URL;

public class WeatherRepository {
    private final ArrayList<Weather> arrayOfWeather = new ArrayList<>();
    private final int DAYS = 7;
    private String fullUrl = WEATHER_API_URL;

    public WeatherRepository(Activity activity) {
        int zipCode = new Pref(activity).getZipCode();
        fullUrl += "zip=" + zipCode + ",us&appid=" + WEATHER_API_KEY + "&cnt=" + DAYS;
    }

    // Get weather if connected to internet
    public void getWeather(final ValidAsyncResponse validCallback,
                           final InvalidArrayResponse invalidArrayCallback,
                           final NetworkErrorResponse networkErrorCallback) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    for (int i = 0; i < DAYS; i++) {
                        try {
                            Weather weather = new Weather();
                            weather.setLocation(response.getJSONObject("city").getString("name"));
                            weather.setDate(response.getJSONArray("list").getJSONObject(i).getInt("dt"));
                            weather.setMaxTemperature((float) response.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getDouble("max"), false);
                            weather.setMinTemperature((float) response.getJSONArray("list").getJSONObject(i).getJSONObject("temp").getDouble("min"), false);
                            weather.setHumidity(response.getJSONArray("list").getJSONObject(i).getInt("humidity"));
                            weather.setWindSpeed((float) response.getJSONArray("list").getJSONObject(i).getDouble("speed"), false);
                            weather.setTypeOfWeather(response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                            arrayOfWeather.add(weather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    validCallback.processFinished(arrayOfWeather);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                if(error.networkResponse.data != null) {
                    try {
                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        JSONObject jsonObject = new JSONObject(body);
                        if (jsonObject.getString("message").equals("city not found")) {
                            invalidArrayCallback.processFinished();
                        } else {
                            // Network Error Occurred
                            System.out.printf("ERROR: %s%n", error.networkResponse.data);
                            networkErrorCallback.processFinished(error.networkResponse);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Network Error Occurred
                    networkErrorCallback.processFinished(error.networkResponse);
                }
            }
        });

        // Add request to Volley queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    // Get weather if no internet connection but weather data is stored in SharedPreferences
    public ArrayList<Weather> getWeather(JSONArray jsonArray)  {
        ArrayList<Weather> arrayOfWeather = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length() - 1; i++) {
                Weather weather = new Weather();
                weather.setDate(jsonArray.getJSONObject(i).getInt("date"));
                weather.setLocation(jsonArray.getJSONObject(i).getString("location"));
                weather.setMaxTemperature((float) jsonArray.getJSONObject(i).getDouble("maxTemp"), true);
                weather.setMinTemperature((float) jsonArray.getJSONObject(i).getDouble("minTemp"), true);
                weather.setHumidity(jsonArray.getJSONObject(i).getInt("humidity"));
                weather.setWindSpeed((float) jsonArray.getJSONObject(i).getDouble("windSpeed"), true);
                weather.setTypeOfWeather(jsonArray.getJSONObject(i).getString("typeOfWeather"));
                arrayOfWeather.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayOfWeather;
    }
}
