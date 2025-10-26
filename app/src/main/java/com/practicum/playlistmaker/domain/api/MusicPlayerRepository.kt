package com.practicum.playlistmaker.domain.api

interface MusicPlayerRepository {
    fun preparePlayer(url: String?,onCompletion:()->Unit)
    fun startPlayer()
    fun getCurrentPlayedTime(): Int
    fun pausePlayer()
    fun releasePlayer()

    fun stateOfPlayer():Int
}