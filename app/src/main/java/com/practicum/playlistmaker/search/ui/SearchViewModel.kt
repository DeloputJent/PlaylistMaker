package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
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

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val stateLiveData = MutableLiveData<SearchTrackState>()
    val historyOfSearch = Creator.getHistoryOfSearch(context)

    var historyList = mutableListOf<Track>()
    private var latestSearchSong: String = ""
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
           Log.d("ResultsText",changedText+"="+latestSearchSong)
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
                                Log.d("ResultsSong=", songName)
                                Log.d("Results=", trackList.size.toString())
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

    fun addToHistoryList(track: Track) {
        historyList.removeIf { it.trackId == track.trackId }
        if (historyList.size == 10) historyList.removeAt(9)
        historyList.add(0, track)
    }
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
}