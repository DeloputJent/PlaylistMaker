package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.playlist.domain.api.FileStorageRepository
import java.io.File

class FileStorageRepositoryImpl(val context: Context): FileStorageRepository {

    override fun getFile(pathToArtwork: String): Uri {
        val filePath = File(context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        val file = File(filePath, pathToArtwork)
        return file.toUri()
    }
}