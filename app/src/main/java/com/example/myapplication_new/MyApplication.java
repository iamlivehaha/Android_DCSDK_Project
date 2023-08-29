package com.example.myapplication_new;


import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.quicksdk.QuickSdkApplication;

import java.util.HashMap;

public class MyApplication extends QuickSdkApplication {

    private static MyApplication mApp;

    //global variable
    public HashMap<String,String> infomap = new HashMap<>();

    public static MyApplication getInstance()
    {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lifecycle", "MyApplication onCreate: ");
    }

    //no use: never be called on real phone
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d("lifecycle", "MyApplication onTerminate: ");
    }

    //好的应用程序一般会在这个方法里面释放一些不必要的资源来应付当后台程序已经终止，
    //前台应用程序内存还不够时的情况。
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d("lifecycle", "MyApplication onLowMemory: ");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d("lifecycle", "MyApplication onTrimMemory: ");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("lifecycle", "MyApplication onConfigurationChanged: ");
    }
}
