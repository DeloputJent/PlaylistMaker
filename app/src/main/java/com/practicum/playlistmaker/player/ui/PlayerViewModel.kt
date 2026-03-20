package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
import android.util.Log
import androidx.core.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.db.domain.FavoriteInteractor
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val track: Track,
                      private val mediaPlayer:MediaPlayer,
                      private val favoriteInteractor: FavoriteInteractor,
                      private val playlistsInteractor: PlaylistsInteractor,
                      private val gson : Gson) : ViewModel() {

    var isTrackFavorite: Boolean=false

    private var currentPlaylists: MutableList<Playlist> = mutableListOf()

    fun checkIsTrackFavorite() {
        viewModelScope.launch {
        isTrackFavoriteLiveData.value=favoriteInteractor.getFavoritesId().contains(track.trackId)
        }
    }

    private val playlistLiveData = MutableLiveData<MutableList<Playlist>>(
        currentPlaylists
    )

    private val playerStateLiveData = MutableLiveData<PlayerState>(
        PlayerState.Default()
    )

    private val isTrackFavoriteLiveData = MutableLiveData<Boolean>(
        isTrackFavorite
    )

    fun observeFavoriteState(): LiveData<Boolean> = isTrackFavoriteLiveData
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    fun observePlaylistLiveData(): LiveData<MutableList<Playlist>> = playlistLiveData

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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylists()
                .collect {playlists->
                    currentPlaylists=playlists.toMutableList()
                    playlistLiveData.postValue(playlists.toMutableList())
            }
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playlist)
        }
    }

    fun addTrackToBase(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.insertTrack(track)
        }
    }

    fun getTracksID(playlist: Playlist): List<String> {
     return if (!playlist.tracksId.isNullOrEmpty())
         gson.fromJson<MutableList<String>>(
             playlist.tracksId,
             object : TypeToken<List<String>>(){}.type)
     else emptyList()
    }

    fun addTrackToPlayList (playlist: Playlist): Boolean
    {
        val trackIdsList: MutableList<String> = getTracksID(playlist).toMutableList()

        if (trackIdsList.contains(track.trackId)) return false
        else {
            trackIdsList.add(track.trackId)
            val tracksId = gson.toJson(trackIdsList.toList())
            val amount = playlist.tracksAmount+1
            val updatedPlaylist = Playlist(
                playlist.playlistID,
                playlist.playlistName,
                playlist.playlistDescription,
                playlist.pathToArtwork,
                tracksId=tracksId,
                tracksAmount = amount)
            currentPlaylists[currentPlaylists.indexOf(playlist)] = updatedPlaylist
            updatePlaylist(updatedPlaylist)
            addTrackToBase(track)
            return true
        }
    }


    companion object{
        private const val CHECK_CURRENT_POSITION = 300L
    }
}