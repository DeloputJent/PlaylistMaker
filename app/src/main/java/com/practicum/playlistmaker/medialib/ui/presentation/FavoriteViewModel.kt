package com.practicum.playlistmaker.medialib.ui.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.FavoriteInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteInteractor: FavoriteInteractor,
): ViewModel() {
    private val stateLiveData = MutableLiveData<FavoriteTrackState>()

    fun observeState(): LiveData<FavoriteTrackState> = stateLiveData

    fun fillData() {
        renderState(FavoriteTrackState.Loading)
        viewModelScope.launch {
            favoriteInteractor
                .getFavorites()
                .collect{favoriteTracks->
                    processResult(favoriteTracks)
                }
        }
    }

    private fun processResult(favoriteTracks: List<Track>) {
        if (favoriteTracks.isEmpty()) {
            renderState(FavoriteTrackState.NothingFound)
        } else {
            renderState(FavoriteTrackState.Content(favoriteTracks))
        }
    }

    private fun renderState(state: FavoriteTrackState) {
        stateLiveData.postValue(state)
    }
}