package com.example.zensudoku;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

public class MusicPlayer {


    MediaPlayer mediaPlayer;

    Context context;


    public MusicPlayer(Context context) {
        this.context = context;
    }


    public void musicChecker() {

        SharedPreferences sb = PreferenceManager.getDefaultSharedPreferences(context);
        if (sb.getBoolean("music", true)) {
            mediaPlayer = MediaPlayer.create(context, R.raw.ambient);
            mediaPlayer.start();
        } else {
            stop();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}

