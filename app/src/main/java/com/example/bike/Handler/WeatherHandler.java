package com.example.bike.Handler;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.android.volley.NetworkResponse;
import com.example.bike.Interface.InvalidArrayResponse;
import com.example.bike.Interface.NetworkErrorResponse;
import com.example.bike.Model.Comparison;
import com.example.bike.Model.Weather;
import com.example.bike.WeatherRepository;
import com.example.bike.Interface.ValidAsyncResponse;
import com.example.bike.R;
import com.example.bike.Util.Pref;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WeatherHandler {
    private final Activity activity;
    private final Comparison comparison;
    private final Pref pref;
    private final TextView date;
    private ArrayList<Weather> weatherList;
    private Weather weather;
    private int unixDate;
    private int index = 0;
    private final WeatherRepository weatherRepository;
    private final LinearLayoutCompat linearLayoutCompat;
    private final CardView cardView;
    private final TextView noPreferences;

    public WeatherHandler(Activity activity, Comparison comparison, Pref pref, WeatherRepository weatherRepository) {
        this.activity = activity;
        this.comparison = comparison;
        this.pref = pref;
        this.date = activity.findViewById(R.id.date_textview);
        this.noPreferences = activity.findViewById(R.id.no_preferences_textview);
        this.cardView = activity.findViewById(R.id.display_cardview);
        this.linearLayoutCompat = activity.findViewById(R.id.linearLayoutCompat1);
        this.weatherRepository = weatherRepository;
    }

    // Update UI with data from API
    public void handleUpdateUI() {
        // Check if data is stored in weather array
        if (weatherList == null) {
            try {
                JSONArray jsonArray = new JSONArray(pref.getWeatherData());
                weatherList = weatherRepository.getWeather(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Set current weather object
        weather = weatherList.get(index);
        unixDate = weatherList.get(index).getDate();

        // Compare preferred and actual weather data
        compareData();

        // Calculate date from data
        getDate();

        // Change UI elements
        noPreferences.setText(R.string.no_preferences_selected);
        noPreferences.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        linearLayoutCompat.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
    }

    // Save weather data as string format in SharedPreferences
    private void saveWeatherData(ArrayList<Weather> weatherData) {
        String weatherDataInStringFormat = "";
        weatherDataInStringFormat += "[";

        for (Weather w: weatherData) {
            weatherDataInStringFormat += "{\"date\": \"" + w.getDate() + "\",";
            weatherDataInStringFormat += "\"location\": \"" + w.getLocation() + "\",";
            weatherDataInStringFormat += "\"typeOfWeather\": \"" + w.getTypeOfWeather() + "\",";
            weatherDataInStringFormat += "\"humidity\": \"" + w.getHumidity() + "\",";
            weatherDataInStringFormat += "\"windSpeed\": \"" + w.getWindSpeed() + "\",";
            weatherDataInStringFormat += "\"minTemp\": \"" + w.getMinTemperature() + "\",";
            weatherDataInStringFormat += "\"maxTemp\": \"" + w.getMaxTemperature() + "\"},";
        }

        weatherDataInStringFormat += "]";
        pref.saveWeatherData(weatherDataInStringFormat);
    }

    // Compare type of weather (Extension of compareData())
    private void compareWeatherTypes(String type) {
        switch (type) {
            case "Rain":
                comparison.compareTypeOfWeather(pref.getRain());
                break;
            case "Snow":
                comparison.compareTypeOfWeather(pref.getSnow());
                break;
            case "Clear":
                comparison.compareTypeOfWeather(pref.getClear());
                break;
            case "Clouds":
                comparison.compareTypeOfWeather(pref.getClouds());
                break;
            case "Drizzle":
                comparison.compareTypeOfWeather(pref.getDrizzle());
        }
    }

    // Compare preferred weather and actual weather
    private void compareData() {
        comparison.compareTemperature(weather.getMaxTemperature(), weather.getMinTemperature(), pref.getMaxTemperature(), pref.getMinTemperature());
        comparison.compareHumidity(weather.getHumidity(),pref.getMinHumidity(), pref.getMaxHumidity());
        comparison.compareWindSpeed(weather.getWindSpeed(), pref.getMinWindSpeed(), pref.getMaxWindSpeed());
        compareWeatherTypes(weather.getTypeOfWeather());

        // Call method to display the score to user
        int score = comparison.getScore();
        displayUI(score, weather.getLocation());
    }

    // Convert and display readable score to user
    private void displayUI(int score, String location) {
        TextView scoreMessage = activity.findViewById(R.id.message_textview);
        ImageView displayImage = activity.findViewById(R.id.display_imageview);

        if (score == 1) {
            scoreMessage.setText(String.format("Bad day to bike in %s", location));
            displayImage.setImageResource(R.drawable.bad);

        } else if (score == 2) {
            scoreMessage.setText(String.format("Mediocre day to bike in %s", location));
            displayImage.setImageResource(R.drawable.mediocre);
        } else if (score == 3) {
            scoreMessage.setText(String.format("Good day to bike in %s", location));
            displayImage.setImageResource(R.drawable.good);
        } else {
            scoreMessage.setText(String.format("Great day to bike in %s", location));
            displayImage.setImageResource(R.drawable.great);
        }
    }

    // Convert Unix weather date to recognizable UTC date
    private void getDate() {
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixDate*1000L);

        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy z", java.util.Locale.getDefault());

        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        setDate(formattedDate);
    }

    // Set the date in the UI
    private void setDate(String receivedDate) {
        date.setText(receivedDate);
    }

    // Handle receiving data from API
    public void getData() {
        // Get weather data from weather API
        weatherRepository.getWeather(new ValidAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Weather> weatherArrayList) {
                handleAsyncData(weatherArrayList);
            }
        }, new InvalidArrayResponse() {
            @Override
            public void processFinished() {
                handleInvalidZipCode();
            }
        }, new NetworkErrorResponse() {
            @Override
            public void processFinished(NetworkResponse response) {
                handleInvalidNetworkResponse(response);
            }
        });
    }

    // Get weather data from weather API
    private void handleAsyncData(ArrayList<Weather> weatherArrayList) {
        // Initialize attributes
        this.weatherList = weatherArrayList;

        // Set weather data in a preference
        saveWeatherData(weatherArrayList);

        // Set invalid zip code check to false
        pref.saveCheckedZipCode(false);

        // Update UI
        handleUpdateUI();
    }

    // Handle invalid zip code entrance
    private void handleInvalidZipCode() {
        // Set Invalid Zip Code to true
        pref.saveCheckedZipCode(true);

        // Change UI elements
        cardView.setVisibility(View.GONE);
        linearLayoutCompat.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        noPreferences.setText(R.string.invalid_zipcode);
        noPreferences.setVisibility(View.VISIBLE);
    }

    // Handle network error during API call
    private void handleInvalidNetworkResponse(NetworkResponse networkResponse) {
        noPreferences.setText(String.format("Network Error: %s", networkResponse));
    }

    public void reduceIndex() {
        index--;
        if (index < 0) {
            index = weatherList.size() - 1;
        }
    }

    public void increaseIndex() {
        index = (index + 1) % weatherList.size();
    }
}
