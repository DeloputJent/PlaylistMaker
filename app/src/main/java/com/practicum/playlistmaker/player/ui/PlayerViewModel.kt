package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.FavoriteInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: Track,
                      private val mediaPlayer:MediaPlayer,
                      private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    var isTrackFavorite: Boolean=false

    fun checkIsTrackFavorite() {
        viewModelScope.launch {
        isTrackFavoriteLiveData.value=favoriteInteractor.getFavoritesId().contains(track.trackId)
        }
    }

    private val playerStateLiveData = MutableLiveData<PlayerState>(
        PlayerState.Default()
    )

    private val isTrackFavoriteLiveData = MutableLiveData<Boolean>(
        isTrackFavorite
    )

    fun observeFavoriteState(): LiveData<Boolean> = isTrackFavoriteLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }

    fun onPlayButtonClicked() {
        when(playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun onFavoriteClicked() {
        isTrackFavorite = isTrackFavoriteLiveData.value?:false
        if (isTrackFavorite) {
            isTrackFavoriteLiveData.value=false
            viewModelScope.launch {
                favoriteInteractor.deleteFromFavorites(track)
            }
        } else {
            isTrackFavoriteLiveData.value=true
            isTrackFavorite = isTrackFavoriteLiveData.value?:false
            viewModelScope.launch {
                favoriteInteractor.addToFavorite(track)
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            checkIsTrackFavorite()
            playerStateLiveData.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.Prepared())
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(
            PlayerState.Playing(getCurrentPosition())
        )
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
            delay(CHECK_CURRENT_POSITION)
            playerStateLiveData.postValue(PlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(
            PlayerState.Paused(
                SimpleDateFormat("mm:ss",
                    Locale.getDefault()).format(mediaPlayer.currentPosition),
            )
        )
    }

    private fun getCurrentPosition():String =SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(mediaPlayer.currentPosition)

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Default())
    }

    companion object{
        private const val CHECK_CURRENT_POSITION = 300L
    }
}