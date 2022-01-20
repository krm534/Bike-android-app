package com.example.bike.Interface;

import com.example.bike.Model.Weather;

import java.util.ArrayList;

public interface ValidAsyncResponse {
    void processFinished(ArrayList<Weather> weatherArrayList);
}
