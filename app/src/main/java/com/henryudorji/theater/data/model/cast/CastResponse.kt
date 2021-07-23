package com.henryudorji.theater.data.model.cast


import com.google.gson.annotations.SerializedName

data class CastResponse(
    @SerializedName("cast")
    val cast: List<Cast>,
    @SerializedName("id")
    val id: Int
)