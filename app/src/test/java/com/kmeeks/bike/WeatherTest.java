package com.kmeeks.bike;

import com.kmeeks.bike.Model.Weather;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherTest {
    private Weather weather;

    @Before
    public void setup() {
        weather = new Weather();
    }

    @Test
    public void setLocationPassingTest() {
        weather.setLocation("New York");
        assertEquals("New York", weather.getLocation());
    }

    @Test
    public void setDatePassingTest() {
        weather.setDate(1642790584);
        assertEquals(1642790584, weather.getDate());
    }

    @Test
    public void setMaxTempPassingTest() {
        // Kelvin temp is converted to Fahrenheit
        weather.setMaxTemperature(294.261f, false);
        assertEquals(70, weather.getMaxTemperature(), 1);
    }

    @Test
    public void setMinTempPassingTest() {
        // Kelvin temp is converted to Fahrenheit
        weather.setMinTemperature(288.705f, false);
        assertEquals(60, weather.getMinTemperature(), 1);
    }

    @Test
    public void setWindSpeedPassingTest() {
        // M/S wind speed is converted to MPH
        weather.setWindSpeed(15, false);
        assertEquals(33, weather.getWindSpeed(), 1);
    }

    @Test
    public void setHumidityPassingTest() {
        weather.setHumidity(10);
        assertEquals(10, weather.getHumidity(), 1);
    }

    @Test
    public void setWeatherTypeTest() {
        weather.setWeatherType("Rain");
        assertEquals("Rain", weather.getWeatherType());
    }
}
