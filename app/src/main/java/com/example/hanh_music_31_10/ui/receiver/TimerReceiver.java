package com.example.hanh_music_31_10.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hanh_music_31_10.service.MediaPlaybackService;


public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MediaPlaybackService.class);
        context.stopService(myIntent);
    }
}
