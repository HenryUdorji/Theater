package com.henryudorji.theater.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.henryudorji.theater.data.model.Movie
import com.henryudorji.theater.data.model.MovieResponse
import kotlinx.coroutines.flow.Flow


@Dao
interface TheaterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieResponse(movieResponse: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovieResponse()

    @Query("SELECT * FROM movie")
    fun getPopularMovies(): Flow<List<Movie>>
    fun getUpcomingMovies(): Flow<List<Movie>>
    fun getTopRatedMovies(): Flow<List<Movie>>
}