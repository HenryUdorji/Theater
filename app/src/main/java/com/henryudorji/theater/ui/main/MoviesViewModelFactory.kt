package com.henryudorji.theater.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.henryudorji.theater.data.repository.MovieRepository

//
// Created by  on 6/10/2021.
//
@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(
    private val repository: MovieRepository,
    private val application: Application
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository, application) as T
    }
}