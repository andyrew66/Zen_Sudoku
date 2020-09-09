package com.example.zensudoku;

import android.os.Handler;

public class Timer implements Runnable {
    private final Handler handler = new Handler();
    String timer;
    //private final TextView textView;
    private volatile long startTime;
    private volatile long elapsedTime;


    public Timer() {


    }

    @Override
    public void run() {
        long millis = System.currentTimeMillis() - startTime;

        int seconds = (int) millis / 1000;
        int minutes = seconds / 60;

        timer = (String.format("%d:%02d", minutes, seconds));
        if (elapsedTime == -1) {
            handler.postDelayed(this, 500);
        }

    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = -1;
        handler.post(this);
    }

    public void stop() {
        this.elapsedTime = System.currentTimeMillis() - startTime;
        handler.removeCallbacks(this);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getTimer() {
        return timer;
    }
}
