package com.henryudorji.theater.utils

//
// Created by hash on 5/2/2021.
//
sealed class Resource<T>(
        val data: T? = null,
        val message: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>: Resource<T>()
}