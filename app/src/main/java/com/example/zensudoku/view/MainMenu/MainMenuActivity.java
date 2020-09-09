package com.example.zensudoku.view.MainMenu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.zensudoku.*;
import com.example.zensudoku.R;
import com.example.zensudoku.view.SudokuActivity;

public class MainMenuActivity extends AppCompatActivity {

    MusicPlayer musicPlayer;
    MediaPlayer popNoise;
    String difficulty;


    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //musicPlayer.musicChecker();
        }
    };

    public void popPlay() {

        SharedPreferences sb = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sb.getBoolean("sfx", true)) {
            popNoise.start();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //Load ads


        popNoise = MediaPlayer.create(getApplicationContext(), R.raw.pop1);
        musicPlayer = new MusicPlayer(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        setContentView(R.layout.content_main);
    }


    @Override
    protected void onStart() {
        super.onStart();
        difficulty = "easy";
        Activity activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayer.musicChecker();
        System.out.println("resumed");

    }


    @Override
    protected void onStop() {
        super.onStop();
        musicPlayer.stop();
    }




}