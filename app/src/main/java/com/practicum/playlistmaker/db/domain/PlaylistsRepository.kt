package com.practicum.playlistmaker.db.domain

import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int) : Playlist
    suspend fun addNewPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun insertTrack(track: Track)
    fun getTracksFromPlaylist(tracksIdList: List<String>):Flow<List<Track>>
}