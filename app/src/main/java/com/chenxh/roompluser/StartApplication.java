package com.chenxh.roompluser;

import android.app.Application;
import android.content.Context;

public class StartApplication extends Application {

    public static Context getContext() {
        return context;
    }

    public final static String TAG = "RoomDB";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
