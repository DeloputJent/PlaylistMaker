package com.practicum.playlistmaker.playlist.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsScrollState
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class PlayListViewModel(private val dbinteractor: PlaylistsInteractor, private val gson : Gson): ViewModel() {

    var playListTracks: MutableList<Track> = mutableListOf()

    var currentPlaylist: Playlist = Playlist()

    private val currentPlaylistLiveData = MutableLiveData <PlayListScreen> ( )

    fun observeCurrentPlaylist(): LiveData<PlayListScreen> = currentPlaylistLiveData

    fun getPlaylistsById(playlistId:Int) {
        viewModelScope.launch {
            currentPlaylist = dbinteractor.getPlaylistById(playlistId)
            Log.d("currentPlaylist Name", currentPlaylist.playlistName)
            playListTracks = dbinteractor.getTracksFromPlaylist(getTracksID(currentPlaylist)).toMutableList()
            val playlistScreen = PlayListScreen(currentPlaylist, playListTracks)
            currentPlaylistLiveData.postValue(playlistScreen)
        }
    }

    fun getTracksID(playlist: Playlist): List<String> {
        return if (!playlist.tracksId.isNullOrEmpty())
            gson.fromJson<MutableList<String>>(
                playlist.tracksId,
                object : TypeToken<List<String>>(){}.type)
        else emptyList()
    }
}