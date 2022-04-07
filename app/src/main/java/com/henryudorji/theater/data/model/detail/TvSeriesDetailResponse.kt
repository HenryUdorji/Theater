package com.henryudorji.theater.data.model.detail


import com.google.gson.annotations.SerializedName
import com.henryudorji.theater.data.model.detail.Genre

data class TvSeriesDetailResponse(
        @SerializedName("backdrop_path")
        val backdropPath: String,
        @SerializedName("genres")
        val genres: List<Genre>,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("episode_runtime")
        val episodeRunTime: String?,
        @SerializedName("number_of_episodes")
        val numberOfEpisodes: Int?,
        @SerializedName("number_of_seasons")
        val numberOfSeasons: Int?,
        @SerializedName("original_name")
        val originalName: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("popularity")
        val popularity: Double,
        @SerializedName("poster_path")
        val posterPath: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("vote_average")
        val voteAverage: Double,
        @SerializedName("vote_count")
        val voteCount: Int
)