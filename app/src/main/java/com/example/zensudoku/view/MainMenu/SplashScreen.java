package com.example.zensudoku.view.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zensudoku.*;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this,
                        MainMenuActivity.class);
                startActivity(i);
                SplashScreen.this.finish();
            }
        }, 3000);
    }
    @Override
    public void finish() {
        super.finish();
    }
}