package com.practicum.playlistmaker.db.data.impl

import com.practicum.playlistmaker.db.TrackDatabase
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.data.dao.TrackDao
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val converter: TrackDbConvertor
): FavoriteRepository {

    override fun getFavorites(): Flow<List<Track>> = flow {
        val favoriteTracks = trackDatabase.getTrackDao().getFavorites()
        emit(convertFromTrackEntity(favoriteTracks).reversed())
    }

    override suspend fun addToFavorite(track: Track) {
        trackDatabase.getTrackDao().insertFavorite(convertFromTrack(track))
    }

    override suspend fun getFavoritesId(): List<String> {
        return trackBase.getTrackDao().getTracksId()
    }

    override suspend fun deleteFromFavorites(track: Track) {
        trackDatabase.getTrackDao().dropOut(convertFromTrack(track))
    }

    override suspend fun isInFavorites(track: Track): Boolean {
        val favoriteIds = trackDatabase.getTrackDao().getTracksId()
           return favoriteIds.contains(track.trackId)
    }


    private fun convertFromTrackEntity(favoriteTracks: List<TrackEntity>): List<Track> {
        return favoriteTracks.map { track -> converter.map(track) }
    }

    private fun convertFromTrack(favoriteTrack: Track): TrackEntity {
        return converter.map(favoriteTrack)
    }
}