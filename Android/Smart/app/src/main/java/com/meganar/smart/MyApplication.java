package com.meganar.smart;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;
    private static int mPId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mPId = android.os.Process.myPid();
    }

    public static Context context() {
        return context;
    }

    public static int getmPId() {
        return mPId;
    }
}

