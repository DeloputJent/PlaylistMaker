package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class MusicPlayerRepositoryImpl(val mediaPlayer : MediaPlayer) {

    var playerState = STATE_DEFAULT

    fun preparePlayer(url: String?,onCompletion:()->Unit) {
        if (url!=null) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                onCompletion()
            }
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    fun getCurrentPlayedTime() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}