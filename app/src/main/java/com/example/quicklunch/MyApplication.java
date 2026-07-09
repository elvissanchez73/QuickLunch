package com.example.quicklunch;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        // Optional: Enable disk persistence for Firebase Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
