package com.practicum.playlistmaker.playlist.domain.api

import android.net.Uri


interface FileStorageRepository {
    fun getFile(pathToArtwork: String): Uri
}