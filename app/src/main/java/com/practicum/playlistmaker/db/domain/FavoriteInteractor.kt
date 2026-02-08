package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun getFavorites(): Flow<List<Track>>
    suspend fun addToFavorite(track: Track)
    suspend fun deleteFromFavorites(track: Track)
}