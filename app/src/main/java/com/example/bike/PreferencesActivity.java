package com.example.bike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.materialrangebar.IRangeBarFormatter;
import com.appyvet.materialrangebar.RangeBar;
import com.example.bike.Util.Pref;

import java.sql.BatchUpdateException;

public class PreferencesActivity extends AppCompatActivity implements RangeBar.OnRangeBarChangeListener{
    private RangeBar temperatureRangebar, windSpeedRangebar, humidityRangebar;
    private CheckBox snowCheckBox, rainCheckBox, clearCheckBox, cloudsCheckBox, drizzleCheckBox;
    private EditText zipCodeEditText;
    private TextView tempError, windSpeedError, humidityError, conditionsError, zipCodeError;
    private Button saveButton;
    private int maxTemp, minTemp, maxHumidity, minHumidity, maxWindSpeed, minWindSpeed;
    private boolean checkTempRangeBarInput, checkWindSpeedRangeBarInput, checkHumidityRangeBarInput, checkCheckBoxesInput, checkZipCodeInput;
    private Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Initialize Pref class
        pref = new Pref(this);

        // Rangebars
        temperatureRangebar = findViewById(R.id.SearchrangeSeekbarTemperature);
        windSpeedRangebar = findViewById(R.id.SearchrangeSeekbarWindSpeed);
        humidityRangebar = findViewById(R.id.SearchrangeSeekbarHumidity);

        // TextViews
        tempError = (TextView) findViewById(R.id.preferred_temperature_error_textview);
        windSpeedError = (TextView) findViewById(R.id.preferred_wind_speed_error_textview);
        humidityError = (TextView) findViewById(R.id.preferred_humidity_error_textview);
        conditionsError = (TextView) findViewById(R.id.preferred_conditions_error_textview);
        zipCodeError = (TextView) findViewById(R.id.preferred_zipcode_error_textview);

        // Checkboxes
        snowCheckBox = findViewById(R.id.checkbox_snow);
        rainCheckBox = findViewById(R.id.checkbox_rain);
        clearCheckBox = findViewById(R.id.checkbox_clear);
        cloudsCheckBox = findViewById(R.id.checkbox_clouds);
        drizzleCheckBox = findViewById(R.id.checkbox_drizzle);

        // Button
        saveButton = findViewById(R.id.save_button);

        // EditText
        zipCodeEditText = findViewById(R.id.zip_code_edittext);

        // Check if the SharedPreferences keys are filled
        if ((pref.getClear() || pref.getClouds() || pref.getDrizzle() || pref.getRain() || pref.getSnow()) &&
                pref.getZipCode() != 0 && pref.getMaxHumidity() != 0 && pref.getMinHumidity() != 0 &&
                pref.getMaxWindSpeed() != 0 && pref.getMinWindSpeed() != 0 && pref.getMaxTemperature() != 0 &&
                pref.getMinTemperature() != 0) {
            // Setup form based on preferences
            setupForm();
        }

        // Initialize Default Rangebar Min and Max values
        maxTemp = Integer.parseInt(temperatureRangebar.getRightPinValue());
        minTemp = Integer.parseInt(temperatureRangebar.getLeftPinValue());
        maxHumidity = Integer.parseInt(humidityRangebar.getRightPinValue());
        minHumidity = Integer.parseInt(humidityRangebar.getLeftPinValue());
        maxWindSpeed = Integer.parseInt(windSpeedRangebar.getRightPinValue());
        minWindSpeed = Integer.parseInt(windSpeedRangebar.getLeftPinValue());

        // Rangebar Listeners
        temperatureRangebar.setOnRangeBarChangeListener(this);
        windSpeedRangebar.setOnRangeBarChangeListener(this);
        humidityRangebar.setOnRangeBarChangeListener(this);

