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
    private final SharedPreferences sharedPreferences;

    public Pref(Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences(SPEC, Activity.MODE_PRIVATE);
    }

    public void saveMinTemperature(int minTemperature) {
        sharedPreferences.edit().putInt(MIN_TEMP, minTemperature).apply();
    }

    public int getMinTemperature() {
        return sharedPreferences.getInt(MIN_TEMP, 0);
    }

    public void saveMaxTemperature(int maxTemperature) {
        sharedPreferences.edit().putInt(MAX_TEMP, maxTemperature).apply();
    }

    public int getMaxTemperature() {
        return sharedPreferences.getInt(MAX_TEMP, 0);
    }

    public void saveMinWindSpeed(int minWindSpeed) {
        sharedPreferences.edit().putInt(MIN_WIND_SPEED, minWindSpeed).apply();
    }

    public int getMinWindSpeed() {
        return sharedPreferences.getInt(MIN_WIND_SPEED, 0);
    }

    public void saveMaxWindSpeed(int maxWindSpeed) {
        sharedPreferences.edit().putInt(MAX_WIND_SPEED, maxWindSpeed).apply();
    }

    public int getMaxWindSpeed() {
        return sharedPreferences.getInt(MAX_WIND_SPEED, 0);
    }

    public void saveMinHumidity(int minHumidity) {
        sharedPreferences.edit().putInt(MIN_HUMIDITY, minHumidity).apply();
    }

    public int getMinHumidity() {
        return sharedPreferences.getInt(MIN_HUMIDITY, 0);
    }

    public void saveMaxHumidity(int maxHumidity) {
        sharedPreferences.edit().putInt(MAX_HUMIDITY, maxHumidity).apply();
    }

    public int getMaxHumidity() {
        return sharedPreferences.getInt(MAX_HUMIDITY, 0);
    }

    public void saveSnow(boolean snow) {
        sharedPreferences.edit().putBoolean(SNOW, snow).apply();
    }

    public boolean getSnow() {
        return sharedPreferences.getBoolean(SNOW, false);
    }

    public void saveRain(boolean rain) {
        sharedPreferences.edit().putBoolean(RAIN, rain).apply();
    }

    public boolean getRain() {
        return sharedPreferences.getBoolean(RAIN, false);
    }

    public void saveClear(boolean clear) {
        sharedPreferences.edit().putBoolean(CLEAR, clear).apply();
    }

    public boolean getClear() {
        return sharedPreferences.getBoolean(CLEAR, false);
    }

    public void saveClouds(boolean clouds) {
        sharedPreferences.edit().putBoolean(CLOUDS, clouds).apply();
    }

    public boolean getClouds() {
        return sharedPreferences.getBoolean(CLOUDS, false);
    }

    public void saveDrizzle(boolean drizzle) {
        sharedPreferences.edit().putBoolean(DRIZZLE, drizzle).apply();
    }

    public boolean getDrizzle() {
        return sharedPreferences.getBoolean(DRIZZLE, false);
    }

    public void saveZipCode(int zipCode) {
        sharedPreferences.edit().putInt(ZIP_CODE, zipCode).apply();
    }

    public int getZipCode() {
        return sharedPreferences.getInt(ZIP_CODE, 0);
    }

    public void saveWeatherData(String weatherData) {
        sharedPreferences.edit().putString(WEATHER_DATA, weatherData).apply();
    }

    public String getWeatherData() {
        return sharedPreferences.getString(WEATHER_DATA, "");
    }

    public void saveCheckedZipCode(boolean check) {
        sharedPreferences.edit().putBoolean(ZIP_CODE_CHECKED_FLAG, check).apply();
    }

    public boolean getCheckedZipCode() {
        return sharedPreferences.getBoolean(ZIP_CODE_CHECKED_FLAG, false);
    }
}
