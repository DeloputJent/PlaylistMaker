package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.db.domain.PlaylistsRepository
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository): PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
       return repository.getPlaylists()
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {
        repository.addNewPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        repository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return repository.getPlaylistById(playlistId)
    }

    override suspend fun insertTrack(track: Track) {
        repository.insertTrack(track)
    }

    override fun getTracksFromPlaylist(tracksIdList: List<String>): Flow<List<Track>> {
       return repository.getTracksFromPlaylist(tracksIdList)
    }
}