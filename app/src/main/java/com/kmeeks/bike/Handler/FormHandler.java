package com.kmeeks.bike.Handler;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.kmeeks.bike.R;
import com.kmeeks.bike.Util.Pref;

import java.util.Locale;

public class FormHandler implements RangeBar.OnRangeBarChangeListener {
    private final RangeBar mHumidityRangebar;
    private final RangeBar mTemperatureRangebar;
    private final RangeBar mWindSpeedRangebar;
    private final TextView mTempError;
    private final TextView mWindSpeedError;
    private final TextView mHumidityError;
    private final TextView mConditionsError;
    private final TextView mZipCodeError;
    private final CheckBox mSnowCheckBox;
    private final CheckBox mRainCheckBox;
    private final CheckBox mClearCheckBox;
    private final CheckBox mCloudsCheckBox;
    private final CheckBox mDrizzleCheckBox;
    private int mMaxTemp, mMinTemp, mMaxHumidity, mMinHumidity, mMaxWindSpeed, mMinWindSpeed;
    private final Pref mPref;
    private final Activity mActivity;
    private final EditText mZipCodeEditText;

    public FormHandler(Pref pref, Activity activity) {
        mPref = pref;
        mActivity = activity;

        mTemperatureRangebar = activity.findViewById(R.id.SearchrangeSeekbarTemperature);
        mWindSpeedRangebar = activity.findViewById(R.id.SearchrangeSeekbarWindSpeed);
        mHumidityRangebar = activity.findViewById(R.id.SearchrangeSeekbarHumidity);

        mTemperatureRangebar.setOnRangeBarChangeListener(this);
        mWindSpeedRangebar.setOnRangeBarChangeListener(this);
        mHumidityRangebar.setOnRangeBarChangeListener(this);

        mTempError = activity.findViewById(R.id.preferred_temperature_error_textview);
        mWindSpeedError = activity.findViewById(R.id.preferred_wind_speed_error_textview);
        mHumidityError = activity.findViewById(R.id.preferred_humidity_error_textview);
        mConditionsError = activity.findViewById(R.id.preferred_conditions_error_textview);
        mZipCodeError = activity.findViewById(R.id.preferred_zipcode_error_textview);

        mSnowCheckBox = activity.findViewById(R.id.checkbox_snow);
        mRainCheckBox = activity.findViewById(R.id.checkbox_rain);
        mClearCheckBox = activity.findViewById(R.id.checkbox_clear);
        mCloudsCheckBox = activity.findViewById(R.id.checkbox_clouds);
        mDrizzleCheckBox = activity.findViewById(R.id.checkbox_drizzle);

        mZipCodeEditText = activity.findViewById(R.id.zip_code_edittext);

        mMaxTemp = Integer.parseInt(mTemperatureRangebar.getRightPinValue());
        mMinTemp = Integer.parseInt(mTemperatureRangebar.getLeftPinValue());
        mMaxHumidity = Integer.parseInt(mHumidityRangebar.getRightPinValue());
        mMinHumidity = Integer.parseInt(mHumidityRangebar.getLeftPinValue());
        mMaxWindSpeed = Integer.parseInt(mWindSpeedRangebar.getRightPinValue());
        mMinWindSpeed = Integer.parseInt(mWindSpeedRangebar.getLeftPinValue());
    }

    // Setup the form based on information in SharedPreferences
    public void setupForm() {
        // Check clear condition
        if (mPref.getClear()) {
            mClearCheckBox.setChecked(true);
        } else {
            mClearCheckBox.setChecked(false);
        }

        // Check clouds condition
        if (mPref.getClouds()) {
            mCloudsCheckBox.setChecked(true);
        } else {
            mCloudsCheckBox.setChecked(false);
        }

        // Check drizzle condition
        if (mPref.getDrizzle()) {
            mDrizzleCheckBox.setChecked(true);
        } else {
            mDrizzleCheckBox.setChecked(false);
        }

        // Check rain condition
        if (mPref.getRain()) {
            mRainCheckBox.setChecked(true);
        } else {
            mRainCheckBox.setChecked(false);
        }

        // Check rain condition
        if (mPref.getSnow()) {
            mSnowCheckBox.setChecked(true);
        } else {
            mSnowCheckBox.setChecked(false);
        }

        // Zip code
        mZipCodeEditText.setText(String.format(Locale.getDefault(), "%d", mPref.getZipCode()));

        // Max and Min temperature
        mTemperatureRangebar.setRangePinsByValue(mPref.getMinTemperature(), mPref.getMaxTemperature());

        // Max and Min humidity
        mHumidityRangebar.setRangePinsByValue(mPref.getMinHumidity(), mPref.getMaxHumidity());

        // Max and Min wind speed
        mWindSpeedRangebar.setRangePinsByValue(mPref.getMinWindSpeed(), mPref.getMaxWindSpeed());
    }

