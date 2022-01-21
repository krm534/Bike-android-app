package com.kmeeks.bike.Model;

public class Weather {
    private float mMaxTemperature, mMinTemperature, mWindSpeed;
    private int mHumidity, mDate;
    private String mWeatherType, mLocation;

    public void setLocation(String city) {
        mLocation = city;
    }

    public String getLocation() {
        return mLocation;
    }

    public int getDate() {
        return mDate;
    }

    public void setDate(int date) {
        mDate = date;
    }

    public float getMaxTemperature() {
        return mMaxTemperature;
    }

    public void setMaxTemperature(float maxTemperature, boolean isRepeat) {
        // Convert to Fahrenheit from Kelvin
        if (!isRepeat) {
            mMaxTemperature = (maxTemperature - 273.15f) * 1.8f + 32;
        } else {
            mMaxTemperature = maxTemperature;
        }
    }

    public float getMinTemperature() {
        return mMinTemperature;
    }

    public void setMinTemperature(float minTemperature, boolean isRepeat) {
        // Convert to Fahrenheit from Kelvin
        if (!isRepeat) {
            mMinTemperature = (minTemperature - 273.15f) * 1.8f + 32;
        } else {
            mMinTemperature = minTemperature;
        }
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(float windSpeed, boolean isRepeat) {
        if (!isRepeat) {
            // Convert from m/s to miles/hour
            mWindSpeed = windSpeed * 2.237f;
        } else {
            mWindSpeed = windSpeed;
        }
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHumidity = humidity;
    }

    public String getTypeOfWeather() {
        return mWeatherType;
    }

    public void setTypeOfWeather(String typeOfWeather) {
        mWeatherType = typeOfWeather;
    }
}
