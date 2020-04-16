package com.example.bike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.example.bike.Data.InvalidArrayResponse;
import com.example.bike.Data.NetworkErrorResponse;
import com.example.bike.Data.WeatherRepository;
import com.example.bike.Data.WeatherRepositoryAsyncResponse;
import com.example.bike.Model.Comparison;
import com.example.bike.Model.Weather;
import com.example.bike.Util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton settings, nextButton, previousButton;
    private ImageView displayImage;
    private CardView cardView;
    private TextView noPreferences, date, scoreMessage;
    private LinearLayoutCompat linearLayoutCompat;
    private WeatherRepository weatherRepository;
    private Comparison comparison;
    private List<Weather> weatherList = new ArrayList<>();
    private Pref pref;
    private int index = 0;

    private final int LAUNCH_FIRST_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate SharedPreferences Class
        pref = new Pref(this);
        comparison = new Comparison();

        // Find UI Views
        settings = (ImageButton) findViewById(R.id.settings_imagebutton);
        cardView = (CardView) findViewById(R.id.display_cardview);
        noPreferences = (TextView) findViewById(R.id.no_preferences_textview);
        date = (TextView) findViewById(R.id.date_textview);
        linearLayoutCompat = (LinearLayoutCompat) findViewById(R.id.linearLayoutCompat1);
        scoreMessage = findViewById(R.id.message_textview);
        displayImage = findViewById(R.id.display_imageview);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.prevButton);

        // Set Button listeners
        settings.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        // Check network
        if (haveNetworkConnection()) {
            System.out.println("CONNECTED!");
            // Check if the SharedPreferences keys are filled
            if ((pref.getClear() || pref.getClouds() || pref.getDrizzle() || pref.getRain() || pref.getSnow()) &&
                    pref.getZipCode() != 0 && pref.getMaxHumidity() != 0 && pref.getMinHumidity() != 0 &&
                    pref.getMaxWindSpeed() != 0 && pref.getMinWindSpeed() != 0 && pref.getMaxTemperature() != 0 &&
                    pref.getMinTemperature() != 0) {

                // Get Data
                getData();
            }
            else if (pref.getCheckedZipCode()) {
                noPreferences.setText("Invalid zip code entered");
            }
            else {
                noPreferences.setText("No preferences have been selected");
            }
        }
        else {
            System.out.println("DISCONNECTED!");
            // Check if weather data is stored in preference
            if (!(pref.getWeatherData().isEmpty())) {
                try {
                    JSONArray jsonArray = new JSONArray(pref.getWeatherData());
                    weatherList = WeatherRepository.GetWeather(jsonArray);
                    handleUpdateUI((ArrayList<Weather>) weatherList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (pref.getCheckedZipCode()) {
                    noPreferences.setText("Invalid zip code entered | No network");
                }
                else if (!pref.getClear() && !pref.getClouds() && !pref.getDrizzle() && !pref.getRain() && !pref.getSnow() &&
                        pref.getZipCode() == 0 && pref.getMaxHumidity() == 0 && pref.getMinHumidity() == 0 &&
                        pref.getMaxWindSpeed() == 0 && pref.getMinWindSpeed() == 0 && pref.getMaxTemperature() == 0 &&
                        pref.getMinTemperature() == 0) {
                    noPreferences.setText("No preferences have been selected | No network");
                }
                else {
                    noPreferences.setText("Network Error");
                }
                settings.setVisibility(View.GONE);
            }
        }
    }

    // Check if user is connected to Wifi or Mobile service (Extension of onCreate())
    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    // Get weather data from weather API
    public void getData() {
        weatherList = new WeatherRepository(this).getWeather(new WeatherRepositoryAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Weather> weatherArrayList) {
                // Set weather data in a preference
                saveWeatherData(weatherArrayList);

                // Set invalid zip code check to false
                pref.saveCheckedZipCode(false);

                // Update UI
                handleUpdateUI(weatherArrayList);
            }
        }, new InvalidArrayResponse() {
            @Override
            public void processFinished() {
                // Set Invalid Zip Code to true
                pref.saveCheckedZipCode(true);

                // Change UI elements
                cardView.setVisibility(View.GONE);
                linearLayoutCompat.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                noPreferences.setText("Invalid zip code entered");
                noPreferences.setVisibility(View.VISIBLE);
            }
        }, new NetworkErrorResponse() {
            @Override
            public void processFinished(NetworkResponse networkResponse) {
                noPreferences.setText("Network Error: " + networkResponse);
            }
        });
    }

    // Update UI with data from API
    public void handleUpdateUI(ArrayList<Weather> weather) {
        // Compare preferred and actual weather data
        compareData(weather.get(index));

        // Calculate date from data
        getDate(weather.get(index).getDate());

        // Change UI elements
        noPreferences.setText("No preferences have been selected");
        noPreferences.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        linearLayoutCompat.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
    }

    // Save weather data as string format in SharedPreferences
    public void saveWeatherData(ArrayList<Weather> weatherData) {
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

    // Convert Unix weather date to recognizable UTC date
    public void getDate(int UnixDate) {

        // convert seconds to milliseconds
        Date date = new java.util.Date(UnixDate*1000L);

        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy z");

        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(TimeZone.getTimeZone(String.valueOf("UTC")));
        String formattedDate = sdf.format(date);
        setDate(formattedDate);
    }

    // Set the date in the UI
    public void setDate(String receivedDate) {
        date.setText(receivedDate);
    }

    // Compare preferred weather and actual weather
    public void compareData(Weather w) {
        comparison.compareTemperature(w.getMaxTemperature(), w.getMinTemperature(), pref.getMaxTemperature(), pref.getMinTemperature());
        comparison.compareHumidity(w.getHumidity(),pref.getMinHumidity(), pref.getMaxHumidity());
        comparison.compareWindSpeed(w.getWindSpeed(), pref.getMinWindSpeed(), pref.getMaxWindSpeed());
        compareTypeOfWeather(w.getTypeOfWeather());

        // Call method to display the score to user
        int score = comparison.getScore();
        displayUI(score, w.getLocation());
    }

    // Compare type of weather (Extension of compareData())
    public void compareTypeOfWeather(String type) {
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

    // Convert and display readable score to user
    public void displayUI(int score, String location) {
        if (score == 1) {
            scoreMessage.setText("Bad day to bike in " + location);
            displayImage.setImageResource(R.drawable.bad);

        }
        else if (score == 2) {
            scoreMessage.setText("Mediocre day to bike in " + location);
            displayImage.setImageResource(R.drawable.mediocre);
        }
        else if (score == 3) {
            scoreMessage.setText("Good day to bike in " + location);
            displayImage.setImageResource(R.drawable.good);
        }
        else {
            scoreMessage.setText("Great day to bike in " + location);
            displayImage.setImageResource(R.drawable.great);
        }
    }

    // Monitor button clicks
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_imagebutton:
                redirectToNewActivity();
                break;
            case R.id.prevButton:
                index--;
                if (index < 0) {
                    index = weatherList.size() - 1;
                }
                getDate(weatherList.get(index).getDate());
                compareData(weatherList.get(index));
                break;
            case R.id.nextButton:
                index = (index + 1) % weatherList.size();
                getDate(weatherList.get(index).getDate());
                compareData(weatherList.get(index));
                break;
        }
    }

    // Redirect to PreferencesActivity (Extension of onClick())
    protected void redirectToNewActivity() {
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivityForResult(intent, LAUNCH_FIRST_ACTIVITY);
    }

    // Recreate MainActivity when control is returned from PreferenceActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_FIRST_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                recreate();
            }
        }
    }
}
