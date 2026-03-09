package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class NewPlayListViewModel(private val playlistsInteractor: PlaylistsInteractor,
                           private val context: Context,
) : ViewModel() {

    fun createPlayList(playlistName: String, playlistDescription:String, pathToArtwork:String) {
        viewModelScope.launch {
            val playlist = Playlist(
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                pathToArtwork = pathToArtwork,
            )
            playlistsInteractor.addNewPlaylist(playlist)
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        val picName=playlistName + "_Artwork" + ".jpg"
        val filePath = File(context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        if(!filePath.exists()) {filePath.mkdirs()}
        val file = File(filePath, picName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return picName
    }
}