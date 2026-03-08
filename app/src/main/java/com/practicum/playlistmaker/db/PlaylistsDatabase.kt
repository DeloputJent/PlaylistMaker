package com.practicum.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.db.data.dao.PlaylistDao
import com.practicum.playlistmaker.db.data.entity.PlayListEntity

@Database(version = 1, entities = [PlayListEntity::class])
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun getPlaylistsDao(): PlaylistDao
}
