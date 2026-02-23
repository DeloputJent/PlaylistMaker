package com.practicum.playlistmaker.db.di

import androidx.room.Room
import com.practicum.playlistmaker.db.TrackDatabase
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.data.impl.FavoriteRepositoryImpl
import com.practicum.playlistmaker.db.domain.FavoriteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), TrackDatabase::class.java, "database.db")
            .build()
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    factory { TrackDbConvertor() }
}