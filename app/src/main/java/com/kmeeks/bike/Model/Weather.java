package com.kmeeks.bike.Model;

public class Weather {
    private float maxTemperature, minTemperature, windSpeed;
    private int humidity, date;
    private String typeOfWeather, location;

    public void setLocation(String city) {
        this.location = city;
    }

    public String getLocation() {
        return location;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature, boolean isRepeat) {
        // Convert to Fahrenheit from Kelvin
        if (!isRepeat) {
            this.maxTemperature = (maxTemperature - 273.15f) * 1.8f + 32;
        } else {
            this.maxTemperature = maxTemperature;
        }
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature, boolean isRepeat) {
        // Convert to Fahrenheit from Kelvin
        if (!isRepeat) {
            this.minTemperature = (minTemperature - 273.15f) * 1.8f + 32;
        } else {
            this.minTemperature = minTemperature;
        }
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed, boolean isRepeat) {
        if (!isRepeat) {
            // Convert from m/s to miles/hour
            this.windSpeed = windSpeed * 2.237f;
        } else {
            this.windSpeed = windSpeed;
        }
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getTypeOfWeather() {
        return typeOfWeather;
    }

    public void setTypeOfWeather(String typeOfWeather) {
        this.typeOfWeather = typeOfWeather;
    }
}
