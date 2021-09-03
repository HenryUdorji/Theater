package com.henryudorji.theater.data.repository

import com.henryudorji.theater.data.local.db.TheaterDb
import com.henryudorji.theater.data.remote.ServiceApi
import com.henryudorji.theater.data.remote.ServiceGenerator
import javax.inject.Inject


class MovieRepository @Inject constructor (
    private val api: ServiceApi
) {
    suspend fun getPopularMovies(page: Int) = api.getPopularMovies(page)
    suspend fun getUpcomingMovies(page: Int) = api.getUpcomingMovies(page)
    suspend fun getTopRatedMovies(page: Int) = api.getTopRatedMovies(page)

    suspend fun getPopularTvSeries(page: Int) = api.getPopularTvSeries(page)
    suspend fun getOnTheAir(page: Int) = api.getOnTheAirTvSeries(page)
    suspend fun getTopRatedTvSeries(page: Int) = api.getTopRatedTvSeries(page)

    suspend fun getMovieDetails(movieID: Int) = api.getMovieDetail(movieID)
    suspend fun getTvSeriesDetails(movieID: Int) = api.getTvSeriesDetail(movieID)

    suspend fun getMovieRecommendation(movieID: Int) = api.getMovieRecommendations(movieID)
    suspend fun getTvSeriesRecommendation(movieID: Int) = api.getTvSeriesRecommendations(movieID)

    suspend fun getMovieReview(page: Int, movieID: Int) = api.getMovieReviews(movieID, page)
    suspend fun getTvSeriesReview(page: Int, movieID: Int) = api.getTvSeriesReviews(movieID, page)

    suspend fun getMovieCast(page: Int, movieID: Int) = api.getMovieCredit(page, movieID)
    suspend fun getTvSeriesCast(page: Int, movieID: Int) = api.getTvSeriesCredit(page, movieID)

    suspend fun getMovieVideo(movieID: Int) = api.getMovieVideos(movieID)
    suspend fun getTvSeriesVideo(movieID: Int) = api.getTvSeriesVideos(movieID)
}