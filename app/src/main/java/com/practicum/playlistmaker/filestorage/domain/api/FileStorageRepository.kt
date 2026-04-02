package com.practicum.playlistmaker.filestorage.domain.api

import android.net.Uri

interface FileStorageRepository {
    fun getFile(pathToArtwork: String): Uri
    fun saveToStorage(uri: Uri, picName: String): String
    fun deleteFile (picName: String):Boolean
}