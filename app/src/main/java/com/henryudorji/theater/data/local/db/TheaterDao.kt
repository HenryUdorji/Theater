package com.henryudorji.theater.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.henryudorji.theater.data.model.movie.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface TheaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieResponse(movieResponse: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovieResponse()

    @Query("SELECT * FROM movie")
    fun getPopularMovies(): Flow<List<Movie>>
    @Query("SELECT * FROM movie")
    fun getUpcomingMovies(): Flow<List<Movie>>
    @Query("SELECT * FROM movie")
    fun getTopRatedMovies(): Flow<List<Movie>>
}