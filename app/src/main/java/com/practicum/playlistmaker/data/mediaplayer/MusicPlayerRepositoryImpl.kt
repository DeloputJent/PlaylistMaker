package com.practicum.playlistmaker.data.mediaplayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MediaPlayerInterface


class MusicPlayerRepositoryImpl  (val mediaPlayer : MediaPlayer): MediaPlayerInterface {

    var playerState = STATE_DEFAULT

     override fun preparePlayer(url: String?, onCompletion:()->Unit) {
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

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun getCurrentPlayedTime() = mediaPlayer.currentPosition

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
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