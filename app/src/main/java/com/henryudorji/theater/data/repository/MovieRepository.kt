package com.henryudorji.theater.data.repository

import com.henryudorji.theater.data.remote.ServiceGenerator

//
// Created by Henry on 5/27/2021.
//
class MovieRepository() {
    suspend fun getMovieList(page: Int) = ServiceGenerator.api.getMovieList(page)
}