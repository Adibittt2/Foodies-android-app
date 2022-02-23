package com.aditya_verma.foodies_business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;

    @Override

    public void onReceive(Context context, Intent intent) {
        mp=MediaPlayer.create(context, R.raw.alert_tone);
        mp.start();
     //   Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

    }
}