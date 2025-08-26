package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory (val sharedPreferences: SharedPreferences) {
    private val gson=Gson()
    fun writeInMemory(scroll:MutableList<Track>) {
        val json = gson.toJson(scroll)
        sharedPreferences.edit().putString(TRACKS_KEY, json).apply()
    }

    fun readFromMemory(): MutableList<Track> {
        val json = sharedPreferences.getString(TRACKS_KEY, null)

        return if (json == null) {
            mutableListOf()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toMutableList()
        }
    }

    fun clearMemory () {
        sharedPreferences.edit().clear().apply()

    }
}