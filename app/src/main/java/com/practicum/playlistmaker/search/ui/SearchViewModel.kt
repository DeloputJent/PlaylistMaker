package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.SearchTrackState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor:TracksInteractor,
                      private val historyOfSearch:SearchHistoryInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<SearchTrackState>()

    var historyList = mutableListOf<Track>()
    private var latestSearchSong: String = ""
    private var handler: Handler = Handler(Looper.getMainLooper())




    private var searchJob: Job? = null

    fun readFromMemory(): MutableList<Track> {
        historyOfSearch.getHistory(object : SearchHistoryInteractor.HistoryConsumer {
            override fun consume(searchHistory: List<Track>?) {
                historyList = (searchHistory ?: mutableListOf()).toMutableList()
            }
        })
        return historyList
    }

    fun clearHistory() {
        historyOfSearch.clearHistory()
        historyList.clear()
    }

    fun observeState(): LiveData<SearchTrackState> = stateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchSong == changedText) {
           return
        }
        this.latestSearchSong = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchThisTrack(changedText)
        }
    }

    fun searchThisTrack(songName:String) {
        if(songName.isNotEmpty()) {
            renderState(SearchTrackState.Loading)
            tracksInteractor.searchTracks(songName, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        val trackList = mutableListOf<Track>()
                        if (foundTracks != null) {
                            if (foundTracks.isNotEmpty()) {
                                trackList.clear()
                                trackList.addAll(foundTracks)
                                renderState(SearchTrackState.Content(trackList))
                            } else {
                                renderState(SearchTrackState.NothingFound)
                            }
                        } else {
                            renderState(SearchTrackState.NoNetFound)
                        }
                    }
                }
            })
        }
    }

    fun addToHistoryList(track: Track) {
        historyOfSearch.saveToHistory(track)
    }
    private fun renderState(state: SearchTrackState) {
        stateLiveData.postValue(state)
    }

    fun showHistory() {
        readFromMemory()
        renderState(SearchTrackState.ShowHistory(historyList))
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}