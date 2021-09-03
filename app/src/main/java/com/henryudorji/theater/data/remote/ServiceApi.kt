package com.henryudorji.theater.data.remote

import com.henryudorji.theater.data.model.movie.MovieResponse
import com.henryudorji.theater.data.model.cast.CastResponse
import com.henryudorji.theater.data.model.detail.MovieDetailResponse
import com.henryudorji.theater.data.model.detail.TvSeriesDetailResponse
import com.henryudorji.theater.data.model.review.ReviewResponse
import com.henryudorji.theater.data.model.tv.TvSeriesResponse
import com.henryudorji.theater.data.model.video.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ServiceApi {

    companion object {
        const val API_KEY = "fb827c7470c2b05072c9106ba63ddbf4"
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    //MOVIES API -START
    @GET("movie/popular?api_key=$API_KEY&language=en-US")
    suspend fun getPopularMovies(
            @Query("page")
            page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/top_rated?api_key=$API_KEY&language=en-US")
    suspend fun getTopRatedMovies(
            @Query("page")
            page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/upcoming?api_key=$API_KEY&language=en-US")
    suspend fun getUpcomingMovies(
            @Query("page")
            page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}?api_key=$API_KEY&language=en-US")
    suspend fun getMovieDetail(
            @Path("movie_id")
            movieId: Int
    ): Response<MovieDetailResponse>

    @GET("movie/{movie_id}/credits?api_key=$API_KEY&language=en-US")
    suspend fun getMovieCredit(
            @Query("page")
            page: Int = 1,
            @Path("movie_id")
            movieId: Int
    ): Response<CastResponse>

    @GET("movie/{movie_id}/recommendations?api_key=$API_KEY&language=en-US")
    suspend fun getMovieRecommendations(
            @Path("movie_id")
            movieId: Int,
            @Query("page")
            page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/reviews?api_key=$API_KEY&language=en-US")
    suspend fun getMovieReviews(
            @Path("movie_id")
            movieId: Int,
            @Query("page")
            page: Int = 1
    ): Response<ReviewResponse>

    @GET("movie/{movie_id}/videos?api_key=$API_KEY&language=en-US")
    suspend fun getMovieVideos(
            @Path("movie_id")
            movieId: Int
    ): Response<VideoResponse>

    @GET("movie/now_playing?api_key=$API_KEY&language=en-US")
    suspend fun getNowPlayingMovies(
            @Query("page")
            page: Int = 1
    ): Response<MovieResponse>

    //MOVIES API -END


    //TV SERIES API
    @GET("tv/popular?api_key=$API_KEY&language=en-US")
    suspend fun getPopularTvSeries(
            @Query("page")
            page: Int = 1
    ): Response<TvSeriesResponse>

    @GET("tv/top_rated?api_key=$API_KEY&language=en-US")
    suspend fun getTopRatedTvSeries(
            @Query("page")
            page: Int = 1
    ): Response<TvSeriesResponse>

    @GET("tv/on_the_air?api_key=$API_KEY&language=en-US")
    suspend fun getOnTheAirTvSeries(
            @Query("page")
            page: Int = 1
    ): Response<TvSeriesResponse>

    @GET("tv/{tv_id}?api_key=$API_KEY&language=en-US")
    suspend fun getTvSeriesDetail(
            @Path("tv_id")
            tvId: Int
    ): Response<TvSeriesDetailResponse>

    @GET("tv/{tv_id}/credits?api_key=$API_KEY&language=en-US")
    suspend fun getTvSeriesCredit(
            @Path("tv_id")
            tvId: Int,
            @Query("page")
            page: Int = 1
    ): Response<CastResponse>

    @GET("tv/{tv_id}/recommendations?api_key=$API_KEY&language=en-US")
    suspend fun getTvSeriesRecommendations(
            @Path("tv_id")
            tvId: Int,
            @Query("page")
            page: Int = 1
    ): Response<TvSeriesResponse>

    @GET("tv/{tv_id}/reviews?api_key=$API_KEY&language=en-US")
    suspend fun getTvSeriesReviews(
            @Path("tv_id")
            tvId: Int,
            @Query("page")
            page: Int = 1
    ): Response<ReviewResponse>

    @GET("tv/{tv_id}/videos?api_key=$API_KEY&language=en-US")
    suspend fun getTvSeriesVideos(
            @Path("tv_id")
            tvId: Int
    ): Response<VideoResponse>

    @GET("tv/airing_today?api_key=$API_KEY&language=en-US")
    suspend fun getAiringTodayTvSeries(
            @Query("page")
            page: Int = 1
    ): Response<TvSeriesResponse>

    //TV_SERIES API - END


    //SEARCH API - START
    @GET("search/movie?api_key=$API_KEY&language=en-US&include_adult=false")
    suspend fun searchForMovies(
            @Query("page")
            page: Int = 1,
            @Query("query")
            query: String
    ): Response<MovieResponse>

    @GET("search/tv?api_key=$API_KEY&language=en-US&include_adult=false")
    suspend fun searchForTvSeries(
            @Query("page")
            page: Int = 1,
            @Query("query")
            query: String
    ): Response<TvSeriesResponse>

    //SEARCH API - END
}
