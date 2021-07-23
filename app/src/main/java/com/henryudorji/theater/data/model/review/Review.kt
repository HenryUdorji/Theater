package com.henryudorji.theater.data.model.review


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetails,
    @SerializedName("content")
    val content: String,
    @SerializedName("id")
    val id: String
)