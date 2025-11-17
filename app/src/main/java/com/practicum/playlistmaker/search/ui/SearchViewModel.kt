package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.SearchTrackState
import com.practicum.playlistmaker.search.domain.Track

class SearchViewModel(private val context: Context): ViewModel() {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY]) as Application
                SearchViewModel(app)
            }
        }
    }

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val stateLiveData = MutableLiveData<SearchTrackState>()
    val historyOfSearch = Creator.getHistoryOfSearch(context)
    var historyList = mutableListOf<Track>()
    private var latestSearchSong: String? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    private val trackList:MutableList<Track> = mutableListOf()

    fun readFromMemory(): MutableList<Track> {
        historyList = historyOfSearch.readFromMemory()
        return historyList
    }

    fun writeInMemory() {
        historyOfSearch.writeInMemory(historyList)
    }

    fun clearHistory() {
        historyOfSearch.clearMemory()
    }

    fun getHistoryOfSearch() : MutableList<Track> {
        return historyList
    }

    fun observeState(): LiveData<SearchTrackState> = stateLiveData

    fun searchDebounce(changedText: String) {
        if (latestSearchSong == changedText) {
            return
        }
        this.latestSearchSong = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchThisTrack(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun addToHistoryList(track: Track) {
        historyList.removeIf { it.trackId == track.trackId }
        if (historyList.size == 10) historyList.removeAt(9)
        historyList.add(0, track)
    }

    fun searchThisTrack(songName:String) {
        if(songName.isNotEmpty()) {
            renderState(SearchTrackState.Loading)
            tracksInteractor.searchTracks(songName, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
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
    } //fun searchThisTrack()

    private fun renderState(state: SearchTrackState) {
        stateLiveData.postValue(state)
    }

    fun showHistory() {
        renderState(SearchTrackState.ShowHistory(historyList))
    }

    override fun onCleared() {
        super.onCleared()
        writeInMemory()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}