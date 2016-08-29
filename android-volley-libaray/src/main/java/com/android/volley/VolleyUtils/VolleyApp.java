/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.android.volley.VolleyUtils;


import android.app.Application;


public class VolleyApp extends Application {

    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
    }

}
