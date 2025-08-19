package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory (val sharedPreferences: SharedPreferences) {

    fun writeInMemory(scroll:ArrayList<Track>) {
        val json = Gson().toJson(scroll)
        sharedPreferences.edit().putString(TRACKS_KEY, json).apply()
    }

    fun readFromMemory(): Array<Track> {
        val json = sharedPreferences.getString(TRACKS_KEY, null)

        return if (json == null) {
            emptyArray()
        } else {
            Gson().fromJson(json, Array<Track>::class.java)
        }
    }

    fun clearMemory () {
        sharedPreferences.edit().clear().apply()

    }
}