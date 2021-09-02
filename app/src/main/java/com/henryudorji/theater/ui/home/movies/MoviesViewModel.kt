package com.henryudorji.theater.ui.home.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henryudorji.theater.data.model.MovieResponse
import com.henryudorji.theater.data.repository.MovieRepository
import com.henryudorji.theater.utils.NetworkManager
import com.henryudorji.theater.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor (
    private val repository: MovieRepository,
    private val networkManager: NetworkManager
) : ViewModel() {

    private val networkObserver = networkManager.observeConnectionStatus

    private val _popularMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val popularMoviesLiveData: LiveData<Resource<MovieResponse>> = _popularMoviesLiveData

    private val _upcomingMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val upcomingMoviesLiveData: LiveData<Resource<MovieResponse>> = _upcomingMoviesLiveData

    private val _topRatedMoviesLiveData = MutableLiveData<Resource<MovieResponse>>()
    val topRatedLiveData: LiveData<Resource<MovieResponse>> = _topRatedMoviesLiveData

    private val _nowPlayingLiveData = MutableLiveData<Resource<MovieResponse>>()
    val nowPlayingLiveData: LiveData<Resource<MovieResponse>> = _nowPlayingLiveData

    val page = 1

    init {
        getNowPlayingAiringToday(page)
        getPopularMovies(page)
        getUpcomingMovies(page)
        getTopRatedMovies(page)
    }

    private fun getNowPlayingAiringToday(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _nowPlayingLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val nowPlayingAiringToday = repository.getNowPlayingMovies(page)
                if (nowPlayingAiringToday.isSuccessful) {
                    nowPlayingAiringToday.body()?.let { movieResponse ->
                        _nowPlayingLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _nowPlayingLiveData.postValue(
                    Resource.Error(nowPlayingAiringToday.errorBody().toString())
                )
            } catch (e: Exception) {
                _nowPlayingLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _nowPlayingLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    private fun getTopRatedMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _topRatedMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val topRatedMoviesResponse = repository.getTopRatedMovies(page)
                if (topRatedMoviesResponse.isSuccessful) {
                    topRatedMoviesResponse.body()?.let { movieResponse ->
                        _topRatedMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _topRatedMoviesLiveData.postValue(
                    Resource.Error(topRatedMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _topRatedMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _topRatedMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }

    private fun getUpcomingMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _upcomingMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val upcomingMoviesResponse = repository.getPopularMovies(page)
                if (upcomingMoviesResponse.isSuccessful) {
                    upcomingMoviesResponse.body()?.let { movieResponse ->
                        _upcomingMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _upcomingMoviesLiveData.postValue(
                    Resource.Error(upcomingMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _upcomingMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _upcomingMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }

    }

    private fun getPopularMovies(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        _popularMoviesLiveData.postValue(Resource.Loading())
        if (networkObserver.value == true) {
            try {
                val popularMoviesResponse = repository.getPopularMovies(page)
                if (popularMoviesResponse.isSuccessful) {
                    popularMoviesResponse.body()?.let { movieResponse ->
                        _popularMoviesLiveData.postValue(Resource.Success(movieResponse))
                    }
                }else  _popularMoviesLiveData.postValue(
                    Resource.Error(popularMoviesResponse.errorBody().toString())
                )
            } catch (e: Exception) {
                _popularMoviesLiveData.postValue(Resource.Error(e.localizedMessage))
            }
        }else {
            _popularMoviesLiveData.postValue(Resource.Error(
                "internet not available, check connection"
            ))
        }
    }


}