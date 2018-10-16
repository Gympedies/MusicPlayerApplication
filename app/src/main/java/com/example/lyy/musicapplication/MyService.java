package com.example.lyy.musicapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private ArrayList<String> list;
    private String path="";
    private int index;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        list = new ArrayList<String>();
    }
}
