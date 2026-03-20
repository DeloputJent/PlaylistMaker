package com.practicum.playlistmaker.medialib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.medialib.ui.presentation.PlayListsScrollState
import kotlinx.coroutines.launch

class PlayListsViewModel(private val dbinteractor: PlaylistsInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlayListsScrollState>()

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
    }

}