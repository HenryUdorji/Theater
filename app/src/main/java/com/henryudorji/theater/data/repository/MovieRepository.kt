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
    suspend fun getOnTheAir(page: Int) = ServiceGenerator.api.getOnTheAirTvSeries(page)
    suspend fun getTopRatedTvSeries(page: Int) = ServiceGenerator.api.getTopRatedTvSeries(page)

    suspend fun getNowPlayingMovies(page: Int) = ServiceGenerator.api.getNowPlayingMovies(page)
    suspend fun getAiringTodayTvSeries(page: Int) = ServiceGenerator.api.getAiringTodayTvSeries(page)

    suspend fun getTrendingMovies(page: Int) = ServiceGenerator.api.getTrendingMovies(page)
    suspend fun getTrendingTvSeries(page: Int) = ServiceGenerator.api.getTrendingTvSeries(page)

    suspend fun getMovieDetails(movieID: Int) = ServiceGenerator.api.getMovieDetail(movieID)
    suspend fun getTvSeriesDetails(movieID: Int) = ServiceGenerator.api.getTvSeriesDetail(movieID)

    suspend fun getMovieRecommendation(movieID: Int) = ServiceGenerator.api.getMovieRecommendations(movieID)
    suspend fun getTvSeriesRecommendation(movieID: Int) = ServiceGenerator.api.getTvSeriesRecommendations(movieID)

    suspend fun getMovieReview(page: Int, movieID: Int) = ServiceGenerator.api.getMovieReviews(movieID, page)
    suspend fun getTvSeriesReview(page: Int, movieID: Int) = ServiceGenerator.api.getTvSeriesReviews(movieID, page)

    suspend fun getMovieCast(page: Int, movieID: Int) = ServiceGenerator.api.getMovieCredit(page, movieID)
    suspend fun getTvSeriesCast(page: Int, movieID: Int) = ServiceGenerator.api.getTvSeriesCredit(page, movieID)

    suspend fun getMovieVideo(movieID: Int) = ServiceGenerator.api.getMovieVideos(movieID)
    suspend fun getTvSeriesVideo(movieID: Int) = ServiceGenerator.api.getTvSeriesVideos(movieID)
}