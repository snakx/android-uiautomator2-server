package com.github.uiautomator2;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class Main extends Application {
    public static Context mContext;
    private static Main main;
    public static Main getInstance() {
        return main;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        main = this;
        mContext = main.getApplicationContext();
    }
}
