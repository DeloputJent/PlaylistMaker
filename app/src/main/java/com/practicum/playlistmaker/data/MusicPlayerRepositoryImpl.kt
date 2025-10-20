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

    fun getCurrentPlayedTime() = mediaPlayer.currentPosition

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    fun releasePlayer() {
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}