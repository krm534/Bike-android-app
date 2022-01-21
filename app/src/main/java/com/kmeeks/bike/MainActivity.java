package com.kmeeks.bike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.kmeeks.bike.Model.Comparison;
import com.kmeeks.bike.Handler.NetworkHandler;
import com.kmeeks.bike.Handler.WeatherHandler;
import com.kmeeks.bike.Util.Pref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private WeatherHandler mWeatherHandler;
    private final int LAUNCH_FIRST_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate Classes
        Pref pref = new Pref(this);
        Comparison comparison = new Comparison();
        WeatherRepository weatherRepository = new WeatherRepository(this);
        mWeatherHandler = new WeatherHandler(this, comparison, pref, weatherRepository);
        NetworkHandler networkHandler = new NetworkHandler(this, pref, mWeatherHandler);

        // Find UI Views
        ImageButton settings = findViewById(R.id.settings_imagebutton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton previousButton = findViewById(R.id.prevButton);

        // Set Button listeners
        settings.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        // Check network status
        if (networkHandler.hasNetworkConnection()) {
            networkHandler.isConnectedToNetwork();
        } else {
            networkHandler.notConnectedToNetwork();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_imagebutton:
                redirectToPreferences();
                break;
            case R.id.prevButton:
                mWeatherHandler.reduceIndex();
                mWeatherHandler.handleUpdateUI();
                break;
            case R.id.nextButton:
                mWeatherHandler.increaseIndex();
                mWeatherHandler.handleUpdateUI();
                break;
        }
    }

    private void redirectToPreferences() {
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivityForResult(intent, LAUNCH_FIRST_ACTIVITY);
    }

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
