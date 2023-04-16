package com.example.zensudoku

import android.content.Context
import android.media.MediaPlayer
import androidx.preference.PreferenceManager

class MusicPlayer(var context: Context) {
    var mediaPlayer: MediaPlayer? = null

    fun musicChecker() {
        val sb = PreferenceManager.getDefaultSharedPreferences(context)
        if (sb.getBoolean("music", true)) {
            mediaPlayer = MediaPlayer.create(context, R.raw.ambient)
            mediaPlayer?.start() // Add null check here
        } else {
            stop()
        }
    }

    fun stop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
    }
}
