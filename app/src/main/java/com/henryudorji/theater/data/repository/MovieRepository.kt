package com.henryudorji.theater.data.repository

import com.henryudorji.theater.data.remote.ServiceGenerator

//
// Created by Henry on 5/27/2021.
//
class MovieRepository() {
    suspend fun getPopularMovies(page: Int) = ServiceGenerator.api.getPopularMovies(page)
    suspend fun getUpcomingMovies(page: Int) = ServiceGenerator.api.getUpcomingMovies(page)
    suspend fun getTopRatedMovies(page: Int) = ServiceGenerator.api.getTopRatedMovies(page)

    suspend fun getPopularTvSeries(page: Int) = ServiceGenerator.api.getPopularTvSeries(page)
    suspend fun getUpcomingTvSeries(page: Int) = ServiceGenerator.api.getUpcomingTvSeries(page)
    suspend fun getTopRatedTvSeries(page: Int) = ServiceGenerator.api.getTopRatedTvSeries(page)
}