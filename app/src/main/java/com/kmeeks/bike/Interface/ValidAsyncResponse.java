package com.kmeeks.bike.Interface;

import com.kmeeks.bike.Model.Weather;

import java.util.ArrayList;

public interface ValidAsyncResponse {
    void processFinished(ArrayList<Weather> weatherArrayList);
}
