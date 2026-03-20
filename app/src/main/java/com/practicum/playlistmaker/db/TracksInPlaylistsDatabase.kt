package com.practicum.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.db.data.dao.TracksInPlaylistsDao
import com.practicum.playlistmaker.db.data.entity.TracksInPlaylistsEntity

@Database(version = 1, entities = [TracksInPlaylistsEntity::class])
abstract class TracksInPlaylistsDatabase: RoomDatabase()  {

    abstract fun getPlaylistsTracksInPlaylistsDao(): TracksInPlaylistsDao
}
