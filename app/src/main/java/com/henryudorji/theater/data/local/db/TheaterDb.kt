package com.henryudorji.theater.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.henryudorji.theater.data.model.movie.Movie


@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class TheaterDb : RoomDatabase() {

    abstract fun theaterDao(): TheaterDao
}