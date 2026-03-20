package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class PlayListViewModel(private val dbinteractor: PlaylistsInteractor): ViewModel() {

    var playListTracks: MutableList<Track> = mutableListOf()

    fun getTracksFromPlaylists() {
        viewModelScope.launch {

            }
        }


    /*private val stateLiveData = MutableLiveData<PlayListsScrollState>()

    fun observeState(): LiveData<PlayListsScrollState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            dbinteractor.getPlaylists().collect {playlists->
                processResult(playlists)
            }
        }
    }

    private fun processResult(playLists: List<Playlist>) {
        if (playLists.isEmpty()) {
            renderState(PlayListsScrollState.NoPlaylistsFound)
        } else {
            renderState(PlayListsScrollState.Content(playLists))
        }
    }


    private fun renderState (state: PlayListsScrollState) {
        stateLiveData.postValue(state)
    }*/

}