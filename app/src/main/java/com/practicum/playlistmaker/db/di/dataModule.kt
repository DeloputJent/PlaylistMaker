package com.practicum.playlistmaker.db.di

import androidx.room.Room
import com.practicum.playlistmaker.db.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.trackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), trackDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConvertor() }
}