package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.util.Log
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
    private var isTrackFavorite: Boolean = track.isFavorite

    private val playerStateLiveData = MutableLiveData<PlayerState>(
        PlayerState.Default(isTrackFavorite)
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

    fun checkIsFavorite(): Boolean {
        viewModelScope.launch {
            isTrackFavorite = favoriteInteractor.isInFavorites(track)
            Log.d("isFav", isTrackFavorite.toString())
        }
        return isTrackFavorite
    }

    fun onPlayButtonClicked() {
        when(playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun onFavoriteClicked() {
        if (isTrackFavorite) {
            isTrackFavoriteLiveData.value=false
            isTrackFavorite = false
            viewModelScope.launch {
                favoriteInteractor.deleteFromFavorites(track)
            }
        } else {
            isTrackFavoriteLiveData.value=true
            track.isFavorite = true
            viewModelScope.launch {
                favoriteInteractor.addToFavorite(track)
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared(isTrackFavorite))
        }
        mediaPlayer.setOnCompletionListener {
            playerStateLiveData.postValue(PlayerState.Prepared(isTrackFavorite))
            resetTimer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(
            PlayerState.Playing(getCurrentPosition(),isTrackFavorite)
        )
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
            delay(CHECK_POSITION_DELAY)
            playerStateLiveData.postValue(PlayerState.Playing(getCurrentPosition(),
                isTrackFavorite))
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
                isTrackFavorite
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
        playerStateLiveData.postValue(PlayerState.Default(isTrackFavorite))
    }
    companion object {
        private const val CHECK_POSITION_DELAY = 300L
    }
}