package com.practicum.playlistmaker.newplaylist.ui

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.newplaylist.domain.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI


class NewPlayListViewModel(private val playlistsInteractor: PlaylistsInteractor,
                           private val context: Context
) : ViewModel() {

    fun createPlayList(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.addNewPlaylist(playlist)
        }
    }

    fun saveImageToPrivateStorage(uri: URI) {
        val filePath = File(context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
    }



}