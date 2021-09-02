package com.henryudorji.theater.ui.home.search

import androidx.lifecycle.ViewModel
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
}