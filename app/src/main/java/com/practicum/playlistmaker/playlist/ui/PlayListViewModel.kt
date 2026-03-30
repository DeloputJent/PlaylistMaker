package com.practicum.playlistmaker.playlist.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.playlist.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.playlist.domain.api.PlaylistSharingInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class PlayListViewModel(private val dbinteractor: PlaylistsInteractor,
                        private val sharingInteractor: PlaylistSharingInteractor,
                        private val fileInteractor: FileStorageInteractor,
                        private val gson : Gson): ViewModel() {

    var currentPlaylist: Playlist = Playlist()
    var currentTrackSet: List<Track> = emptyList()

    private val currentPlaylistLiveData = MutableLiveData<PlayListScreen>()

    val isPlaylistDeleted = MutableLiveData<Boolean>()

    fun getImageUri(): Uri {
        return fileInteractor.getFile(currentPlaylist.pathToArtwork)
    }

    fun observeCurrentPlaylist(): LiveData<PlayListScreen> = currentPlaylistLiveData

    fun setTrackSet(trackSet: List<Track>) {
        currentTrackSet = trackSet
    }

    fun sharePlaylist(trackAmount: String) {
        val message = makeMessage(trackAmount)
        sharingInteractor.sharePlaylist(message)
    }

    fun makeMessage(trackAmount: String): List<String> {
        val message: MutableList<String> = mutableListOf()
        message.add(currentPlaylist.playlistName)
        message.add(currentPlaylist.playlistDescription)
        message.add(trackAmount)
        var index = 0
        currentTrackSet.forEach { track ->
            message.add(getTrackInfoToString(index++, track))
        }
        val messageList = message.toList()
        return (messageList)
    }

    private fun getTrackInfoToString(trackNum: Int, track: Track): String {
        var trackInfo: String
        var num = trackNum
        num++
        trackInfo =
            num.toString() + ". " + track.artistName + " - " + track.trackName + " (" + track.trackTimeMillis + ")"
        return trackInfo
    }

    fun getPlaylistsById(playlistId: Int) {
        viewModelScope.launch {
            currentPlaylist = dbinteractor.getPlaylistById(playlistId)
            dbinteractor.getTracksFromPlaylist(getTracksID(currentPlaylist))
                .collect { playListTracks ->
                    currentPlaylistLiveData.postValue(
                        PlayListScreen(currentPlaylist, playListTracks)
                    )
                }
        }
    }

    fun getTracksID(playlist: Playlist): List<String> {
        return if (playlist.tracksId.isNotEmpty())
            gson.fromJson<MutableList<String>>(
                playlist.tracksId,
                object : TypeToken<List<String>>() {}.type
            )
        else emptyList()
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            dbinteractor.deleteTrackFromPlaylist(track.trackId, currentPlaylist.playlistID)
            dbinteractor.getTracksFromPlaylist(getTracksID(currentPlaylist))
                .collect { playListTracks ->
                    currentPlaylistLiveData.postValue(
                        PlayListScreen(currentPlaylist, playListTracks)
                    )
                }
        }
    }

    fun deleteThisPlaylist() {
        viewModelScope.launch {
            val tracksIDList = getTracksID(currentPlaylist)
            if (tracksIDList.isNotEmpty()) {
                tracksIDList.forEach { trackId ->
                dbinteractor.deleteTrackFromPlaylist(
                    trackId, currentPlaylist.playlistID
                )
                }
            }
            dbinteractor.deletePlaylist(currentPlaylist)
            isPlaylistDeleted.value = true
        }
    }
}