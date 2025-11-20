package com.practicum.playlistmaker.sharing.data.storage

import android.content.Context
import com.practicum.playlistmaker.sharing.data.StorageClient
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

}