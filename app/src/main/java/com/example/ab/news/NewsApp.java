package com.example.ab.news;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by ab on 1/31/15.
 */
public class NewsApp extends Application {
    private static NewsApp sInstance;

    public static NewsApp getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        //Firebase.setAndroidContext(this);
    }
}
