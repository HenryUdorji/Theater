package com.henryudorji.theater.data.remote

import com.henryudorji.theater.data.model.GenreResponse
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//
// Created by hash on 5/2/2021.
//
interface ServiceApi {

    @GET("genre/movie/list?api_key=fb827c7470c2b05072c9106ba63ddbf4&language=en-US")
    suspend fun getGenreList(): Response<GenreResponse>


    @GET("genre/movie/list")
    suspend fun getMovieList(
        @Query("page")
        page: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<MovieResponse>
}
