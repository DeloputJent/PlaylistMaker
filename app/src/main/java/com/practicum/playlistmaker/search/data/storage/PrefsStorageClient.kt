package com.practicum.playlistmaker.search.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.StorageClient
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    private val context: Context,
    private val gson:Gson,
    private val type: Type,
) : StorageClient<T> {
    val lookedTracks: SharedPreferences = context.getSharedPreferences(HISTORY,
        Context.MODE_PRIVATE
    )

    //private val gson = Gson()

    override fun storeData(data: T) {
        //val json = gson.toJson(data)
        lookedTracks.edit { putString(DATAKEY, gson.toJson(data, type)) }
    }

    override fun getData(): T? {
        val dataJson = lookedTracks.getString(DATAKEY, null)
        if (dataJson == null) {
            return null
        } else {
            return gson.fromJson(dataJson, type)
        }
    }

    override fun clearData () {
        lookedTracks.edit().clear().apply()
    }

    companion object{
        const val DATAKEY: String = "HISTORY"
        const val HISTORY = "History_of_search"
    }
}