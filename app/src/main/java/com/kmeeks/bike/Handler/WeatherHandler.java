package com.kmeeks.bike.Handler;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.android.volley.NetworkResponse;
import com.kmeeks.bike.Interface.InvalidArrayResponse;
import com.kmeeks.bike.Interface.NetworkErrorResponse;
import com.kmeeks.bike.Model.Comparison;
import com.kmeeks.bike.Model.Weather;
import com.kmeeks.bike.WeatherRepository;
import com.kmeeks.bike.Interface.ValidAsyncResponse;
import com.kmeeks.bike.R;
import com.kmeeks.bike.Util.Pref;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class WeatherHandler {
    private final Activity mActivity;
    private final Comparison mComparison;
    private final Pref mPref;
    private final TextView mDate;
    private ArrayList<Weather> mWeatherList;
    private Weather mWeather;
    private int mUnixDate;
    private int mIndex = 0;
    private final WeatherRepository mWeatherRepository;
    private final LinearLayoutCompat mLinearLayoutCompat;
    private final CardView mCardView;
    private final TextView mNoPreferences;

    public WeatherHandler(Activity activity, Comparison comparison, Pref pref, WeatherRepository weatherRepository) {
        mActivity = activity;
        mComparison = comparison;
        mPref = pref;
        mDate = activity.findViewById(R.id.date_textview);
        mNoPreferences = activity.findViewById(R.id.no_preferences_textview);
        mCardView = activity.findViewById(R.id.display_cardview);
        mLinearLayoutCompat = activity.findViewById(R.id.linearLayoutCompat1);
        mWeatherRepository = weatherRepository;
    }

    // Update UI with data from API
    public void handleUpdateUI() {
        // Check if data is stored in weather array
        if (mWeatherList == null) {
            try {
                JSONArray jsonArray = new JSONArray(mPref.getWeatherData());
                mWeatherList = mWeatherRepository.getWeather(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Set current weather object
        mWeather = mWeatherList.get(mIndex);
        mUnixDate = mWeatherList.get(mIndex).getDate();

        // Compare preferred and actual weather data
        compareData();

        // Calculate date from data
        getDate();

        // Change UI elements
        mNoPreferences.setText(R.string.no_preferences_selected);
        mNoPreferences.setVisibility(View.GONE);
        mCardView.setVisibility(View.VISIBLE);
        mLinearLayoutCompat.setVisibility(View.VISIBLE);
        mDate.setVisibility(View.VISIBLE);
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
        mPref.saveWeatherData(weatherDataInStringFormat);
    }

    // Compare type of weather (Extension of compareData())
    private void compareWeatherTypes(String type) {
        switch (type) {
            case "Rain":
                mComparison.compareTypeOfWeather(mPref.getRain());
                break;
            case "Snow":
                mComparison.compareTypeOfWeather(mPref.getSnow());
                break;
            case "Clear":
                mComparison.compareTypeOfWeather(mPref.getClear());
                break;
            case "Clouds":
                mComparison.compareTypeOfWeather(mPref.getClouds());
                break;
            case "Drizzle":
                mComparison.compareTypeOfWeather(mPref.getDrizzle());
        }
    }

    // Compare preferred weather and actual weather
    private void compareData() {
        mComparison.compareTemperature(mWeather.getMaxTemperature(), mWeather.getMinTemperature(), mPref.getMaxTemperature(), mPref.getMinTemperature());
        mComparison.compareHumidity(mWeather.getHumidity(), mPref.getMinHumidity(), mPref.getMaxHumidity());
        mComparison.compareWindSpeed(mWeather.getWindSpeed(), mPref.getMinWindSpeed(), mPref.getMaxWindSpeed());
        compareWeatherTypes(mWeather.getTypeOfWeather());

        // Call method to display the score to user
        int score = mComparison.getScore();
        displayUI(score, mWeather.getLocation());
    }

    // Convert and display readable score to user
    private void displayUI(int score, String location) {
        TextView scoreMessage = mActivity.findViewById(R.id.message_textview);
        ImageView displayImage = mActivity.findViewById(R.id.display_imageview);

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
        Date date = new java.util.Date(mUnixDate *1000L);

        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy z", java.util.Locale.getDefault());

        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(date);
        setDate(formattedDate);
    }

    // Set the date in the UI
    private void setDate(String receivedDate) {
        mDate.setText(receivedDate);
    }

    // Handle receiving data from API
    public void getData() {
        // Get weather data from weather API
        mWeatherRepository.getWeather(new ValidAsyncResponse() {
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
        mWeatherList = weatherArrayList;

        // Set weather data in a preference
        saveWeatherData(weatherArrayList);

        // Set invalid zip code check to false
        mPref.saveCheckedZipCode(false);

        // Update UI
        handleUpdateUI();
    }

    // Handle invalid zip code entrance
    private void handleInvalidZipCode() {
        // Set Invalid Zip Code to true
        mPref.saveCheckedZipCode(true);

        // Change UI elements
        mCardView.setVisibility(View.GONE);
        mLinearLayoutCompat.setVisibility(View.GONE);
        mDate.setVisibility(View.GONE);
        mNoPreferences.setText(R.string.invalid_zipcode);
        mNoPreferences.setVisibility(View.VISIBLE);
    }

    // Handle network error during API call
    private void handleInvalidNetworkResponse(NetworkResponse networkResponse) {
        mNoPreferences.setText(String.format("Network Error: %s", networkResponse));
    }

    public void reduceIndex() {
        mIndex--;
        if (mIndex < 0) {
            mIndex = mWeatherList.size() - 1;
        }
    }

    public void increaseIndex() {
        mIndex = (mIndex + 1) % mWeatherList.size();
    }
}
