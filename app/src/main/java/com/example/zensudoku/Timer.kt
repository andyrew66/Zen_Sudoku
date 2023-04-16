package com.example.zensudoku

import android.os.Handler

class Timer : Runnable {
    private val handler = Handler()
    var timer: String? = null

    //private final TextView textView;
    @Volatile
    private var startTime: Long = 0

    @Volatile
    var elapsedTime: Long = 0
        private set

    override fun run() {
        val millis = System.currentTimeMillis() - startTime
        val seconds = millis.toInt() / 1000
        val minutes = seconds / 60
        timer = String.format("%d:%02d", minutes, seconds)
        if (elapsedTime == -1L) {
            handler.postDelayed(this, 500)
        }
    }

    fun start() {
        startTime = System.currentTimeMillis()
        elapsedTime = -1
        handler.post(this)
    }

    fun stop() {
        elapsedTime = System.currentTimeMillis() - startTime
        handler.removeCallbacks(this)
    }
}