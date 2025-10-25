package com.practicum.playlistmaker.data.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track


class SearchHistory (private val context: Context) {

    val lookedTracks: SharedPreferences = context.getSharedPreferences(HISTORY, MODE_PRIVATE)
    private val gson = Gson()
    fun writeInMemory(scroll:MutableList<Track>) {
        val json = gson.toJson(scroll)
        lookedTracks.edit().putString(TRACKS_KEY, json).apply()
    }

    fun readFromMemory(): MutableList<Track> {
        val json = lookedTracks.getString(TRACKS_KEY, null)

        return if (json == null) {
            mutableListOf()
        } else {
            gson.fromJson(json, Array<Track>::class.java).toMutableList()
        }
    }

    fun clearMemory () {
        lookedTracks.edit().clear().apply()
    }

    companion object{
        const val HISTORY ="History_of_search"
        const val TRACKS_KEY ="Track_List"
    }
}