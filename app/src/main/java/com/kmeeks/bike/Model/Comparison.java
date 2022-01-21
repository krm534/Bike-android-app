package com.kmeeks.bike.Model;

public class Comparison {
    private int mCounter;

    // Compare preferred and actual temperature values
    public void compareTemperature(float maxTemp, float minTemp, int preferredMaxTemp, int preferredMinTemp) {
        float averageTemp = (maxTemp + minTemp) / 2;
        if (averageTemp >= preferredMinTemp && averageTemp <= preferredMaxTemp) {
            mCounter++;
        }
    }

    // Compare preferred and actual wind speed
    public void compareWindSpeed(float windSpeed, int preferredMinWindSpeed, int preferredMaxWindSpeed) {
        if (windSpeed >= preferredMinWindSpeed && windSpeed <= preferredMaxWindSpeed) {
            mCounter++;
        }
    }

    // Compare preferred and actual humidity
    public void compareHumidity(int humidity, int preferredMinHumidity, int preferredMaxHumidity) {
        if (humidity >= preferredMinHumidity && humidity <= preferredMaxHumidity) {
            mCounter++;
        }
    }

    // Compare preferred and actual type of weather
    public void compareWeatherType(Boolean weatherType) {
        if (weatherType) {
            mCounter++;
        }
    }

    public int getCurrentCounter() {
        return mCounter;
    }

    // Calculate and return score
    public int getScore() {
        float score = (float) mCounter / 4;
        mCounter = 0;

        if (score <= 0.25) {
            return 1;
        } else if (score == 0.50) {
            return 2;
        } else if (score == 0.75) {
            return 3;
        } else {
            return 4;
        }
    }
}
