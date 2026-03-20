package com.practicum.playlistmaker.db.data.impl

import com.practicum.playlistmaker.db.PlaylistsDatabase
import com.practicum.playlistmaker.db.TracksInPlaylistsDatabase
import com.practicum.playlistmaker.db.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.entity.PlayListEntity
import com.practicum.playlistmaker.db.data.entity.TracksInPlaylistsEntity
import com.practicum.playlistmaker.db.domain.PlaylistsRepository
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val playlistBase: PlaylistsDatabase,
    private val tracksInPlaylistsBase: TracksInPlaylistsDatabase,
    private val converter: PlaylistDbConverter,

): PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistBase.getPlaylistsDao().getPlayLists()
        emit(convertFromEntity(playlists).reversed())
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistBase.getPlaylistsDao().addPlayList(convertFromPlayList(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistBase.getPlaylistsDao().updatePlayList(convertFromPlayList(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistBase.getPlaylistsDao().deletePlaylist(convertFromPlayList(playlist))
    }

    override suspend fun insertTrack(track: Track) {
        tracksInPlaylistsBase
            .getPlaylistsTracksInPlaylistsDao()
            .insertTrack(convertFromTrack(track))
    }

    private fun convertFromEntity(playlists: List<PlayListEntity>): List<Playlist> {
        return playlists.map { playlist -> converter.map(playlist) }
    }

    private fun convertFromPlayList(playlist: Playlist): PlayListEntity {
        return converter.map(playlist)
    }

    private fun convertFromTracksInPlaylistsEntity(tracks: List<TracksInPlaylistsEntity>): List<Track> {
        return tracks.map { track -> converter.mapTrack(track) }
    }

    private fun convertFromTrack(track: Track): TracksInPlaylistsEntity {
        return converter.mapTrack(track)
    }
}