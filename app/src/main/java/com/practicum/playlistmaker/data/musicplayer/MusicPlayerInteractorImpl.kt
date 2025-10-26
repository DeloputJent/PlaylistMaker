package com.practicum.playlistmaker.data.musicplayer

import com.practicum.playlistmaker.domain.api.MusicPlayerInteractor
import com.practicum.playlistmaker.domain.api.MusicPlayerRepository

class MusicPlayerInteractorImpl(private val musicPlayerRepository: MusicPlayerRepository) :
    MusicPlayerInteractor {
    override fun preparePlayer(url: String?, onCompletion: () -> Unit) {
        musicPlayerRepository.preparePlayer(url, onCompletion)
    }

    override fun startPlayer() {
        musicPlayerRepository.startPlayer()
    }

    override fun getCurrentPlayedTime() = musicPlayerRepository.getCurrentPlayedTime()

    override fun pausePlayer() {
        musicPlayerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        musicPlayerRepository.releasePlayer()
    }

    override fun stateOfPlayer() = musicPlayerRepository.stateOfPlayer()
}