        // Save Button Listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFormInput();
            }
        });
    }

    // Setup the form based on information in SharedPreferences (Extension of onCreate())
    public void setupForm() {
        // Check clear condition
        if (pref.getClear()) {
            clearCheckBox.setChecked(true);
        }
        else {
            clearCheckBox.setChecked(false);
        }

        // Check clouds condition
        if (pref.getClouds()) {
            cloudsCheckBox.setChecked(true);
        }
        else {
            cloudsCheckBox.setChecked(false);
        }

        // Check drizzle condition
        if (pref.getDrizzle()) {
            drizzleCheckBox.setChecked(true);
        }
        else {
            drizzleCheckBox.setChecked(false);
        }

        // Check rain condition
        if (pref.getRain()) {
            rainCheckBox.setChecked(true);
        }
        else {
            rainCheckBox.setChecked(false);
        }

        // Check rain condition
        if (pref.getSnow()) {
            snowCheckBox.setChecked(true);
        }
        else {
            snowCheckBox.setChecked(false);
        }

        // Zip code
        zipCodeEditText.setText(Integer.toString(pref.getZipCode()));

        // Max and Min temperature
        temperatureRangebar.setRangePinsByValue(pref.getMinTemperature(), pref.getMaxTemperature());

        // Max and Min humidity
        humidityRangebar.setRangePinsByValue(pref.getMinHumidity(), pref.getMaxHumidity());

        // Max and Min wind speed
        windSpeedRangebar.setRangePinsByValue(pref.getMinWindSpeed(), pref.getMaxWindSpeed());
    }

    // Setup SharedPreferences (Extension of checkFormInput())
    public void setupPreferences() {
        pref.saveMinTemperature(minTemp);
        pref.saveMaxTemperature(maxTemp);
        pref.saveMinHumidity(minHumidity);
        pref.saveMaxHumidity(maxHumidity);
        pref.saveMinWindSpeed(minWindSpeed);
        pref.saveMaxWindSpeed(maxWindSpeed);
        pref.saveClear(clearCheckBox.isChecked());
        pref.saveSnow(snowCheckBox.isChecked());
        pref.saveClouds(cloudsCheckBox.isChecked());
        pref.saveDrizzle(drizzleCheckBox.isChecked());
        pref.saveRain(rainCheckBox.isChecked());
        pref.saveZipCode(Integer.parseInt(zipCodeEditText.getText().toString()));
    }

    // Return to MainActivity (Extension of checkFormInput())
    public void returnToMainActivity() {
        setResult(RESULT_OK);
        finish();
    }

    // Validate form input
    public void checkFormInput() {
        // Check Temperature Rangebar Input
        if (minTemp == maxTemp || minTemp < 1 || minTemp > 100 || maxTemp < 1 || maxTemp > 100) {
            checkTempRangeBarInput = false;
            tempError.setVisibility(View.VISIBLE);
        }
        else {
            checkTempRangeBarInput = true;
            tempError.setVisibility(View.GONE);
        }

        // Check Wind Speed Rangebar Input
        if (minWindSpeed == maxWindSpeed || minWindSpeed < 1 || minWindSpeed > 30 || maxWindSpeed < 1 || maxWindSpeed > 30) {
            checkWindSpeedRangeBarInput = false;
            windSpeedError.setVisibility(View.VISIBLE);
        }
        else {
            checkWindSpeedRangeBarInput = true;
            windSpeedError.setVisibility(View.GONE);
        }

        // Check Humidity Rangebar Input
        if (minHumidity == maxHumidity || minHumidity < 1 || minHumidity > 100 || maxHumidity < 1 || maxHumidity > 100) {
            checkHumidityRangeBarInput = false;
            humidityError.setVisibility(View.VISIBLE);
        }
        else {
            checkHumidityRangeBarInput = true;
            humidityError.setVisibility(View.GONE);
        }

        // Check CheckBoxes Input
        if (snowCheckBox.isChecked() || rainCheckBox.isChecked() || clearCheckBox.isChecked() || cloudsCheckBox.isChecked() || drizzleCheckBox.isChecked()) {
            checkCheckBoxesInput = true;
            conditionsError.setVisibility(View.GONE);
        }
        else {
            checkCheckBoxesInput = false;
            conditionsError.setVisibility(View.VISIBLE);
        }

        // Check EditText Input
        if (zipCodeEditText.getText().toString().trim().length() > 0 && zipCodeEditText.getText().toString().trim().matches("[0-9]*") && zipCodeEditText.getText().toString().trim().length() < 10) {
            checkZipCodeInput = true;
            zipCodeError.setVisibility(View.GONE);
        }
        else {
            checkZipCodeInput = false;
            zipCodeError.setVisibility(View.VISIBLE);
        }

        // Check boolean values
        if (checkTempRangeBarInput && checkWindSpeedRangeBarInput && checkHumidityRangeBarInput && checkCheckBoxesInput && checkZipCodeInput) {
            setupPreferences();
            returnToMainActivity();
        }
        else {
            System.out.println("ERROR!!!");
        }
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

    }

    // Change min and max integer values when the RangeBar pins are moved
    @Override
    public void onTouchEnded(RangeBar rangeBar) {
        switch (rangeBar.getId()) {
            case R.id.SearchrangeSeekbarHumidity:
                maxHumidity = Integer.parseInt(rangeBar.getRightPinValue());
                minHumidity = Integer.parseInt(rangeBar.getLeftPinValue());
                break;
            case R.id.SearchrangeSeekbarTemperature:
                maxTemp = Integer.parseInt(rangeBar.getRightPinValue());
                minTemp = Integer.parseInt(rangeBar.getLeftPinValue());
                break;
            case R.id.SearchrangeSeekbarWindSpeed:
                maxWindSpeed = Integer.parseInt(rangeBar.getRightPinValue());
                minWindSpeed = Integer.parseInt(rangeBar.getLeftPinValue());
        }
    }

    @Override
    public void onTouchStarted(RangeBar rangeBar) {

    }
}
