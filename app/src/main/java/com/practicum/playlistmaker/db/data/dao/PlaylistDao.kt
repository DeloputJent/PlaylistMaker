package com.practicum.playlistmaker.db.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.db.data.entity.PlayListEntity

interface PlaylistDao {
    @Insert(entity = PlayListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class,)
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("SELECT * FROM PlayList_table")
    suspend fun getPlayLists(): List<PlayListEntity>

    @Query("SELECT tracksId FROM PlayList_table")
    suspend fun getTracksId():String

    @Delete (entity = PlayListEntity::class)
    suspend fun deletePlaylist(playList: PlayListEntity)
}