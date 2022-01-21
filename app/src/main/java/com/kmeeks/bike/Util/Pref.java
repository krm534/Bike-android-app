package com.kmeeks.bike.Util;

import android.app.Activity;
import android.content.SharedPreferences;

import static com.kmeeks.bike.Util.Constants.CLEAR;
import static com.kmeeks.bike.Util.Constants.CLOUDS;
import static com.kmeeks.bike.Util.Constants.DRIZZLE;
import static com.kmeeks.bike.Util.Constants.MAX_HUMIDITY;
import static com.kmeeks.bike.Util.Constants.MAX_TEMP;
import static com.kmeeks.bike.Util.Constants.MAX_WIND_SPEED;
import static com.kmeeks.bike.Util.Constants.MIN_HUMIDITY;
import static com.kmeeks.bike.Util.Constants.MIN_TEMP;
import static com.kmeeks.bike.Util.Constants.MIN_WIND_SPEED;
import static com.kmeeks.bike.Util.Constants.RAIN;
import static com.kmeeks.bike.Util.Constants.SNOW;
import static com.kmeeks.bike.Util.Constants.SPEC;
import static com.kmeeks.bike.Util.Constants.WEATHER_DATA;
import static com.kmeeks.bike.Util.Constants.ZIP_CODE;
import static com.kmeeks.bike.Util.Constants.ZIP_CODE_CHECKED_FLAG;

public class Pref {
    private final SharedPreferences mSharedPreferences;

    public Pref(Activity activity) {
        mSharedPreferences = activity.getSharedPreferences(SPEC, Activity.MODE_PRIVATE);
    }

    public void saveMinTemperature(int minTemperature) {
        mSharedPreferences.edit().putInt(MIN_TEMP, minTemperature).apply();
    }

    public int getMinTemperature() {
        return mSharedPreferences.getInt(MIN_TEMP, 0);
    }

    public void saveMaxTemperature(int maxTemperature) {
        mSharedPreferences.edit().putInt(MAX_TEMP, maxTemperature).apply();
    }

    public int getMaxTemperature() {
        return mSharedPreferences.getInt(MAX_TEMP, 0);
    }

    public void saveMinWindSpeed(int minWindSpeed) {
        mSharedPreferences.edit().putInt(MIN_WIND_SPEED, minWindSpeed).apply();
    }

    public int getMinWindSpeed() {
        return mSharedPreferences.getInt(MIN_WIND_SPEED, 0);
    }

    public void saveMaxWindSpeed(int maxWindSpeed) {
        mSharedPreferences.edit().putInt(MAX_WIND_SPEED, maxWindSpeed).apply();
    }

    public int getMaxWindSpeed() {
        return mSharedPreferences.getInt(MAX_WIND_SPEED, 0);
    }

    public void saveMinHumidity(int minHumidity) {
        mSharedPreferences.edit().putInt(MIN_HUMIDITY, minHumidity).apply();
    }

    public int getMinHumidity() {
        return mSharedPreferences.getInt(MIN_HUMIDITY, 0);
    }

    public void saveMaxHumidity(int maxHumidity) {
        mSharedPreferences.edit().putInt(MAX_HUMIDITY, maxHumidity).apply();
    }

    public int getMaxHumidity() {
        return mSharedPreferences.getInt(MAX_HUMIDITY, 0);
    }

    public void saveSnow(boolean snow) {
        mSharedPreferences.edit().putBoolean(SNOW, snow).apply();
    }

    public boolean getSnow() {
        return mSharedPreferences.getBoolean(SNOW, false);
    }

    public void saveRain(boolean rain) {
        mSharedPreferences.edit().putBoolean(RAIN, rain).apply();
    }

    public boolean getRain() {
        return mSharedPreferences.getBoolean(RAIN, false);
    }

    public void saveClear(boolean clear) {
        mSharedPreferences.edit().putBoolean(CLEAR, clear).apply();
    }

    public boolean getClear() {
        return mSharedPreferences.getBoolean(CLEAR, false);
    }

    public void saveClouds(boolean clouds) {
        mSharedPreferences.edit().putBoolean(CLOUDS, clouds).apply();
    }

    public boolean getClouds() {
        return mSharedPreferences.getBoolean(CLOUDS, false);
    }

    public void saveDrizzle(boolean drizzle) {
        mSharedPreferences.edit().putBoolean(DRIZZLE, drizzle).apply();
    }

    public boolean getDrizzle() {
        return mSharedPreferences.getBoolean(DRIZZLE, false);
    }

    public void saveZipCode(int zipCode) {
        mSharedPreferences.edit().putInt(ZIP_CODE, zipCode).apply();
    }

    public int getZipCode() {
        return mSharedPreferences.getInt(ZIP_CODE, 0);
    }

    public void saveWeatherData(String weatherData) {
        mSharedPreferences.edit().putString(WEATHER_DATA, weatherData).apply();
    }

    public String getWeatherData() {
        return mSharedPreferences.getString(WEATHER_DATA, "");
    }

    public void saveCheckedZipCode(boolean check) {
        mSharedPreferences.edit().putBoolean(ZIP_CODE_CHECKED_FLAG, check).apply();
    }

    public boolean getCheckedZipCode() {
        return mSharedPreferences.getBoolean(ZIP_CODE_CHECKED_FLAG, false);
    }
}
