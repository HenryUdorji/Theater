package com.henryudorji.theater.data.model.tv


import com.google.gson.annotations.SerializedName

data class TvSeriesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val tvSeries: List<TvSeries>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)