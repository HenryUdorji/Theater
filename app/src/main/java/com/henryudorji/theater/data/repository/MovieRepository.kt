package com.henryudorji.theater.data.repository

import com.henryudorji.theater.data.remote.ServiceApi
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
    suspend fun getTvSeriesDetails(tvSeriesID: Int) = api.getTvSeriesDetail(tvSeriesID)

    suspend fun getMovieRecommendation(movieID: Int) = api.getMovieRecommendations(movieID)
    suspend fun getTvSeriesRecommendation(tvSeriesID: Int) = api.getTvSeriesRecommendations(tvSeriesID)

    suspend fun getMovieReview(movieID: Int, page: Int) = api.getMovieReviews(movieID, page)
    suspend fun getTvSeriesReview(tvSeriesID: Int, page: Int) = api.getTvSeriesReviews(tvSeriesID, page)

    suspend fun getMovieCast(movieID: Int, page: Int) = api.getMovieCredit(movieID, page)
    suspend fun getTvSeriesCast(tvSeriesID: Int, page: Int) = api.getTvSeriesCredit(tvSeriesID, page)

    suspend fun getMovieTrailer(movieID: Int) = api.getMovieTrailer(movieID)
    suspend fun getTvSeriesTrailer(tvSeriesID: Int) = api.getTvSeriesTrailer(tvSeriesID)

    suspend fun searchForMovies(page: Int, searchQuery: String) = api.searchForMovies(page, searchQuery)
    suspend fun searchForTvSeries(page: Int, searchQuery: String) = api.searchForTvSeries(page, searchQuery)
}