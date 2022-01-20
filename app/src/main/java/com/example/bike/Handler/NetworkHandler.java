package com.example.bike.Handler;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bike.R;
import com.example.bike.Util.Pref;

public class NetworkHandler {
    private final Context context;
    private final Pref pref;
    private TextView noPreferences;
    private final WeatherHandler weatherHandler;

    public NetworkHandler(Context context, Pref pref, WeatherHandler weatherHandler) {
        this.context = context;
        this.pref = pref;
        this.weatherHandler = weatherHandler;
    }

    // Check if user is connected to network
    public boolean hasNetworkConnection() {
        boolean hasConnectivity = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni.getTypeName().equalsIgnoreCase("MOBILE")) && ni.isConnected())
                hasConnectivity = true;
        }
        return hasConnectivity;
    }

    // Run this code if connected to network
    public void isConnectedToNetwork() {
        noPreferences = ((Activity) context).findViewById(R.id.no_preferences_textview);

        // Check if the SharedPreferences keys are filled
        if ((pref.getClear() || pref.getClouds() || pref.getDrizzle() || pref.getRain() || pref.getSnow()) &&
                pref.getZipCode() != 0 && pref.getMaxHumidity() != 0 && pref.getMinHumidity() != 0 &&
                pref.getMaxWindSpeed() != 0 && pref.getMinWindSpeed() != 0 && pref.getMaxTemperature() != 0 &&
                pref.getMinTemperature() != 0) {
            weatherHandler.getData();
        } else if (pref.getCheckedZipCode()) {
            noPreferences.setText(R.string.invalid_zipcode);
        } else {
            noPreferences.setText(R.string.no_preferences_selected);
        }
    }

    // Run this code if not connected to network
    public void notConnectedToNetwork() {
        ImageButton settings = ((Activity) context).findViewById(R.id.settings_imagebutton);

        // Check if weather data is stored in preference
        if (!(pref.getWeatherData().isEmpty())) {
            prefIsNotEmpty();
        } else {
            prefIsEmpty();
            settings.setVisibility(View.GONE);
        }
    }

    // Run this code if not connected to network and no preferences are stored
    private void prefIsEmpty() {
        if (pref.getCheckedZipCode()) {
            noPreferences.setText(R.string.invalid_zipcode_and_no_network);
        } else if (!pref.getClear() && !pref.getClouds() && !pref.getDrizzle() && !pref.getRain() && !pref.getSnow() &&
                pref.getZipCode() == 0 && pref.getMaxHumidity() == 0 && pref.getMinHumidity() == 0 &&
                pref.getMaxWindSpeed() == 0 && pref.getMinWindSpeed() == 0 && pref.getMaxTemperature() == 0 &&
                pref.getMinTemperature() == 0) {
            noPreferences.setText(R.string.no_preferences_and_no_network);
        } else {
            noPreferences.setText(R.string.network_error);
        }
    }

    // Run this code if not connected to network and preferences are stored
    private void prefIsNotEmpty() {
        weatherHandler.handleUpdateUI();
    }
}
