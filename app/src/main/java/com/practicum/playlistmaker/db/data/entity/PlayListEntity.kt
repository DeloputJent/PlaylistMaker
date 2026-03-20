package com.practicum.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayList_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistID:Int,
    val playlistName:String,
    val playlistDescription:String,
    val pathToArtwork:String,
    val tracksId:String,
    val tracksAmount:Int,
)
