package com.practicum.playlistmaker.medialib.data

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import java.io.File

class FileStorage(private val context: Context,private val appStorageDir:String) {
    fun getFilePath():File {
        val filePath=File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appStorageDir)
        return filePath
    }

    fun getResourceId(pathToRes:String): Uri {
        val filePath = getFilePath()
        val uri = File(filePath, pathToRes).toUri()
        return uri
    }

}