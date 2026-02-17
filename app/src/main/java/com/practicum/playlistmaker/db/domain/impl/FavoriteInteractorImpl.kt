package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.FavoriteInteractor
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
): FavoriteInteractor {
    override fun getFavorites(): Flow<List<Track>> {
       return favoriteRepository.getFavorites()
    }

    override suspend fun addToFavorite(track: Track) {
        favoriteRepository.addToFavorite(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoriteRepository.deleteFromFavorites(track)
    }

    override suspend fun getFavoritesId(): List<String> {
        return favoriteRepository.getFavoritesId()
    }
}