package com.example.bike.Data;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bike.Controller.AppController;
import com.example.bike.Model.Weather;
import com.example.bike.Util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherRepository {
    private String url = "https://api.openweathermap.org/data/2.5/forecast/daily?";
    private int days;
    private String API_key;
    private int zipCode;
    private ArrayList<Weather> arrayOfWeather = new ArrayList<>();
    private final int DAYS = 7;

    public WeatherRepository(Activity activity) {
        zipCode = new Pref(activity).getZipCode();
        days = 7;
        API_key= "e9eb0a43c8fdbe3ac1d4df98bbe7ab5e";
        url += "zip=" + zipCode + ",us&appid=" + API_key + "&cnt=" + days;
    }

    // Get weather if connected to internet
    public List getWeather(final WeatherRepositoryAsyncResponse callBack, final InvalidArrayResponse callback2, final NetworkErrorResponse callback3) {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                    callBack.processFinished(arrayOfWeather);
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
                            callback2.processFinished();
                        }
                        else {
                            // Network Error Occurred
                            System.out.println("ERROR: " + error.networkResponse.data);
                            callback3.processFinished(error.networkResponse);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // Network Error Occurred
                    callback3.processFinished(error.networkResponse);
                }
            }
        });

        // Add request to Volley queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return arrayOfWeather;
    }

    // Get weather if no internet connection but weather data is stored in SharedPreferences
    public ArrayList<Weather> GetWeather(JSONArray jsonArray)  {
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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayOfWeather;
    }
}
