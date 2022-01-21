package com.kmeeks.bike;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Controller {
    private static Controller mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized Controller getInstance() {
        if (mInstance == null) {
            mInstance = new Controller();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, Context context) {
        getRequestQueue(context).add(req);
    }
}
