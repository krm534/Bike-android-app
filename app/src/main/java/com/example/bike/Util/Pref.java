package com.example.bike.Util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Pref {
    private SharedPreferences sharedPreferences;

    public Pref(Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences("Specifications", Activity.MODE_PRIVATE);
    }

    public void saveMinTemperature(int minTemperature) {
        sharedPreferences.edit().putInt("minTemperature", minTemperature).apply();
    }

    public int getMinTemperature() {
        return sharedPreferences.getInt("minTemperature", 0);
    }

    public void saveMaxTemperature(int maxTemperature) {
        sharedPreferences.edit().putInt("maxTemperature", maxTemperature).apply();
    }

    public int getMaxTemperature() {
        return sharedPreferences.getInt("maxTemperature", 0);
    }

    public void saveMinWindSpeed(int minWindSpeed) {
        sharedPreferences.edit().putInt("minWindSpeed", minWindSpeed).apply();
    }

    public int getMinWindSpeed() {
        return sharedPreferences.getInt("minWindSpeed", 0);
    }

    public void saveMaxWindSpeed(int maxWindSpeed) {
        sharedPreferences.edit().putInt("maxWindSpeed", maxWindSpeed).apply();
    }

    public int getMaxWindSpeed() {
        return sharedPreferences.getInt("maxWindSpeed", 0);
    }

    public void saveMinHumidity(int minHumidity) {
        sharedPreferences.edit().putInt("minHumidity", minHumidity).apply();
    }

    public int getMinHumidity() {
        return sharedPreferences.getInt("minHumidity", 0);
    }

    public void saveMaxHumidity(int maxHumidity) {
        sharedPreferences.edit().putInt("maxHumidity", maxHumidity).apply();
    }

    public int getMaxHumidity() {
        return sharedPreferences.getInt("maxHumidity", 0);
    }

    public void saveSnow(boolean snow) {
        sharedPreferences.edit().putBoolean("snow", snow).apply();
    }

    public boolean getSnow() {
        return sharedPreferences.getBoolean("snow", false);
    }

    public void saveRain(boolean rain) {
        sharedPreferences.edit().putBoolean("rain", rain).apply();
    }

    public boolean getRain() {
        return sharedPreferences.getBoolean("rain", false);
    }

    public void saveClear(boolean clear) {
        sharedPreferences.edit().putBoolean("clear", clear).apply();
    }

    public boolean getClear() {
        return sharedPreferences.getBoolean("clear", false);
    }

    public void saveClouds(boolean clouds) {
        sharedPreferences.edit().putBoolean("clouds", clouds).apply();
    }

    public boolean getClouds() {
        return sharedPreferences.getBoolean("clouds", false);
    }

    public void saveDrizzle(boolean drizzle) {
        sharedPreferences.edit().putBoolean("drizzle", drizzle).apply();
    }

    public boolean getDrizzle() {
        return sharedPreferences.getBoolean("drizzle", false);
    }

    public void saveZipCode(int zipCode) {
        sharedPreferences.edit().putInt("zipCode", zipCode).apply();
    }

    public int getZipCode() {
        return sharedPreferences.getInt("zipCode", 0);
    }

    public void saveWeatherData(String weatherData) {
        sharedPreferences.edit().putString("weatherData", weatherData).apply();
    }

    public String getWeatherData() {
        return sharedPreferences.getString("weatherData", "");
    }

    public void saveCheckedZipCode(boolean check) {
        sharedPreferences.edit().putBoolean("checkZipCode", check).apply();
    }

    public boolean getCheckedZipCode() {
        return sharedPreferences.getBoolean("checkZipCode", false);
    }
}
