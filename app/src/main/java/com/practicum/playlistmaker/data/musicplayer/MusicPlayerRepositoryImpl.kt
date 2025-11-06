package com.practicum.playlistmaker.data.musicplayer

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MusicPlayerRepository


class MusicPlayerRepositoryImpl  (val mediaPlayer : MediaPlayer): MusicPlayerRepository {

    private var playerState = STATE_DEFAULT

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

    override fun stateOfPlayer():Int {
        return playerState
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}