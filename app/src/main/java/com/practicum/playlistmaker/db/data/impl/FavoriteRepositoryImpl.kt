package com.practicum.playlistmaker.db.data.impl

import com.practicum.playlistmaker.db.TrackDatabase
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoriteRepositoryImpl(
    private val trackBase: TrackDatabase,
    private val converter: TrackDbConvertor
): FavoriteRepository {

    override fun getFavorites(): Flow<List<Track>> = flow {
        val favoriteTracks = trackBase.getTrackDao().getFavorites()
        emit(convertFromTrackEntity(favoriteTracks).reversed())
    }

    override suspend fun addToFavorite(track: Track) {
        trackBase.getTrackDao().insertFavorite(convertFromTrack(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        trackBase.getTrackDao().dropOut(convertFromTrack(track))
    }

    private fun convertFromTrackEntity(favoriteTracks: List<TrackEntity>): List<Track> {
        return favoriteTracks.map { track -> converter.map(track) }
    }

    private fun convertFromTrack(favoriteTrack: Track): TrackEntity {
        return converter.map(favoriteTrack)
    }
}