    // Setup SharedPreferences
    private void setupPreferences() {
        mPref.saveMinTemperature(mMinTemp);
        mPref.saveMaxTemperature(mMaxTemp);
        mPref.saveMinHumidity(mMinHumidity);
        mPref.saveMaxHumidity(mMaxHumidity);
        mPref.saveMinWindSpeed(mMinWindSpeed);
        mPref.saveMaxWindSpeed(mMaxWindSpeed);
        mPref.saveClear(mClearCheckBox.isChecked());
        mPref.saveSnow(mSnowCheckBox.isChecked());
        mPref.saveClouds(mCloudsCheckBox.isChecked());
        mPref.saveDrizzle(mDrizzleCheckBox.isChecked());
        mPref.saveRain(mRainCheckBox.isChecked());
        mPref.saveZipCode(Integer.parseInt(mZipCodeEditText.getText().toString()));
    }

    // Validate form input
    public void checkFormInput() {
        // Check Temperature Rangebar Input
        boolean checkTempRangeBarInput;
        if (mMinTemp == mMaxTemp || mMinTemp < 1 || mMinTemp > 100 || mMaxTemp < 1 || mMaxTemp > 100) {
            checkTempRangeBarInput = false;
            mTempError.setVisibility(View.VISIBLE);
        } else {
            checkTempRangeBarInput = true;
            mTempError.setVisibility(View.GONE);
        }

        // Check Wind Speed Rangebar Input
        boolean checkWindSpeedRangeBarInput;
        if (mMinWindSpeed == mMaxWindSpeed || mMinWindSpeed < 1 || mMinWindSpeed > 30 || mMaxWindSpeed < 1 || mMaxWindSpeed > 30) {
            checkWindSpeedRangeBarInput = false;
            mWindSpeedError.setVisibility(View.VISIBLE);
        } else {
            checkWindSpeedRangeBarInput = true;
            mWindSpeedError.setVisibility(View.GONE);
        }

        // Check Humidity Rangebar Input
        boolean checkHumidityRangeBarInput;
        if (mMinHumidity == mMaxHumidity || mMinHumidity < 1 || mMinHumidity > 100 || mMaxHumidity < 1 || mMaxHumidity > 100) {
            checkHumidityRangeBarInput = false;
            mHumidityError.setVisibility(View.VISIBLE);
        } else {
            checkHumidityRangeBarInput = true;
            mHumidityError.setVisibility(View.GONE);
        }

        // Check CheckBoxes Input
        boolean checkCheckBoxesInput;
        if (mSnowCheckBox.isChecked() || mRainCheckBox.isChecked() || mClearCheckBox.isChecked() || mCloudsCheckBox.isChecked() || mDrizzleCheckBox.isChecked()) {
            checkCheckBoxesInput = true;
            mConditionsError.setVisibility(View.GONE);
        } else {
            checkCheckBoxesInput = false;
            mConditionsError.setVisibility(View.VISIBLE);
        }

        // Check EditText Input
        boolean checkZipCodeInput;
        if (mZipCodeEditText.getText().toString().trim().length() > 0 && mZipCodeEditText.getText().toString().trim().matches("[0-9]*") && mZipCodeEditText.getText().toString().trim().length() < 10) {
            checkZipCodeInput = true;
            mZipCodeError.setVisibility(View.GONE);
        } else {
            checkZipCodeInput = false;
            mZipCodeError.setVisibility(View.VISIBLE);
        }

        // Check boolean values
        if (checkTempRangeBarInput && checkWindSpeedRangeBarInput && checkHumidityRangeBarInput && checkCheckBoxesInput && checkZipCodeInput) {
            setupPreferences();
            returnToMainActivity();
        }
    }

    // Return to MainActivity Class
    private void returnToMainActivity() {
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }

    @Override
    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {}

    // Change min and max integer values when the RangeBar pins are moved
    @Override
    public void onTouchEnded(RangeBar rangeBar) {
        switch (rangeBar.getId()) {
            case R.id.SearchrangeSeekbarHumidity:
                mMaxHumidity = Integer.parseInt(rangeBar.getRightPinValue());
                mMinHumidity = Integer.parseInt(rangeBar.getLeftPinValue());
                break;
            case R.id.SearchrangeSeekbarTemperature:
                mMaxTemp = Integer.parseInt(rangeBar.getRightPinValue());
                mMinTemp = Integer.parseInt(rangeBar.getLeftPinValue());
                break;
            case R.id.SearchrangeSeekbarWindSpeed:
                mMaxWindSpeed = Integer.parseInt(rangeBar.getRightPinValue());
                mMinWindSpeed = Integer.parseInt(rangeBar.getLeftPinValue());
        }
    }

    @Override
    public void onTouchStarted(RangeBar rangeBar) {}
}
