package com.example.bmusic.application;

import com.firebase.client.Firebase;

public class MusicApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
