package com.example.bike.Model;

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

    // Check if user is connected to Wifi or Mobile service
    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
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

    // Run this code if connected to network
    public void ConnectedToNetwork() {
        // Set TextView
        noPreferences = ((Activity) context).findViewById(R.id.no_preferences_textview);

        // Check if the SharedPreferences keys are filled
        if ((pref.getClear() || pref.getClouds() || pref.getDrizzle() || pref.getRain() || pref.getSnow()) &&
                pref.getZipCode() != 0 && pref.getMaxHumidity() != 0 && pref.getMinHumidity() != 0 &&
                pref.getMaxWindSpeed() != 0 && pref.getMinWindSpeed() != 0 && pref.getMaxTemperature() != 0 &&
                pref.getMinTemperature() != 0) {
            weatherHandler.getData();
        }
        else if (pref.getCheckedZipCode()) {
            noPreferences.setText(R.string.invalid_zipcode);
        }
        else {
            noPreferences.setText(R.string.no_preferences_selected);
        }
    }

    // Run this code if not connected to network
    public void NotConnectedToNetwork() {
        // Set TextView
        ImageButton settings = ((Activity) context).findViewById(R.id.settings_imagebutton);

        // Check if weather data is stored in preference
        if (!(pref.getWeatherData().isEmpty())) {
            PrefIsNotEmpty();
        }
        else {
            PrefIsEmpty();
            settings.setVisibility(View.GONE);
        }
    }

    // Run this code if not connected to network and no preferences are stored
    private void PrefIsEmpty() {
        if (pref.getCheckedZipCode()) {
            noPreferences.setText(R.string.invalid_zipcode_and_no_network);
        }
        else if (!pref.getClear() && !pref.getClouds() && !pref.getDrizzle() && !pref.getRain() && !pref.getSnow() &&
                pref.getZipCode() == 0 && pref.getMaxHumidity() == 0 && pref.getMinHumidity() == 0 &&
                pref.getMaxWindSpeed() == 0 && pref.getMinWindSpeed() == 0 && pref.getMaxTemperature() == 0 &&
                pref.getMinTemperature() == 0) {
            noPreferences.setText(R.string.no_preferences_and_no_network);
        }
        else {
            noPreferences.setText(R.string.network_error);
        }
    }

    // Run this code if not connected to network and preferences are stored
    private void PrefIsNotEmpty() {
        weatherHandler.handleUpdateUI();
    }
}
