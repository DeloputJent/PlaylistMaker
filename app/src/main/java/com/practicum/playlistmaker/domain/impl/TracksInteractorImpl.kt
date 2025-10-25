package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor

class TracksInteractorImpl(private val repository: TrackRepository): TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t= Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}