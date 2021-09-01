package com.abhi.cshop.model;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Utils extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}