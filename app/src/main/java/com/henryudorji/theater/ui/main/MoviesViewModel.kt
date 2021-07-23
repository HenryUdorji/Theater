package com.henryudorji.theater.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.henryudorji.theater.R
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.resource.HomeResource
import com.henryudorji.theater.utils.resource.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

//
// Created by Henry on 5/27/2021.
//
class MoviesViewModel(
        private val movieRepository: MovieRepository,
        private val application: Application
): ViewModel() {

    private var moviePage = 1
    private var movieResponseData: MovieResponse? = null

    private var _popularMutableLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val popularLiveData: LiveData<Resource<MovieResponse>> = _popularMutableLiveData

    private var _topRatedMutableLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val topRatedLiveData: LiveData<Resource<MovieResponse>> = _topRatedMutableLiveData

    private var _upcomingOrOnTheAirMutableLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val upcomingOrOnTheAirLiveData: LiveData<Resource<MovieResponse>> = _upcomingOrOnTheAirMutableLiveData

    private var _nowPlayingOrAiringTodayMutableLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val nowPlayingOrAiringTodayLiveData: LiveData<Resource<MovieResponse>> = _nowPlayingOrAiringTodayMutableLiveData



    fun getMovies() = viewModelScope.launch {
        _popularMutableLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val popularResponse = movieRepository.getPopularMovies(moviePage)
                val topRatedResponse = movieRepository.getTopRatedMovies(moviePage)
                val upcomingResponse = movieRepository.getUpcomingMovies(moviePage)
                val nowPlayingResponse = movieRepository.getNowPlayingMovies(moviePage)

                _popularMutableLiveData.postValue(handleMovieResponse(popularResponse))
                _topRatedMutableLiveData.postValue(handleMovieResponse(topRatedResponse))
                _upcomingOrOnTheAirMutableLiveData.postValue(handleMovieResponse(upcomingResponse))
                _nowPlayingOrAiringTodayMutableLiveData.postValue(handleMovieResponse(nowPlayingResponse))
            }else {
                _popularMutableLiveData.postValue(Resource.Error(application.getString(R.string.connect_unavailable_msg)))
            }
        }catch (t: Throwable) {
            when(t) {
                //If there is no Internet or an Exception occurred only one LiveData would post the error
                is IOException -> _popularMutableLiveData.postValue(Resource.Error(application.getString(R.string.connect_unavailable_msg)))
                else -> _popularMutableLiveData.postValue(Resource.Error(application.getString(R.string.connect_unavailable_msg)))
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
        val connectivityManager = application.getSystemService(
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