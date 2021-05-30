package com.henryudorji.theater.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.henryudorji.theater.R
import com.henryudorji.theater.base.BaseApplication
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

//
// Created by Henry on 5/27/2021.
//
class MoviesViewModel(
        private val movieRepository: MovieRepository,
        application: Application
): AndroidViewModel(application) {

    private var moviePage = 1
    private var movieResponseData: MovieResponse? = null
    private var _movieResponse: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieResponse: LiveData<Resource<MovieResponse>> = _movieResponse

    init {
        getMovies()
    }

    fun getMovies() = viewModelScope.launch {
        _movieResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = movieRepository.getPopularMovies(moviePage)
                _movieResponse.postValue(handleMovieResponse(response))
            }else {
                _movieResponse.postValue(Resource.Error(getApplication<BaseApplication>().getString(R.string.connect_unavailable_msg)))
            }
        }catch (t: Throwable) {
            when(t) {
                is IOException -> _movieResponse.postValue(Resource.Error(getApplication<BaseApplication>().getString(R.string.network_fail_msg)))
                else -> _movieResponse.postValue(Resource.Error(getApplication<BaseApplication>().getString(R.string.conversion_error_msg)))
            }
        }
    }

    private fun handleMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { movieResponse ->
                moviePage++ // for pagination
                if (movieResponseData == null) {
                    movieResponseData = movieResponse
                }else {
                    val oldData = movieResponseData?.movies
                    val newData = movieResponse.movies
                    oldData?.addAll(newData)
                }
                return Resource.Success(movieResponseData ?: movieResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<BaseApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}