package com.henryudorji.theater.ui.list

import androidx.lifecycle.ViewModel
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
}