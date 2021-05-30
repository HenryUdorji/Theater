package com.henryudorji.theater.data.remote

import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//
// Created by hash on 5/2/2021.
//
interface ServiceApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page")
        page: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("language")
        language: String = "en-US"
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
            @Query("page")
            page: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("language")
            language: String = "en-US"
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
            @Query("page")
            page: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("language")
            language: String = "en-US"
    ): Response<MovieResponse>

    @GET("tv/popular")
    suspend fun getPopularTvSeries(
            @Query("page")
            page: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("language")
            language: String = "en-US"
    ): Response<MovieResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvSeries(
            @Query("page")
            page: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("language")
            language: String = "en-US"
    ): Response<MovieResponse>

    @GET("tv/upcoming")
    suspend fun getUpcomingTvSeries(
            @Query("page")
            page: Int = 1,
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("language")
            language: String = "en-US"
    ): Response<MovieResponse>
}
