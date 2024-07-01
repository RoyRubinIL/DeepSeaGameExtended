package com.example.deepseagame.Views;

import android.app.Application;

import com.example.deepseagame.Utilities.MSP;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MSP.init(this);
    }

}
