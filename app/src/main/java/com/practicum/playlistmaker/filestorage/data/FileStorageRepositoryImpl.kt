package com.practicum.playlistmaker.filestorage.data


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.filestorage.domain.api.FileStorageRepository
import java.io.File
import java.io.FileOutputStream

class FileStorageRepositoryImpl(val context: Context): FileStorageRepository {

    override fun getFile(pathToArtwork: String): Uri {
        val filePath = File(
            context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album"
        )
        val file = File(filePath, pathToArtwork)
        return file.toUri()
    }

    override fun saveToStorage(uri: Uri, playlistName: String): String {
        val picName=playlistName + "_Artwork" + ".jpg"
        val filePath = File(context
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES), "artwork_album")
        if(!filePath.exists()) {filePath.mkdirs()}
        val file = File(filePath, picName)
        if (file.exists()) {
            file.delete()
        }
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return picName
    }
}