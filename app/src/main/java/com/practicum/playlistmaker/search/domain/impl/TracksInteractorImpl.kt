package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor

class TracksInteractorImpl(private val repository: TrackRepository): TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t= Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }
}