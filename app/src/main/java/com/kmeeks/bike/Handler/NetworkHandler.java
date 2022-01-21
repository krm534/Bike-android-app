package com.kmeeks.bike.Handler;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kmeeks.bike.R;
import com.kmeeks.bike.Util.Pref;

public class NetworkHandler {
    private final Context mContext;
    private final Pref mPref;
    private TextView mNoPreferences;
    private final WeatherHandler mWeatherHandler;

    public NetworkHandler(Context context, Pref pref, WeatherHandler weatherHandler) {
        mContext = context;
        mPref = pref;
        mWeatherHandler = weatherHandler;
    }

    // Check if user is connected to network
    public boolean hasNetworkConnection() {
        boolean hasConnectivity = false;

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        mNoPreferences = ((Activity) mContext).findViewById(R.id.no_preferences_textview);

        // Check if the SharedPreferences keys are filled
        if ((mPref.getClear() || mPref.getClouds() || mPref.getDrizzle() || mPref.getRain() || mPref.getSnow()) &&
                mPref.getZipCode() != 0 && mPref.getMaxHumidity() != 0 && mPref.getMinHumidity() != 0 &&
                mPref.getMaxWindSpeed() != 0 && mPref.getMinWindSpeed() != 0 && mPref.getMaxTemperature() != 0 &&
                mPref.getMinTemperature() != 0) {
            mWeatherHandler.getData();
        } else if (mPref.getCheckedZipCode()) {
            mNoPreferences.setText(R.string.invalid_zipcode);
        } else {
            mNoPreferences.setText(R.string.no_preferences_selected);
        }
    }

    // Run this code if not connected to network
    public void notConnectedToNetwork() {
        ImageButton settings = ((Activity) mContext).findViewById(R.id.settings_imagebutton);

        // Check if weather data is stored in preference
        if (!(mPref.getWeatherData().isEmpty())) {
            prefIsNotEmpty();
        } else {
            prefIsEmpty();
            settings.setVisibility(View.GONE);
        }
    }

    // Run this code if not connected to network and no preferences are stored
    private void prefIsEmpty() {
        if (mPref.getCheckedZipCode()) {
            mNoPreferences.setText(R.string.invalid_zipcode_and_no_network);
        } else if (!mPref.getClear() && !mPref.getClouds() && !mPref.getDrizzle() && !mPref.getRain() && !mPref.getSnow() &&
                mPref.getZipCode() == 0 && mPref.getMaxHumidity() == 0 && mPref.getMinHumidity() == 0 &&
                mPref.getMaxWindSpeed() == 0 && mPref.getMinWindSpeed() == 0 && mPref.getMaxTemperature() == 0 &&
                mPref.getMinTemperature() == 0) {
            mNoPreferences.setText(R.string.no_preferences_and_no_network);
        } else {
            mNoPreferences.setText(R.string.network_error);
        }
    }

    // Run this code if not connected to network and preferences are stored
    private void prefIsNotEmpty() {
        mWeatherHandler.handleUpdateUI();
    }
}
