package com.henryudorji.theater.utils.resource

import com.henryudorji.theater.data.model.MovieResponse

//
// Created by hash on 5/2/2021.
//
sealed class HomeResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: HomeResource<T>()
    class Error<T>(message: String?, data: T? = null): HomeResource<T>(data, message)
    class Popular<T>(data: T? = null): HomeResource<T>(data)
    class UpcomingOrOnTheAir<T>(data: T? = null): HomeResource<T>(data)
    class TopRated<T>(data: T? = null): HomeResource<T>(data)
    class NowPlayingOrAiringToday<T>(data: T? = null): HomeResource<T>(data)
}