package com.practicum.playlistmaker.newplaylist.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.PlaylistsInteractor
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageInteractor
import com.practicum.playlistmaker.medialib.domain.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class NewPlayListViewModel(
    val playlistsInteractor: PlaylistsInteractor,
    val fileInteractor: FileStorageInteractor,
    val context: Context,
) : ViewModel() {
    var playListName: String = ""
    var playListDescription: String = ""

    var uri: Uri = Uri.EMPTY

    fun isDescriptionEmpty(): Boolean {
    return playListName.isEmpty() and playListDescription.isEmpty()
    }

    open fun getImageUri(pathToArtwork:String): Uri {
        return fileInteractor.getFile(pathToArtwork)
    }

    open fun createPlayList(playlistName: String, playlistDescription:String, pathToArtwork:String) {
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
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        if(!filePath.exists()) {filePath.mkdirs()}
        val file = File(filePath, picName)
        Log.d("Image", "File-"+file.toString())
        Log.d("Image", "File uri-"+uri.toString())
        val inputStream = context.contentResolver.openInputStream(uri)
        Log.d("Image", "inputStream-"+inputStream.toString())
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return picName
    }
}