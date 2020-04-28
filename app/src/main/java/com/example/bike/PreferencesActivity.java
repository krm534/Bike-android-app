package com.example.bike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appyvet.materialrangebar.RangeBar;
import com.example.bike.Model.FormHandler;
import com.example.bike.Util.Pref;

public class PreferencesActivity extends AppCompatActivity{
    private Button saveButton;
    private Pref pref;
    FormHandler formHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Initialize Class Objects
        pref = new Pref(this);
        formHandler = new FormHandler(pref, this);

        // Initialize Save Button
        saveButton = findViewById(R.id.save_button);

        // Check if the SharedPreferences keys are filled
        if ((pref.getClear() || pref.getClouds() || pref.getDrizzle() || pref.getRain() || pref.getSnow()) &&
                pref.getZipCode() != 0 && pref.getMaxHumidity() != 0 && pref.getMinHumidity() != 0 &&
                pref.getMaxWindSpeed() != 0 && pref.getMinWindSpeed() != 0 && pref.getMaxTemperature() != 0 &&
                pref.getMinTemperature() != 0) {
            // Setup form based on preferences
            formHandler.setupForm();
        }

        // Save Button Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formHandler.checkFormInput();
            }
        });
    }
}
