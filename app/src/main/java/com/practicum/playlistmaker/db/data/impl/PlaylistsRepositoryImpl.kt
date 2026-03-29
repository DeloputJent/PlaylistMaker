package com.practicum.playlistmaker.db.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private val gson: Gson
): PlaylistsRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = playlistBase.getPlaylistsDao().getPlayLists()
        emit(convertFromPlaylistEntityList(playlists).reversed())
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

    override suspend fun getPlaylistById(playlistId: Int):Playlist {
        val entity = playlistBase.getPlaylistsDao().getPlayListById(playlistId)
        return convertFromPlaylistEntity(entity)
    }

    override suspend fun insertTrack(track: Track) {
        tracksInPlaylistsBase
            .getPlaylistsTracksInPlaylistsDao()
            .insertTrack(convertFromTrack(track))
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlistId: Int) {
        val playList = getPlaylistById(playlistId)
        val trackIdsList: MutableList<String> = getTracksID(playList).toMutableList()
        if (trackIdsList.contains(trackId)) trackIdsList.remove(trackId)
        val tracksId = gson.toJson(trackIdsList.toList())
        val updatedPlayList=playList.copy(tracksId=tracksId, tracksAmount = trackIdsList.size)
        updatePlaylist(updatedPlayList)
        if (!checkIfTrackInPlaylists(trackId)) {
            val track = getTrackByID(trackId)
            if (track != null) {
            dropOutTrack(track)}
        }

    }

    override fun getTracksFromPlaylist(tracksIdList: List<String>): Flow<List<Track>> = flow {
        val allTrackList = tracksInPlaylistsBase.getPlaylistsTracksInPlaylistsDao().getTracksInPlaylists()
        val trackListWithId = allTrackList.filter { track ->
            tracksIdList.contains(track.trackId)
        }
        emit (convertFromTracksInPlaylistsEntity(trackListWithId))
    }

    private fun getTracksID(playlist: Playlist): List<String> {
                return if (!playlist.tracksId.isNullOrEmpty())
                gson.fromJson<MutableList<String>>(
                playlist.tracksId,
                object : TypeToken<List<String>>(){}.type)
            else emptyList()
    }

    private suspend fun getTrackByID(trackId: String): TracksInPlaylistsEntity =
        tracksInPlaylistsBase.getPlaylistsTracksInPlaylistsDao().getTrackById(trackId)?: TracksInPlaylistsEntity()

    private suspend fun dropOutTrack(trackInPlaylistsEntity: TracksInPlaylistsEntity) {
        tracksInPlaylistsBase.getPlaylistsTracksInPlaylistsDao().dropOut(
            trackInPlaylistsEntity
        )
    }

    private suspend fun checkIfTrackInPlaylists (trackId: String): Boolean {
        val playlists = convertFromPlaylistEntityList(playlistBase.getPlaylistsDao().getPlayLists())
        var result = false
        playlists.forEach { playlist -> run {
            if (getTracksID(playlist).contains(trackId)) result = true
            else result = false
        }        }
       return result
    }

    private fun convertFromPlaylistEntityList(playlists: List<PlayListEntity>): List<Playlist> {
        return playlists.map { playlist -> converter.map(playlist) }
    }

    private fun convertFromPlaylistEntity(playlistEntity: PlayListEntity): Playlist {
        return converter.map(playlistEntity)
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