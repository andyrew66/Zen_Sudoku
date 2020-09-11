package com.example.zensudoku.view.MainMenu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.zensudoku.MusicPlayer;
import com.example.zensudoku.R;
import com.startapp.sdk.adsbase.AutoInterstitialPreferences;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

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
        StartAppSDK.setUserConsent (this,
                "pas",
                System.currentTimeMillis(),
                false);
        StartAppSDK.init(this, "StartApp App ID", false);
        StartAppAd.setAutoInterstitialPreferences(
                new AutoInterstitialPreferences()
                        .setSecondsBetweenAds(60)
        );
        StartAppAd.enableAutoInterstitial();

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

    }


    @Override
    protected void onStop() {
        musicPlayer.stop();
        super.onStop();

    }




}