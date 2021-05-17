package com.example.hanh_music_31_10.application;

import com.firebase.client.Firebase;

public class MusicApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
