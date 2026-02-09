package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Track>>
    fun addToFavorite(track: Track)
    fun deleteFromFavorites(track: Track)
}