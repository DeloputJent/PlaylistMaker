package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao {
   @Insert(entity = TrackEntity::class,onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertFavorite(favoriteTrack: TrackEntity)

   @Query("SELECT * FROM favoriteTrack_table")
   suspend fun getFavorites(): List<TrackEntity>

    @Query("SELECT trackId FROM favoriteTrack_table")
    suspend fun getTracksId():List<String>

    @Delete (entity = TrackEntity::class)
    suspend fun dropOut(trackEntity: TrackEntity)
}