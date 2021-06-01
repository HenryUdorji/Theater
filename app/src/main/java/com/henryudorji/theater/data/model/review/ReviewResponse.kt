package com.henryudorji.theater.data.model.review


import com.google.gson.annotations.SerializedName

data class ReviewResponse(
        @SerializedName("id")
    val id: Int,
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val reviews: List<Review>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)