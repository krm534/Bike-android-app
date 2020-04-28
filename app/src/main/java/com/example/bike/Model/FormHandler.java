package com.example.bike.Model;

import android.app.Activity;
import android.util.Range;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.example.bike.R;
import com.example.bike.Util.Pref;

public class FormHandler implements RangeBar.OnRangeBarChangeListener {
    private RangeBar humidityRangebar, temperatureRangebar, windSpeedRangebar;
    private TextView tempError, windSpeedError, humidityError, conditionsError, zipCodeError;
    private CheckBox snowCheckBox, rainCheckBox, clearCheckBox, cloudsCheckBox, drizzleCheckBox;
    private int maxTemp, minTemp, maxHumidity, minHumidity, maxWindSpeed, minWindSpeed;
    private boolean checkTempRangeBarInput, checkWindSpeedRangeBarInput, checkHumidityRangeBarInput, checkCheckBoxesInput, checkZipCodeInput;
    private Pref pref;
    private Activity activity;
    private EditText zipCodeEditText;

    public FormHandler(Pref pref, Activity activity) {
        this.pref = pref;
        this.activity = activity;

        temperatureRangebar = activity.findViewById(R.id.SearchrangeSeekbarTemperature);
        windSpeedRangebar = activity.findViewById(R.id.SearchrangeSeekbarWindSpeed);
        humidityRangebar = activity.findViewById(R.id.SearchrangeSeekbarHumidity);

        temperatureRangebar.setOnRangeBarChangeListener(this);
        windSpeedRangebar.setOnRangeBarChangeListener(this);
        humidityRangebar.setOnRangeBarChangeListener(this);

        tempError = activity.findViewById(R.id.preferred_temperature_error_textview);
        windSpeedError = activity.findViewById(R.id.preferred_wind_speed_error_textview);
        humidityError = activity.findViewById(R.id.preferred_humidity_error_textview);
        conditionsError = activity.findViewById(R.id.preferred_conditions_error_textview);
        zipCodeError = activity.findViewById(R.id.preferred_zipcode_error_textview);

        snowCheckBox = activity.findViewById(R.id.checkbox_snow);
        rainCheckBox = activity.findViewById(R.id.checkbox_rain);
        clearCheckBox = activity.findViewById(R.id.checkbox_clear);
        cloudsCheckBox = activity.findViewById(R.id.checkbox_clouds);
        drizzleCheckBox = activity.findViewById(R.id.checkbox_drizzle);

        zipCodeEditText = activity.findViewById(R.id.zip_code_edittext);

        maxTemp = Integer.parseInt(temperatureRangebar.getRightPinValue());
        minTemp = Integer.parseInt(temperatureRangebar.getLeftPinValue());
        maxHumidity = Integer.parseInt(humidityRangebar.getRightPinValue());
        minHumidity = Integer.parseInt(humidityRangebar.getLeftPinValue());
        maxWindSpeed = Integer.parseInt(windSpeedRangebar.getRightPinValue());
        minWindSpeed = Integer.parseInt(windSpeedRangebar.getLeftPinValue());
    }

    // Setup the form based on information in SharedPreferences
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

    // Setup SharedPreferences
    private void setupPreferences() {
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

    // Return to MainActivity Class
    private void returnToMainActivity() {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
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
