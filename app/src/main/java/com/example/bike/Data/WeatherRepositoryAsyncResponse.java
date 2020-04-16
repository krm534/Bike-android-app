package com.example.bike.Data;

import com.example.bike.Model.Weather;

import java.util.ArrayList;

public interface WeatherRepositoryAsyncResponse {
    void processFinished(ArrayList<Weather> weatherArrayList);
}
