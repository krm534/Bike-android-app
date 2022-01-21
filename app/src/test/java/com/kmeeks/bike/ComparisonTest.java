package com.kmeeks.bike;

import com.kmeeks.bike.Model.Comparison;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComparisonTest {
    private Comparison comparison;

    @Before
    public void setup() {
        comparison = new Comparison();
    }

    @Test
    public void compareTempPassingTest() {
        comparison.compareTemperature(70f, 60f, 80, 50);
        int counter = comparison.getCurrentCounter();
        assertEquals(1, counter);
    }

    @Test
    public void compareTempFailingTest() {
        comparison.compareTemperature(70f, 60f, 50, 60);
        int counter = comparison.getCurrentCounter();
        assertEquals(0, counter);
    }

    @Test
    public void compareWindSpeedPassingTest() {
        comparison.compareWindSpeed(5f, 1, 10);
        int counter = comparison.getCurrentCounter();
        assertEquals(1, counter);
    }

    @Test
    public void compareWindSpeedFailingTest() {
        comparison.compareWindSpeed(11f, 1, 10);
        int counter = comparison.getCurrentCounter();
        assertEquals(0, counter);
    }

    @Test
    public void compareHumidityPassingTest() {
        comparison.compareHumidity(30, 25, 50);
        int counter = comparison.getCurrentCounter();
        assertEquals(1, counter);
    }

    @Test
    public void compareHumidityFailingTest() {
        comparison.compareHumidity(20, 25, 50);
        int counter = comparison.getCurrentCounter();
        assertEquals(0, counter);
    }

    @Test
    public void compareWeatherTypePassingTest() {
        comparison.compareWeatherType(true);
        int counter = comparison.getCurrentCounter();
        assertEquals(1, counter);
    }


    @Test
    public void compareWeatherTypeFailingTest() {
        comparison.compareWeatherType(false);
        int counter = comparison.getCurrentCounter();
        assertEquals(0, counter);
    }

    @Test
    public void comparisonScoreTest() {
        comparison.compareTemperature(70f, 60f, 80, 50);
        comparison.compareWindSpeed(5f, 1, 10);
        comparison.compareHumidity(30, 25, 50);
        comparison.compareWeatherType(false);
        int score = comparison.getScore();
        assertEquals(3, score);
    }
}