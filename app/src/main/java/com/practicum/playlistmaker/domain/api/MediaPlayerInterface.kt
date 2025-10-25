package com.practicum.playlistmaker.domain.api

interface MediaPlayerInterface {
    fun preparePlayer(url: String?,onCompletion:()->Unit)
    fun startPlayer()
    fun getCurrentPlayedTime(): Int
    fun pausePlayer()
    fun releasePlayer()
}