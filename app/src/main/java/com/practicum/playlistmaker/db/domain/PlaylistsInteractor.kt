package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.newplaylist.domain.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)
}