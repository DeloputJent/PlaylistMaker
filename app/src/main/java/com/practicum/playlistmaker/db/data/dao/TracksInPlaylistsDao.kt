package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDao {

    @Insert(entity = TracksInPlaylistsEntity::class,onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(tracksInPlaylistsEntity : TracksInPlaylistsEntity)

    @Query("SELECT * FROM TracksInPlaylists_table")
    suspend fun getTracksInPlaylists(): List<TracksInPlaylistsEntity>

    @Delete (entity = TracksInPlaylistsEntity::class)
    suspend fun dropOut(tracksInPlaylistsEntity: TracksInPlaylistsEntity)
}