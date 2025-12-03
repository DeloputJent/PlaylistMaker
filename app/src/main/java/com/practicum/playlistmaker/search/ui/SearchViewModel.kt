package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.SearchTrackState
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import org.koin.core.component.KoinComponent

class SearchViewModel(private val tracksInteractor:TracksInteractor,
                      private val historyOfSearch:SearchHistoryInteractor
): ViewModel(), KoinComponent {

    private val stateLiveData = MutableLiveData<SearchTrackState>()

    var historyList = mutableListOf<Track>()
    private var latestSearchSong: String = ""
    private var handler: Handler = Handler(Looper.getMainLooper())

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
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable {searchThisTrack(changedText) }

        handler.postDelayed(searchRunnable, SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY,)
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